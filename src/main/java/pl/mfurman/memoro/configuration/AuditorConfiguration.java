package pl.mfurman.memoro.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;

@Configuration
public class AuditorConfiguration {

  @Bean
  AuditorAware<UUID> auditorProvider() {
    return () -> Optional.of(getLoggedUserId());
  }
}
