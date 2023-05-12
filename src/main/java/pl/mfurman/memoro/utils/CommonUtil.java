package pl.mfurman.memoro.utils;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static pl.mfurman.memoro.utils.StringConstants.ENTITY_NOT_FOUND;

public interface CommonUtil {

  static <T> T getOrThrow(final Optional<T> entity) {
    return entity.orElseThrow(() -> new ApiException(ENTITY_NOT_FOUND));
  }

  static <T> Stream<T> toStream(final Collection<T> collection) {
    return Stream.ofNullable(collection).flatMap(Collection::stream);
  }

  static MailProperties bindProperties(final Environment environment) {
    return Binder.get(environment).bind("spring.mail", MailProperties.class).get();
  }
}
