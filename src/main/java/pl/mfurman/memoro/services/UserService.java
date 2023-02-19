package pl.mfurman.memoro.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.mfurman.memoro.dto.UserResponse;
import pl.mfurman.memoro.entities.User;

import static pl.mfurman.memoro.mappers.UserMapper.toResponse;
import static pl.mfurman.memoro.utils.UserUtil.getPrincipal;

@Service
@AllArgsConstructor
public class UserService {

  public UserResponse getUserContext() {
    return isUserLoggedIn() ? toResponse(getPrincipal()) : null;
  }

  public boolean isUserLoggedIn() {
    return SecurityContextHolder.getContext() != null &&
      SecurityContextHolder.getContext().getAuthentication() != null &&
      SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;
  }
}
