package pl.mfurman.memoro.main.expiring_map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.mfurman.memoro.main.CardScheduler;

@Getter
@RequiredArgsConstructor
public abstract class RemoveEntryListener {

  private final String username;
  private final CardScheduler scheduler;

  public abstract void onEntryRemoved();
}
