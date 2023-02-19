package pl.mfurman.memoro.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mfurman.memoro.dto.UserResponse;
import pl.mfurman.memoro.services.UserService;

@RestController
@AllArgsConstructor
public class UserController {

  public final static String API_USER_CONTEXT = "/api/user";

  public final static String API_USER_LOGGED_IN = API_USER_CONTEXT + "/logged-in";

  private final UserService userService;

  @GetMapping(API_USER_LOGGED_IN)
  public boolean isUserLoggedIn() {
    return userService.isUserLoggedIn();
  }

  @GetMapping(API_USER_CONTEXT)
  public UserResponse getUserContext() {
    return userService.getUserContext();
  }
}
