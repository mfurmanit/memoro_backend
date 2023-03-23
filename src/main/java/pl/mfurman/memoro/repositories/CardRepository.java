package pl.mfurman.memoro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.mfurman.memoro.entities.Card;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID>, QuerydslPredicateExecutor<Card> {

  List<Card> findAllByCollectionId(final UUID id);
}
