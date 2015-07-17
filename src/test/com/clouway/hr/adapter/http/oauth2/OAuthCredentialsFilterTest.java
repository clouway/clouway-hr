package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.TokenRepository;
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
import java.util.HashMap;
import java.util.Map;

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
  TokenRepository tokenRepository;
  @Mock
  UserService userService;

  @Test
  public void doFilter() throws Exception {

    final OAuthCredentialsFilter filter = new OAuthCredentialsFilter(tokenRepository, userService);
    final String userEmail = "email@domain.com";
    final String userDomain = "domain.com";
    final User googleUser = new User(userEmail, userDomain);
    final Map<String, String> notNullValue = new HashMap<String, String>() {{
      put("someKey", "someValue");
    }};

    context.checking(new Expectations() {{
      oneOf(userService).getCurrentUser();
      will(returnValue(googleUser));
      oneOf(tokenRepository).get(userEmail);
      will(returnValue(notNullValue));
      oneOf(filterChain).doFilter(request, response);
    }});

    filter.doFilter(request, response, filterChain);
  }


}