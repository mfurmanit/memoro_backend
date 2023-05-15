package pl.mfurman.memoro.criteria;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.lang.Nullable;
import pl.mfurman.memoro.entities.QCardCollection;

import static org.springframework.util.StringUtils.hasText;
import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;

public class CardCollectionCriteria {

  public static Predicate collectionPredicate(@Nullable final String value) {
    final QCardCollection qCardCollection = QCardCollection.cardCollection;
    final BooleanBuilder builder = new BooleanBuilder();

    builder.and(qCardCollection.user.id.eq(getLoggedUserId()));
    if (hasText(value)) builder.and(qCardCollection.name.containsIgnoreCase(value));

    return builder.getValue();
  }

  public static Predicate sharedPredicate(final boolean my) {
    final QCardCollection qCardCollection = QCardCollection.cardCollection;
    final BooleanBuilder builder = new BooleanBuilder();

    builder.and(qCardCollection.shared.isTrue());
    builder.and(qCardCollection.parent.isNull());
    if (my) builder.and(qCardCollection.user.id.eq(getLoggedUserId()));

    return builder.getValue();
  }
}
