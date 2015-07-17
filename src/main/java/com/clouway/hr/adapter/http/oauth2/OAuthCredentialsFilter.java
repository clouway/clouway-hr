package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.TokenRepository;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Singleton
public class OAuthCredentialsFilter implements Filter {
  private final TokenRepository tokenRepository;
  private final UserService userService;


  @Inject
  public OAuthCredentialsFilter(TokenRepository tokenRepository, UserService userService) {
    this.tokenRepository = tokenRepository;
    this.userService = userService;
  }


  public void init(FilterConfig config) throws ServletException {
  }


  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

    final User currentUser = userService.getCurrentUser();
    final String userEmail = currentUser.getEmail();

    final Map<String, String> tokens = tokenRepository.get(userEmail);

    if (tokens == null) {

      final HttpServletResponse response = (HttpServletResponse) resp;
      response.sendRedirect("/oauth/credential");
      return;
    }

    chain.doFilter(req, resp);
  }


  public void destroy() {
  }
}
