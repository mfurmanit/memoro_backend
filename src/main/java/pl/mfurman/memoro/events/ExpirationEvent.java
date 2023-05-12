package pl.mfurman.memoro.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.mfurman.memoro.entities.User;

import java.util.Set;

@Getter
public class ExpirationEvent extends ApplicationEvent {

  private final Set<User> users;

  public ExpirationEvent(final Object source,
                         final Set<User> users) {
    super(source);
    this.users = users;
  }
}
