package pl.mfurman.memoro.configuration.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.Serializable;

import static pl.mfurman.memoro.utils.StringConstants.API;
import static pl.mfurman.memoro.utils.StringConstants.LOGIN_REDIRECT;
import static pl.mfurman.memoro.utils.StringConstants.UNAUTHORIZED_ERROR;

@AllArgsConstructor
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint, Serializable {

  private String baseUrl;

  @Override
  public void commence(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AuthenticationException authException) throws IOException {
    if (request.getRequestURL() != null && request.getRequestURL().toString().contains(API))
      response.sendError(401, UNAUTHORIZED_ERROR);
    else if (request.getRequestURL() != null) {
      final String requestUrl = request.getRequestURL().toString();
      final String redirectUrl = requestUrl.substring(baseUrl.length());
      response.sendRedirect(String.format(LOGIN_REDIRECT, redirectUrl));
    }
  }
}
