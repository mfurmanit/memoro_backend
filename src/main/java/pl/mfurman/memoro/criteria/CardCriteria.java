package pl.mfurman.memoro.criteria;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.lang.Nullable;
import pl.mfurman.memoro.entities.QCard;
import pl.mfurman.memoro.entities.QCardCollection;
import pl.mfurman.memoro.enums.CardSide;

import java.util.UUID;

import static org.springframework.util.StringUtils.hasText;
import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;

public class CardCriteria {

  public static Predicate cardPredicate(final UUID collectionId,
                                        @Nullable final CardSide side,
                                        @Nullable final String value) {
    final QCard qCard = QCard.card;
    final BooleanBuilder builder = new BooleanBuilder();

    builder.and(qCard.collection.id.eq(collectionId));
    builder.and(qCard.collection.user.id.eq(getLoggedUserId()));

    if (side != null && hasText(value)) {
      return switch (side) {
        case FRONT -> builder.and(qCard.front.containsIgnoreCase(value)).getValue();
        case BACK -> builder.and(qCard.back.containsIgnoreCase(value)).getValue();
      };
    } else return builder.getValue();
  }
}
