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
import pl.mfurman.memoro.dto.StatisticsResponse;
import pl.mfurman.memoro.dto.TimeTuple;
import pl.mfurman.memoro.entities.Card;
import pl.mfurman.memoro.utils.TimeDifferenceCollector;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static pl.mfurman.memoro.utils.CommonUtil.toStream;
import static pl.mfurman.memoro.utils.StatisticsUtil.fillDateRangeWithZeros;
import static pl.mfurman.memoro.utils.StatisticsUtil.mapCards;

@Service
@RequiredArgsConstructor
public class StatisticsService {

  @PersistenceContext
  private final EntityManager entityManager;

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
      }, new TimeDifferenceCollector()));

    final Map<LocalDate, Long> dateSums = map.entrySet().stream()
      .collect(Collectors.groupingBy(entry -> entry.getKey().toLocalDate(),
        Collectors.summingLong(Map.Entry::getValue)));

    return fillDateRangeWithZeros(dateSums, dateRange);
  }

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
