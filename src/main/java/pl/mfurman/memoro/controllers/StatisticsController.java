package pl.mfurman.memoro.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mfurman.memoro.dto.DateRange;
import pl.mfurman.memoro.dto.StatisticsResponse;
import pl.mfurman.memoro.services.StatisticsService;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class StatisticsController {
  public final static String API_STATISTICS = "/api/statistics";
  public final static String API_STATISTICS_ID = API_STATISTICS + "/{collectionId}";

  private final StatisticsService statisticsService;

  @PostMapping(value = {API_STATISTICS, API_STATISTICS_ID})
  public StatisticsResponse prepareData(@RequestBody final DateRange range,
                                        @PathVariable final Optional<UUID> collectionId) {
    return statisticsService.prepareData(range, collectionId.orElse(null));
  }
}
