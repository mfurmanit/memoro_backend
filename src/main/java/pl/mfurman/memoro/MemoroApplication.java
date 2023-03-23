package pl.mfurman.memoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pl.mfurman.memoro.properties.AppProperties;

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
