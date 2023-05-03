package pl.mfurman.memoro.repositories;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;
import pl.mfurman.memoro.entities.Card;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID>, QuerydslPredicateExecutor<Card> {

  @NonNull
  List<Card> findAll(@NonNull final Predicate predicate);
}
