package pl.mfurman.memoro.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TimeTuple(
  UUID id,
  LocalDateTime date
) {
}
