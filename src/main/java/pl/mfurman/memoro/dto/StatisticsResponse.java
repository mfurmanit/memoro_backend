package pl.mfurman.memoro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class StatisticsResponse {

  private Map<LocalDate, Long> viewedCards;
  private Map<LocalDate, Long> reviewedCards;
  private Map<LocalDate, Long> reviewTimes;
}
