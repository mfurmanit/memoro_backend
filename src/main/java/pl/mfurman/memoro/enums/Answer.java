package pl.mfurman.memoro.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Answer {
  AGAIN(0), // complete blackout
  HARD(1), // incorrect response
  GOOD(3), // correct response
  EASY(5); // perfect response

  private final int quality;
}
