package pl.mfurman.memoro.utils.expiring_map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.mfurman.memoro.services.CardScheduler;

@Getter
@RequiredArgsConstructor
public abstract class RemoveEntryListener {

  private final String username;
  private final CardScheduler scheduler;

  public abstract void onEntryRemoved();
}
