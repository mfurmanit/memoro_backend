package pl.mfurman.memoro.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

import static pl.mfurman.memoro.utils.CommonUtil.bindProperties;

@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {

  private final Environment environment;

  @Bean
  public JavaMailSender mailSender() {
    final MailProperties properties = bindProperties(environment);

    final JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setHost(properties.getHost());
    sender.setPort(properties.getPort());
    sender.setUsername(properties.getUsername());
    sender.setPassword(properties.getPassword());
    sender.setDefaultEncoding("UTF-8");

    final Map<String, String> mailProperties = properties.getProperties();
    final Properties props = sender.getJavaMailProperties();
    props.putAll(mailProperties);

    return sender;
  }
}
