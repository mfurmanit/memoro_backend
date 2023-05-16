package pl.mfurman.memoro.services;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.mfurman.memoro.dto.UserRequest;
import pl.mfurman.memoro.dto.UserResponse;
import pl.mfurman.memoro.entities.User;
import pl.mfurman.memoro.events.ActivationEvent;
import pl.mfurman.memoro.events.ExpirationEvent;
import pl.mfurman.memoro.properties.AppProperties;
import pl.mfurman.memoro.repositories.UserRepository;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static pl.mfurman.memoro.criteria.UserCriteria.expiredPredicate;
import static pl.mfurman.memoro.mappers.UserMapper.toEntity;
import static pl.mfurman.memoro.mappers.UserMapper.toResponse;
import static pl.mfurman.memoro.utils.CommonUtil.getOrThrow;
import static pl.mfurman.memoro.utils.StringConstants.ACTIVATION_SUBJECT;
import static pl.mfurman.memoro.utils.StringConstants.CANNOT_ACTIVATE;
import static pl.mfurman.memoro.utils.StringConstants.EXPIRATION_SUBJECT;
import static pl.mfurman.memoro.utils.StringConstants.USER_ACTIVATED;
import static pl.mfurman.memoro.utils.UserUtil.getLoggedUserId;
import static pl.mfurman.memoro.utils.UserUtil.getPrincipal;

@Service
@AllArgsConstructor
public class UserService {

  private final PasswordEncoder encoder;
  private final UserRepository repository;
  private final EmailService emailService;
  private final AppProperties properties;
  private final ApplicationEventPublisher publisher;

  public UserResponse getUserContext() {
    return isUserLoggedIn() ? toResponse(getPrincipal()) : null;
  }

  public boolean isUserLoggedIn() {
    return SecurityContextHolder.getContext() != null &&
      SecurityContextHolder.getContext().getAuthentication() != null &&
      SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;
  }

  @Transactional
  public void register(final UserRequest request) {
    final User user = repository.save(toEntity(request, encoder.encode(request.getPassword()).toCharArray()));
    publisher.publishEvent(new ActivationEvent(this, user));
  }

  @Transactional
  public void activate(final UUID id, final HttpServletResponse response) throws IOException {
    final User user = getOne(id);

    if (LocalDateTime.now().isAfter(user.getActivationLinkExpirationDate()))
      throw new ApiException(CANNOT_ACTIVATE);

    if (user.isActive())
      throw new ApiException(USER_ACTIVATED);

    user.setActive(true);
    repository.save(user);

    response.sendRedirect(properties.getUrl());
  }

  @TransactionalEventListener(ActivationEvent.class)
  public void sendActivationMail(final ActivationEvent event) {
    final User user = event.getUser();
    emailService.sendActivationMail(ACTIVATION_SUBJECT, user.getId(), user.getEmail());
  }

  @TransactionalEventListener(ExpirationEvent.class)
  public void sendExpirationMail(final ExpirationEvent event) {
    final Set<User> users = event.getUsers();
    emailService.sendExpirationMail(EXPIRATION_SUBJECT, users);
  }

  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
  public void deleteExpired() {
    deleteAll(getExpiredUsers());
  }

  @Transactional
  public void deleteAll(final Set<User> users) {
    repository.deleteAll(users);
    publisher.publishEvent(new ExpirationEvent(this, users));
  }

  public Set<User> getExpiredUsers() {
    return StreamSupport.stream(repository.findAll(expiredPredicate()).spliterator(), false)
      .collect(Collectors.toSet());
  }

  public User getOne(final UUID id) {
    return getOrThrow(repository.findById(id));
  }

  public User getLogged() {
    return getOne(getLoggedUserId());
  }
}
