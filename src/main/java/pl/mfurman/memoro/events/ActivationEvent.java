package pl.mfurman.memoro.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.mfurman.memoro.entities.User;

@Getter
public class ActivationEvent extends ApplicationEvent {

  private final User user;

  public ActivationEvent(final Object source,
                         final User user) {
    super(source);
    this.user = user;
  }
}
