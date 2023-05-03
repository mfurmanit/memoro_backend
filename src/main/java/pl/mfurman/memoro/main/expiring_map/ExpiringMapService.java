package pl.mfurman.memoro.main.expiring_map;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class ExpiringMapService {

  private volatile ScheduledExecutorService scheduledExecutorService;

  private synchronized void initExecutor() {
    if (scheduledExecutorService == null) {
      synchronized (ExpiringMap.class) {
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
          scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }
      }
    }
  }

  public ScheduledExecutorService getExecutor() {
    initExecutor();
    return scheduledExecutorService;
  }

  @PreDestroy
  private void onDestroy() {
    scheduledExecutorService.shutdownNow();
  }
}
