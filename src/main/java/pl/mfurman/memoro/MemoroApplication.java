package pl.mfurman.memoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.mfurman.memoro.properties.AppProperties;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties({
  AppProperties.class,
})
public class MemoroApplication {

  public static void main(String[] args) {
    SpringApplication.run(MemoroApplication.class, args);
  }
}
