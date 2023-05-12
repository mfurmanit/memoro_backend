package pl.mfurman.memoro.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import pl.mfurman.memoro.properties.AppProperties;
import pl.mfurman.memoro.repositories.UserRepository;

import static pl.mfurman.memoro.controllers.UserController.API_USER_ACTIVATE_PREFIX;
import static pl.mfurman.memoro.controllers.UserController.API_USER_CONTEXT;
import static pl.mfurman.memoro.controllers.UserController.API_USER_LOGGED_IN;
import static pl.mfurman.memoro.controllers.UserController.API_USER_REGISTER;
import static pl.mfurman.memoro.utils.StringConstants.LOGIN;
import static pl.mfurman.memoro.utils.StringConstants.REGISTER;
import static pl.mfurman.memoro.utils.StringConstants.SESSION_COOKIE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final AppProperties properties;
  private final UserRepository repository;

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
      .requestMatchers(
        "/", API_USER_CONTEXT, API_USER_LOGGED_IN, API_USER_REGISTER, LOGIN, REGISTER,
        API_USER_ACTIVATE_PREFIX + "/**",
        "/**.js", "/**.js.map", "/**.css", "/**.woff2",
        "/**.woff", "/**.ttf", "/**.ico", "/**.map", "/**.svg", "/assets/**",
        "/index.html"
      ).permitAll()
      .anyRequest()
      .authenticated()
      .and().formLogin().loginPage(LOGIN)
      .successHandler(new CustomSuccessHandler())
      .failureHandler(new CustomFailureHandler())
      .and()
      .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint(properties.getUrl()))
      .and()
      .csrf((csrf) -> csrf
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .csrfTokenRequestHandler(getRequestHandler())
      )
      .logout().deleteCookies(SESSION_COOKIE).invalidateHttpSession(true);

    return http.build();
  }

  public CsrfTokenRequestAttributeHandler getRequestHandler() {
    final CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName(null);
    return requestHandler;
  }

  @Bean
  public CustomUserDetailsService userDetailsService() {
    return new CustomUserDetailsService(repository);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
