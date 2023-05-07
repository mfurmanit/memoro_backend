package pl.mfurman.memoro.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ReportTuple(
  UUID id,
  LocalDate date
) {
}
