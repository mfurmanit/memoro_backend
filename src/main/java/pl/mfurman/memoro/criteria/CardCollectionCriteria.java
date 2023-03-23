package pl.mfurman.memoro.criteria;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import pl.mfurman.memoro.entities.QCardCollection;

import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;

public class CardCollectionCriteria {
  public static Predicate userPredicate() {
    return new BooleanBuilder().and(
      QCardCollection.cardCollection.user.id.eq(getLoggedUserId())
    ).getValue();
  }
}
