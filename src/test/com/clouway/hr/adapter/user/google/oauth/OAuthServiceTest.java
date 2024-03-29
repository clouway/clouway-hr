package com.clouway.hr.adapter.user.google.oauth;

import com.clouway.hr.adapter.apis.google.user.oauth.OAuth2Provider;
import com.clouway.hr.adapter.apis.google.user.oauth.OAuthAuthentication;
import com.clouway.hr.adapter.apis.google.user.oauth.OAuthService;
import com.clouway.hr.adapter.apis.google.user.oauth.token.TokenRepository;
import com.clouway.hr.adapter.apis.google.user.oauth.token.UserTokens;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.util.Providers;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.clouway.hr.test.custom.matchers.SitebricksReplyMatchers.hasStatusCode;
import static com.clouway.hr.test.custom.matchers.SitebricksReplyMatchers.sayRedirectTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  OAuthAuthentication oAuthAuthentication;
  @Mock
  TokenRepository tokenRepository;
  @Mock
  HttpServletRequest request;
  @Mock
  UserService userService;

  private OAuthService oAuthService;

  @Before
  public void setUp() throws Exception {
    oAuthService = new OAuthService(Providers.of(request), oAuthAuthentication, tokenRepository, userService);
  }


  @Test
  public void processOAuthCallback() throws Exception {

    final String userEmail = "email@domain.com";

    final GoogleTokenResponse googleTokenResponse = new GoogleTokenResponse();

    final GoogleCredential fakeCredentials = getFakeGoogleCredential("accessTokenValue", "refreshTokenValue");

    final User googleUser = new User(userEmail,"");

    context.checking(new Expectations() {{
      oneOf(oAuthAuthentication).hasValidateGoogleSecurityState(request);
      will(returnValue(true));
      oneOf(request).getParameter("code");
      will(returnValue("someCodeValue"));
      oneOf(oAuthAuthentication).getGoogleTokenResponse("someCodeValue");
      will(returnValue(googleTokenResponse));
      oneOf(userService).getCurrentUser();
      will(returnValue(googleUser));
      oneOf(tokenRepository).store(with(same(userEmail)), with(any(UserTokens.class)));
    }});

    final Reply<Object> reply = oAuthService.processOAuthCallback();

    assertThat(reply, hasStatusCode(302));
    assertThat(reply, sayRedirectTo("/"));
  }

  @Test
  public void processOAuthCallbackWithInvalidSecurityState() throws Exception {

    final String userEmail = "email@domain.com";

    final GoogleTokenResponse googleTokenResponse = new GoogleTokenResponse();

    final GoogleCredential fakeCredentials = getFakeGoogleCredential("accessTokenValue", "refreshTokenValue");

    final User googleUser = new User(userEmail,"");

    context.checking(new Expectations() {{
      oneOf(oAuthAuthentication).hasValidateGoogleSecurityState(request);
      will(returnValue(false));
    }});

    final Reply<Object> reply = oAuthService.processOAuthCallback();

    assertThat(reply, hasStatusCode(302));
    assertThat(reply, sayRedirectTo("/oauth/credential"));
  }

  @Test
  public void createNewCredentialFlow() throws Exception {

    final GoogleAuthorizationCodeFlow googleAuthorizationFlow = getGoogleAuthorizationCodeFlow();

    context.checking(new Expectations() {{
      oneOf(oAuthAuthentication).getGoogleAuthorizationFlow();
      will(returnValue(googleAuthorizationFlow));
      oneOf(oAuthAuthentication).generateGoogleSecurityState();
      will(returnValue("someSecurityState"));
      oneOf(oAuthAuthentication).getAuthorizationUrl(googleAuthorizationFlow, "someSecurityState");
      will(returnValue("someAuthorizationUrl"));
      oneOf(oAuthAuthentication).setGoogleSecurityState(request, "someSecurityState");
    }});

    final Reply reply = oAuthService.createNewCredentialsFlow();

    assertThat(reply, hasStatusCode(302));
    assertThat(reply, sayRedirectTo("someAuthorizationUrl"));
  }

  private GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow() {
    return new GoogleAuthorizationCodeFlow.Builder(

            new NetHttpTransport(),
            new JacksonFactory(),
            OAuth2Provider.GOOGLE_CLIENT_ID,
            OAuth2Provider.GOOGLE_CLIENT_SECRET,
            Arrays.asList(""))
            .setAccessType("offline")
            .setApprovalPrompt("force").build();
  }

  public GoogleCredential getFakeGoogleCredential(String accessToken, String refreshToken) {
    return new GoogleCredential.Builder()

            .setJsonFactory(new JacksonFactory())
            .setTransport(new NetHttpTransport())
            .setClientSecrets(OAuth2Provider.GOOGLE_CLIENT_SECRET, OAuth2Provider.GOOGLE_CLIENT_ID).build()
            .setAccessToken(accessToken)
            .setRefreshToken(refreshToken);
  }

}