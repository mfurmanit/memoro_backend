package pl.mfurman.memoro.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pl.mfurman.memoro.dto.DateRange;
import pl.mfurman.memoro.dto.ReportTuple;
import pl.mfurman.memoro.dto.StatisticsResponse;
import pl.mfurman.memoro.dto.TimeTuple;
import pl.mfurman.memoro.entities.Card;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static pl.mfurman.memoro.utils.CommonUtil.toStream;

@Service
@RequiredArgsConstructor
public class StatisticsService {

  @PersistenceContext
  private final EntityManager entityManager;

  public static Map<LocalDate, Long> fillDateRangeWithZeros(Map<LocalDate, Long> inputMap, DateRange dateRange) {
    long daysBetween = ChronoUnit.DAYS.between(dateRange.getFrom(), dateRange.getTo()) + 1;

    return Stream.iterate(dateRange.getFrom(), date -> date.plusDays(1))
      .limit(daysBetween)
      .collect(Collectors.toMap(Function.identity(), date -> inputMap.getOrDefault(date, 0L), (a, b) -> a));
  }

  @Transactional
  public StatisticsResponse prepareData(final DateRange dateRange, @Nullable final UUID collectionId) {
    return StatisticsResponse.builder()
      .viewedCards(getViewedCards(dateRange, collectionId))
      .reviewedCards(getReviewedCards(dateRange, collectionId))
      .reviewTimes(getReviewTimes(dateRange, collectionId))
      .build();
  }

  @SuppressWarnings("unchecked")
  public Map<LocalDate, Long> getViewedCards(final DateRange dateRange, @Nullable final UUID collectionId) {
    final AuditReader auditReader = AuditReaderFactory.get(entityManager);
    final List<Object[]> result = prepareQueryForViewedCards(auditReader, dateRange, collectionId).getResultList();
    return fillDateRangeWithZeros(mapCards(result), dateRange);
  }

  @SuppressWarnings("unchecked")
  public Map<LocalDate, Long> getReviewedCards(final DateRange dateRange, @Nullable final UUID collectionId) {
    final AuditReader auditReader = AuditReaderFactory.get(entityManager);
    final List<Object[]> result = prepareQueryForReviewedCards(auditReader, dateRange, collectionId).getResultList();
    return fillDateRangeWithZeros(mapCards(result), dateRange);
  }

  @SuppressWarnings("unchecked")
  public Map<LocalDate, Long> getReviewTimes(final DateRange dateRange, @Nullable final UUID collectionId) {
    final AuditReader auditReader = AuditReaderFactory.get(entityManager);
    final List<Object[]> result = prepareQueryForReviewTime(auditReader, dateRange, collectionId).getResultList();

    final List<TimeTuple> tuples = toStream(result)
      .map(row -> new TimeTuple((UUID) row[0], ((LocalDateTime) row[1])))
      .sorted(Comparator.comparing(TimeTuple::date))
      .collect(toList());

    final Map<LocalDateTime, Long> map = toStream(tuples)
      .collect(Collectors.groupingBy(time -> {
        // Store the minute-of-hour field.
        int minutes = time.date().getMinute();

        // Determine how many minutes we are above the nearest 10-minute interval.
        int minutesOver = minutes % 5;

        // Truncate the time to the minute field (zeroing out seconds and nanoseconds),
        // and force the number of minutes to be at a 10-minute interval.
        return time.date().truncatedTo(ChronoUnit.MINUTES).withMinute(minutes - minutesOver);
      }, timeDifferenceCollector()));

    final Map<LocalDate, Long> dateSums = map.entrySet().stream()
      .collect(Collectors.groupingBy(entry -> entry.getKey().toLocalDate(),
        Collectors.summingLong(Map.Entry::getValue)));

    return fillDateRangeWithZeros(dateSums, dateRange);
  }

  public static Collector<TimeTuple, List<TimeTuple>, Long> timeDifferenceCollector() {
    return new Collector<>() {
      @Override
      public Supplier<List<TimeTuple>> supplier() {
        return ArrayList::new;
      }

      @Override
      public BiConsumer<List<TimeTuple>, TimeTuple> accumulator() {
        return List::add;
      }

      @Override
      public BinaryOperator<List<TimeTuple>> combiner() {
        return (list1, list2) -> {
          list1.addAll(list2);
          return list1;
        };
      }

      @Override
      public Function<List<TimeTuple>, Long> finisher() {
        return list -> {
          if (list.size() > 1) {
            LocalDateTime firstDate = list.get(0).date();
            LocalDateTime lastDate = list.get(list.size() - 1).date();
            return Duration.between(firstDate, lastDate).toSeconds();
          } else {
            return 0L;
          }
        };
      }

      @Override
      public Set<Characteristics> characteristics() {
        return EnumSet.noneOf(Characteristics.class);
      }
    };
  }

  public Map<LocalDate, Long> mapCards(final List<Object[]> result) {
    return toStream(result)
      .map(row -> new ReportTuple((UUID) row[0], ((LocalDateTime) row[1]).toLocalDate()))
      .distinct()
      .collect(groupingBy(ReportTuple::date, Collectors.counting()));
  }

//  public void generateReport(final DateRange dateRange, @Nullable final UUID collectionId, final ReportType type) {
//    switch (type) {
//      case VIEWED_CARDS -> getViewedCards(dateRange, collectionId);
//      case REVIEW_TIME -> getReviewedCards(dateRange, collectionId);
//    }
//  }

  private AuditQuery prepareQueryForViewedCards(final AuditReader auditReader,
                                                final DateRange dateRange,
                                                @Nullable final UUID collectionId) {
    final AuditQuery auditQuery = auditReader.createQuery()
      .forRevisionsOfEntity(Card.class, false, false)
      .addProjection(AuditEntity.property("id"))
      .addProjection(AuditEntity.property("modifiedDate"))
      .add(AuditEntity.revisionType().eq(RevisionType.MOD))
      .add(AuditEntity.property("reviewed").eq(true))
      .add(AuditEntity.property("modifiedDate").between(
        dateRange.getFrom().atStartOfDay(),
        dateRange.getTo().atTime(LocalTime.MAX)
      ))
      .addOrder(AuditEntity.id().desc());

    if (collectionId != null)
      return auditQuery.add(AuditEntity.property("collection_id").eq(collectionId));
    else return auditQuery;
  }

  private AuditQuery prepareQueryForReviewedCards(final AuditReader auditReader,
                                                  final DateRange dateRange,
                                                  @Nullable final UUID collectionId) {
    return prepareQueryForViewedCards(auditReader, dateRange, collectionId)
      .add(AuditEntity.property("eFactor").hasChanged());
  }

  private AuditQuery prepareQueryForReviewTime(final AuditReader auditReader,
                                               final DateRange dateRange,
                                               @Nullable final UUID collectionId) {
    final AuditQuery auditQuery = auditReader.createQuery()
      .forRevisionsOfEntity(Card.class, false, false)
      .addProjection(AuditEntity.property("id"))
      .addProjection(AuditEntity.property("modifiedDate"))
      .add(AuditEntity.revisionType().eq(RevisionType.MOD))
      .add(AuditEntity.property("reviewed").eq(true))
      .add(AuditEntity.property("modifiedDate").hasChanged())
      .add(AuditEntity.property("modifiedDate").between(
        dateRange.getFrom().atStartOfDay(),
        dateRange.getTo().atTime(LocalTime.MAX)
      ))
      .addOrder(AuditEntity.property("modifiedDate").desc());

    if (collectionId != null)
      return auditQuery.add(AuditEntity.property("collection_id").eq(collectionId));
    else return auditQuery;
  }
}
