package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.CredentialRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created on 15-7-10.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthCredentialsFilterTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ServletRequest request;
  @Mock
  ServletResponse response;
  @Mock
  FilterChain filterChain;
  @Mock
  CredentialRepository credentialRepository;
  @Mock
  UserService userService;

  @Test
  public void doFilter() throws Exception {

    final OAuthCredentialsFilter filter = new OAuthCredentialsFilter(credentialRepository, userService);
    final String userEmail = "email@domain.com";
    final String userDomain = "domain.com";
    final User googleUser = new User(userEmail, userDomain);
    final GoogleCredential notNullValue = new GoogleCredential();

    context.checking(new Expectations() {{
      oneOf(userService).getCurrentUser();
      will(returnValue(googleUser));
      oneOf(credentialRepository).get(userEmail);
      will(returnValue(notNullValue));
      oneOf(filterChain).doFilter(request, response);
    }});

    filter.doFilter(request, response, filterChain);
  }


}