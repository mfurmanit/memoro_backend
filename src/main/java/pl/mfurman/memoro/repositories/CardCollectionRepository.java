package pl.mfurman.memoro.repositories;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;
import pl.mfurman.memoro.entities.CardCollection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardCollectionRepository extends JpaRepository<CardCollection, UUID>, QuerydslPredicateExecutor<CardCollection> {

  @NonNull
  List<CardCollection> findAll(@NonNull final Predicate predicate);

  Optional<CardCollection> findByIdAndSharedIsTrue(@NonNull final UUID id);
}
