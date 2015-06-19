package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.CredentialRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Singleton
public class OAuthCredentialsFilter implements Filter {
  private final CredentialRepository credentialRepository;
  private final UserService userService;


  @Inject
  public OAuthCredentialsFilter(CredentialRepository credentialRepository, UserService userService) {
    this.credentialRepository = credentialRepository;
    this.userService = userService;
  }


  public void init(FilterConfig config) throws ServletException {
  }


  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
    System.out.println("Filter");

    final User currentUser = userService.getCurrentUser();
    final String userEmail = currentUser.getEmail();

    final GoogleCredential credential = credentialRepository.get(userEmail);

    if (credential == null) {
      final HttpServletResponse response = (HttpServletResponse) resp;
      response.sendRedirect("/oauth/credential");
      return;
    }

    chain.doFilter(req, resp);
  }


  public void destroy() {
  }
}
