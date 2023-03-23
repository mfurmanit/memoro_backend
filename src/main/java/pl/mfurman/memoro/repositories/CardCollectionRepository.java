package pl.mfurman.memoro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.mfurman.memoro.entities.CardCollection;

import java.util.UUID;

public interface CardCollectionRepository extends JpaRepository<CardCollection, UUID>, QuerydslPredicateExecutor<CardCollection> {
}
