package pl.mfurman.memoro.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CardSide {

  @JsonProperty("front")
  FRONT,

  @JsonProperty("back")
  BACK
}
