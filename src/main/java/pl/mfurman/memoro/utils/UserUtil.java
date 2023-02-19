package pl.mfurman.memoro.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.mfurman.memoro.entities.User;

import java.util.UUID;

public interface UserUtil {

  static UUID getLoggedUserId() {
    return getPrincipal().getId();
  }

  static String getLoggedUserName() {
    return getPrincipal().getUsername();
  }

  static boolean isUser() {
    return getAuthentication() != null && getAuthentication().getPrincipal() instanceof User;
  }

  static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  static User getPrincipal() {
    return ((User) getAuthentication().getPrincipal());
  }
}
