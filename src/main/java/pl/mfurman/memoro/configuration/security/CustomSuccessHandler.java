package pl.mfurman.memoro.configuration.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Override
  protected void handle(final HttpServletRequest request,
                        final HttpServletResponse response,
                        final Authentication authentication) {
    response.setStatus(200);
  }
}
