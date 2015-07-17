package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.CurrentUser;
import com.clouway.hr.core.OAuthAuthentication;
import com.clouway.hr.core.OAuthUser;
import com.clouway.hr.core.TokenRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.model.Group;
import com.google.inject.util.Providers;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static com.clouway.hr.adapter.http.matchers.SitebricksReplyMatchers.contains;
import static com.clouway.hr.adapter.http.matchers.SitebricksReplyMatchers.hasStatusCode;
import static com.clouway.hr.adapter.http.matchers.SitebricksReplyMatchers.sayRedirectTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 15-7-10.
 *
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
  OAuthUser oAuthUser;


  @Test
  public void getCurrentUser() throws Exception {

    final OAuthService oAuthService = new OAuthService(Providers.of(request), oAuthAuthentication, tokenRepository, oAuthUser);

    final String userEmail = "email@domain.com";

    context.checking(new Expectations() {{

      oneOf(oAuthUser).getEmail();
      will(returnValue(userEmail));
      oneOf(oAuthUser).getGroups(userEmail);
      oneOf(oAuthUser).getRoles(userEmail, new ArrayList<Group>());
      will(returnValue(new HashSet<String>() {{
        add("MEMBER");
      }}));

    }});

    final Reply<CurrentUser> reply = oAuthService.getCurrentUser();

    assertThat(reply, contains(new CurrentUser(userEmail, false)));
    assertThat(reply, hasStatusCode(200));
  }

  @Test
  public void processOAuthCallback() throws Exception {

    final OAuthService oAuthService = new OAuthService(Providers.of(request), oAuthAuthentication, tokenRepository, oAuthUser);

    final String userEmail = "email@domain.com";

    final GoogleTokenResponse googleTokenResponse = new GoogleTokenResponse();

    final GoogleCredential fakeCredentials = getFakeGoogleCredential("accessTokenValue", "refreshTokenValue");

    context.checking(new Expectations() {{
      oneOf(request).getParameter("code");
      will(returnValue("someCodeValue"));
      oneOf(oAuthAuthentication).getGoogleTokenResponse("someCodeValue");
      will(returnValue(googleTokenResponse));
      oneOf(oAuthAuthentication).getGoogleCredential(googleTokenResponse);
      will(returnValue(fakeCredentials));
      oneOf(oAuthUser).getEmail();
      will(returnValue(userEmail));
      oneOf(tokenRepository).store(userEmail, "accessTokenValue", "refreshTokenValue");
    }});

    final Reply<Object> reply = oAuthService.processOAuthCallback();

    assertThat(reply, hasStatusCode(302));
    assertThat(reply, sayRedirectTo("/"));
  }

  @Test
  public void createNewCredentialFlow() throws Exception {

    final OAuthService oAuthService = new OAuthService(Providers.of(request), oAuthAuthentication, tokenRepository, oAuthUser);
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