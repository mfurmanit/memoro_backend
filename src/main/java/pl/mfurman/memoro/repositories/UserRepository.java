package pl.mfurman.memoro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import pl.mfurman.memoro.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, QuerydslPredicateExecutor<User> {

  Optional<User> findByUsername(final @Param("username") String username);
}
