package pl.mfurman.memoro.utils;

import pl.mfurman.memoro.dto.DateRange;
import pl.mfurman.memoro.dto.StatisticTuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static pl.mfurman.memoro.utils.CommonUtil.toStream;

public interface StatisticsUtil {

  static Map<LocalDate, Long> fillDateRangeWithZeros(Map<LocalDate, Long> inputMap, DateRange dateRange) {
    long daysBetween = ChronoUnit.DAYS.between(dateRange.getFrom(), dateRange.getTo()) + 1;

    return Stream.iterate(dateRange.getFrom(), date -> date.plusDays(1))
      .limit(daysBetween)
      .collect(Collectors.toMap(Function.identity(), date -> inputMap.getOrDefault(date, 0L), (a, b) -> a));
  }

  static Map<LocalDate, Long> mapCards(final List<Object[]> result) {
    return toStream(result)
      .map(row -> new StatisticTuple((UUID) row[0], ((LocalDateTime) row[1]).toLocalDate()))
      .distinct()
      .collect(groupingBy(StatisticTuple::date, Collectors.counting()));
  }
}
