package pl.mfurman.memoro.services;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.mfurman.memoro.entities.User;
import pl.mfurman.memoro.properties.AppProperties;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static pl.mfurman.memoro.controllers.UserController.API_USER_ACTIVATE_PREFIX;
import static pl.mfurman.memoro.utils.CommonUtil.bindProperties;
import static pl.mfurman.memoro.utils.CommonUtil.toStream;
import static pl.mfurman.memoro.utils.StringConstants.CANNOT_LOAD_TEMPLATE;
import static pl.mfurman.memoro.utils.StringConstants.CANNOT_SEND;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender sender;
  private final Environment environment;
  private final AppProperties properties;

  public void sendActivationMail(final String subject, final UUID id,
                                 final String receiver) {
    try {

      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      final InputStream inputStream = classLoader.getResourceAsStream(
        "templates/activation-mail.html"
      );
      final String url = properties.getUrl() + API_USER_ACTIVATE_PREFIX + "/" + id;

      if (inputStream == null) throw new ApiException(CANNOT_LOAD_TEMPLATE);

      final String mail = IOUtils
        .toString(inputStream, StandardCharsets.UTF_8)
        .replace("%URL%", url);

      sendMailAsynchronously(receiver, subject, mail);

    } catch (final Exception exception) {
      log.error(exception.getMessage());
      throw new ApiException(CANNOT_SEND);
    }
  }

  public void sendExpirationMail(final String subject, final Set<User> receivers) {
    try {

      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      final InputStream inputStream = classLoader.getResourceAsStream("templates/expiration-mail.html");

      if (inputStream == null) throw new ApiException(CANNOT_LOAD_TEMPLATE);

      final String mail = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      sendMailAsynchronously(prepareEmailReceivers(receivers), subject, mail);

    } catch (final Exception exception) {
      log.error(exception.getMessage());
      throw new ApiException(CANNOT_SEND);
    }
  }

  private void sendMailAsynchronously(final String receivers, final String subject, final String text) {
    final ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
    emailExecutor.execute(() -> sendMail(receivers, subject, text));
    emailExecutor.shutdown();
  }

  private void sendMail(final String receivers, final String subject, final String text) {
    try {
      if (hasText(receivers)) {
        final MimeMessage mail = sender.createMimeMessage();
        mail.addRecipients(Message.RecipientType.TO, InternetAddress.parse(receivers));

        final MimeMessageHelper helper = new MimeMessageHelper(mail, true);

        final MailProperties properties = bindProperties(environment);

        helper.setFrom(new InternetAddress(properties.getUsername(), "Memoro"));
        helper.setSubject(subject);
        helper.setText(text, true);

        final MimeMultipart multipart = new MimeMultipart("related");
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setContent(text, "text/html; charset=UTF-8");
        multipart.addBodyPart(messageBodyPart);

        mail.setContent(multipart);
        sender.send(mail);
      }
    } catch (final MessagingException | IOException exception) {
      log.error(exception.getMessage());
      throw new ApiException(CANNOT_SEND);
    }
  }

  public static String prepareEmailReceivers(final Set<User> receivers) {
    return toStream(receivers).map(User::getEmail).filter(Objects::nonNull).collect(Collectors.joining(","));
  }
}
