package com.clouway.hr.adapter.apis.google.user.oauth;

import com.clouway.hr.adapter.apis.google.user.oauth.token.TokenRepository;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Singleton
public class OAuthCredentialsFilter implements Filter {
  private final TokenRepository tokenRepository;
  private final UserService userService;
  private final OAuthAuthentication oAuthAuthentication;


  @Inject
  public OAuthCredentialsFilter(TokenRepository tokenRepository, UserService userService, OAuthAuthentication oAuthAuthentication) {
    this.tokenRepository = tokenRepository;
    this.userService = userService;
    this.oAuthAuthentication = oAuthAuthentication;
  }


  public void init(FilterConfig config) throws ServletException {
  }


  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

    HttpServletRequest request = (HttpServletRequest) req;
    String uri = request.getRequestURI();

    if (!uri.contains("/oauth")) {

      final User currentUser = userService.getCurrentUser();

      if (currentUser != null && !tokenRepository.containsTokens(currentUser.getEmail())) {

        final HttpServletResponse response = (HttpServletResponse) resp;
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    chain.doFilter(req, resp);

  }

  public void destroy() {
  }
}
