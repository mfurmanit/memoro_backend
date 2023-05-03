package pl.mfurman.memoro.utils.expiring_map;

import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static pl.mfurman.memoro.utils.CommonUtil.toStream;

public class ExpiringMap<K, V> extends ConcurrentHashMap<K, V> {

  private volatile int expirationTime;
  private final RemoveEntryListener removeEntryListener;
  private volatile ScheduledExecutorService scheduledExecutorService;
  private final TimeUnit timeUnit;
  private volatile Map<K, ScheduledFuture<?>> futures = new HashMap<>();

  public ExpiringMap(final int expirationTime,
                     final TimeUnit timeUnit,
                     final RemoveEntryListener removeEntryListener,
                     final ScheduledExecutorService scheduledExecutorService) {
    super();
    this.expirationTime = expirationTime;
    this.timeUnit = timeUnit;
    this.removeEntryListener = removeEntryListener;
    this.scheduledExecutorService = scheduledExecutorService;
  }

  @Override
  public V put(@NonNull final K key, @NonNull final V value) {
    final V putEntry = super.put(key, value);
    planExpiration(key);
    return putEntry;
  }

  @Override
  public void putAll(final Map<? extends K, ? extends V> map) {
    super.putAll(map);
    toStream(map.entrySet()).forEach(entry -> planExpiration(entry.getKey()));
  }

  @Override
  public V putIfAbsent(final K key, final V value) {
    final V putEntry = super.putIfAbsent(key, value);
    planExpiration(key);
    return putEntry;
  }

  public void resetKeyExpirationTime(final K key) {
    if (futures.containsKey(key)) {
      synchronized (key) {
        final ScheduledFuture<?> future = futures.get(key);
        future.cancel(false);
        futures.remove(key);
        planExpiration(key);
      }
    }
  }

  private void planExpiration(final K key) {
    if (scheduledExecutorService != null) {
      synchronized (key) {
        futures.put(key, scheduledExecutorService.schedule(
          () -> {
            remove(key);
            futures.remove(key);
            if (removeEntryListener != null) {
              removeEntryListener.onEntryRemoved();
            }
          },
          expirationTime,
          timeUnit
        ));
      }
    }
  }

  public synchronized void setExpirationTime(final int expirationTime) {
    this.expirationTime = expirationTime;
  }
}
