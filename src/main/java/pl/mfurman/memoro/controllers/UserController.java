package pl.mfurman.memoro.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mfurman.memoro.dto.UserRequest;
import pl.mfurman.memoro.dto.UserResponse;
import pl.mfurman.memoro.services.UserService;

import java.io.IOException;
import java.util.UUID;

import static pl.mfurman.memoro.utils.StringConstants.API;

@RestController
@AllArgsConstructor
public class UserController {

  public final static String API_USER_CONTEXT = API + "/user";
  public final static String API_USER_LOGGED_IN = API_USER_CONTEXT + "/logged-in";
  public final static String API_USER_REGISTER = API_USER_CONTEXT + "/register";
  public final static String API_USER_ACTIVATE_PREFIX = API_USER_CONTEXT + "/activate";
  public final static String API_USER_ACTIVATE = API_USER_ACTIVATE_PREFIX + "/{id}";

  private final UserService userService;

  @GetMapping(API_USER_LOGGED_IN)
  public boolean isUserLoggedIn() {
    return userService.isUserLoggedIn();
  }

  @GetMapping(API_USER_CONTEXT)
  public UserResponse getUserContext() {
    return userService.getUserContext();
  }

  @PostMapping(API_USER_REGISTER)
  public void register(@RequestBody @Valid final UserRequest request) {
    userService.register(request);
  }

  @GetMapping(API_USER_ACTIVATE)
  public void activate(@PathVariable final UUID id, final HttpServletResponse response) throws IOException {
    userService.activate(id, response);
  }
}
