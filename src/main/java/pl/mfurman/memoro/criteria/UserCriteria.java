package pl.mfurman.memoro.criteria;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import pl.mfurman.memoro.entities.QUser;

import java.time.LocalDateTime;

public class UserCriteria {

  public static Predicate expiredPredicate() {
    final QUser qUser = QUser.user;

    return new BooleanBuilder()
      .and(qUser.activationLinkExpirationDate.before(LocalDateTime.now()))
      .and(qUser.active.eq(Boolean.FALSE))
      .getValue();
  }
}
