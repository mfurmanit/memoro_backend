package pl.mfurman.memoro.services;

import org.springframework.stereotype.Service;
import pl.mfurman.memoro.entities.Card;
import pl.mfurman.memoro.enums.Answer;
import pl.mfurman.memoro.utils.expiring_map.ExpiringMap;
import pl.mfurman.memoro.utils.expiring_map.ExpiringMapService;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static pl.mfurman.memoro.utils.StringConstants.CANNOT_ANSWER;
import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;

@Service
public class LearningService {

  private final CardService service;
  private final ExpiringMap<UUID, CardScheduler> schedulers;

  public LearningService(final CardService service,
                         final ExpiringMapService expiringMapService) {
    this.service = service;
    this.schedulers = new ExpiringMap<>(
      20, TimeUnit.MINUTES, null, expiringMapService.getExecutor()
    );
  }

  public Card start(final UUID collectionId) {
    final UUID userId = getLoggedUserId();

    if (schedulers.containsKey(userId)) {
      final CardScheduler scheduler = schedulers.get(userId);
      if (collectionId.equals(scheduler.getCollectionId())) {
        schedulers.resetKeyExpirationTime(userId);
        return schedulers.get(userId).getNextCardToReview();
      } else {
        schedulers.remove(userId);
        return createScheduler(userId, collectionId);
      }
    } else return createScheduler(userId, collectionId);
  }

  private Card createScheduler(final UUID userId, final UUID collectionId) {
    final CardScheduler scheduler = new CardScheduler(service, collectionId);
    schedulers.put(userId, scheduler);
    return scheduler.getNextCardToReview();
  }

  public void stop() {
    schedulers.remove(getLoggedUserId());
  }

  public Card answer(final UUID cardId, final Answer answer) {
    final UUID userId = getLoggedUserId();

    if (schedulers.containsKey(userId)) {
      schedulers.resetKeyExpirationTime(userId);
      return schedulers.get(userId).answer(cardId, answer);
    } else throw new ApiException(CANNOT_ANSWER);
  }
}
