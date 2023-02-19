package pl.mfurman.memoro.configuration.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

import static pl.mfurman.memoro.utils.StringConstants.BAD_CREDENTIALS;

@Component
@AllArgsConstructor
public class CustomFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(final HttpServletRequest httpServletRequest,
                                      final HttpServletResponse httpServletResponse,
                                      final AuthenticationException exception) throws IOException {
    httpServletResponse.setCharacterEncoding("UTF-8");
    httpServletResponse.setStatus(500);
    httpServletResponse.getWriter().print(BAD_CREDENTIALS);
    httpServletResponse.getWriter().flush();
    httpServletResponse.getWriter().close();
  }
}
