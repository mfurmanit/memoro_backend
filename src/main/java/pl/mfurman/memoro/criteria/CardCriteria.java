package pl.mfurman.memoro.criteria;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.lang.Nullable;
import pl.mfurman.memoro.entities.QCard;
import pl.mfurman.memoro.enums.CardSide;

import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.util.StringUtils.hasText;
import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;

public class CardCriteria {

  public static Predicate cardPredicate(final UUID collectionId,
                                        @Nullable final boolean onlyFavorites,
                                        @Nullable final CardSide side,
                                        @Nullable final String value) {
    final QCard qCard = QCard.card;
    final BooleanBuilder builder = new BooleanBuilder();

    builder.and(qCard.collection.id.eq(collectionId));
    builder.and(
      qCard.collection.user.id.eq(getLoggedUserId()).or(
        qCard.collection.shared.isTrue()
      )
    );

    if (onlyFavorites) builder.and(qCard.isFavorite.isTrue());

    if (side != null && hasText(value)) {
      return switch (side) {
        case FRONT -> builder.and(qCard.front.containsIgnoreCase(value)).getValue();
        case BACK -> builder.and(qCard.back.containsIgnoreCase(value)).getValue();
      };
    } else return builder.getValue();
  }

  public static Predicate reviewPredicate(final UUID collectionId) {
    final QCard qCard = QCard.card;
    final BooleanBuilder builder = new BooleanBuilder();

    builder.and(qCard.collection.id.eq(collectionId));
    builder.and(qCard.collection.user.id.eq(getLoggedUserId()));
    builder.and(qCard.nextReviewDate.loe(now()));

    return builder.getValue();
  }
}
