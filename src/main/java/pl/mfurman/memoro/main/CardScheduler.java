package pl.mfurman.memoro.main;

import lombok.Getter;
import pl.mfurman.memoro.entities.Card;
import pl.mfurman.memoro.enums.Answer;
import pl.mfurman.memoro.services.CardService;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import static pl.mfurman.memoro.utils.StringConstants.BAD_COLLECTION;

public class CardScheduler {

  @Getter
  private final UUID collectionId;

  private Queue<Card> reviewQueue;
  private final CardService cardService;

  public CardScheduler(final CardService cardService,
                       final UUID collectionId) {
    this.cardService = cardService;
    this.collectionId = collectionId;
    this.initializeReviewQueue(collectionId);
  }

  private void initializeReviewQueue(final UUID collectionId) {
    final List<Card> cards = cardService.getAllForReview(collectionId);
    this.reviewQueue = new LinkedList<>(cards);
  }

  public Card getNextCardToReview() {
    return reviewQueue.peek();
  }

  public Card answer(final UUID cardId, final Answer answer) {
    final Card card = cardService.getOneById(cardId);
    if (!card.getCollection().getId().equals(collectionId)) throw new ApiException(BAD_COLLECTION);
    checkAnswer(card, answer);
    cardService.saveAfterAnswer(card);
    if (!Answer.AGAIN.equals(answer)) reviewQueue.remove();
    return reviewQueue.peek();
  }

  private void checkAnswer(final Card card, final Answer answer) {
    double eFactor = card.getEFactor();
    int repetition = card.getRepetition();
    int interval = card.getInterval();

    if (Answer.AGAIN.equals(answer)) {
      repetition = 0;
      interval = 0;
    } else if (Answer.HARD.equals(answer)) {
      repetition = 0;
      interval = 1;
    } else {
      final int quality = answer.getQuality();

      eFactor = Math.max(eFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality)) * 0.02), 1.3);
      repetition = repetition + 1;

      if (repetition == 1) {
        interval = 1;
      } else if (repetition == 2) {
        interval = 6;
      } else {
        interval = (int) Math.round((interval - 1) * eFactor);
      }
    }

    card.setEFactor(eFactor);
    card.setRepetition(repetition);
    card.setInterval(interval);
    card.setNextReviewDate(LocalDateTime.now().plusDays(interval));
  }
}

