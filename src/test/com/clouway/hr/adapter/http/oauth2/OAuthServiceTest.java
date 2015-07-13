package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.CredentialRepository;
import com.clouway.hr.core.OAuthHelper;
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
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;

import static com.clouway.hr.adapter.http.SitebricksReplyAssertion.assertIsRepliedWith;
import static com.clouway.hr.adapter.http.SitebricksReplyAssertion.assertThatReplyRedirectUriIs;
import static com.clouway.hr.adapter.http.SitebricksReplyAssertion.assertThatReplyStatusIs;

/**
 * Created on 15-7-10.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  OAuthHelper oAuthHelper;
  @Mock
  CredentialRepository credentialRepository;
  @Mock
  UserService userService;
  @Mock
  HttpServletRequest request;
  @Mock
  Principal principal;


  @Test
  public void getCurrentUser() throws Exception {

    final OAuthService oAuthService = new OAuthService(Providers.of(request), oAuthHelper, credentialRepository, userService);

    final String userEmail = "email@domain.com";

    context.checking(new Expectations() {{

      oneOf(request).getUserPrincipal();
      will(returnValue(principal));
      oneOf(principal).getName();
      will(returnValue(userEmail));

    }});

    final Reply<String> reply = oAuthService.getCurrentUser();

    assertThatReplyStatusIs(reply, 200);
    assertIsRepliedWith(reply, userEmail);
  }

  @Test
  public void processOAuthCallback() throws Exception {

    final OAuthService oAuthService = new OAuthService(Providers.of(request), oAuthHelper, credentialRepository, userService);

    final String userEmail = "email@domain.com";
    final String userDomain = "domain.com";
    final User googleUser = new User(userEmail, userDomain);

    final GoogleTokenResponse googleTokenResponse = new GoogleTokenResponse();
    googleTokenResponse.setAccessToken("accessToken");
    googleTokenResponse.setRefreshToken("refreshToken");

    final GoogleCredential credentials = new GoogleCredential();

    context.checking(new Expectations() {{
      oneOf(request).getParameter("code");
      will(returnValue("someCodeValue"));
      oneOf(oAuthHelper).getGoogleTokenResponse("someCodeValue");
      will(returnValue(googleTokenResponse));
      oneOf(oAuthHelper).getGoogleCredential(googleTokenResponse);
      will(returnValue(credentials));
      oneOf(userService).getCurrentUser();
      will(returnValue(googleUser));
      oneOf(credentialRepository).store(userEmail, credentials);
    }});

    final Reply<Object> reply = oAuthService.processOAuthCallback();
    assertThatReplyStatusIs(reply, 302);
    assertThatReplyRedirectUriIs(reply, "/home");

  }

  @Test
  public void createNewCredentialFlow() throws Exception {

    final OAuthService oAuthService = new OAuthService(Providers.of(request), oAuthHelper, credentialRepository, userService);
    final GoogleAuthorizationCodeFlow googleAuthorizationFlow = getGoogleAuthorizationCodeFlow();

    context.checking(new Expectations() {{
      oneOf(oAuthHelper).getGoogleAuthorizationFlow();
      will(returnValue(googleAuthorizationFlow));
      oneOf(oAuthHelper).generateGoogleSecurityState();
      will(returnValue("someSecurityState"));
      oneOf(oAuthHelper).getAuthorizationUrl(googleAuthorizationFlow, "someSecurityState");
      will(returnValue("someAuthorizationUrl"));
      oneOf(oAuthHelper).setGoogleSecurityState(request, "someSecurityState");
    }});

    final Reply reply = oAuthService.createNewCredentialsFlow();

    assertThatReplyStatusIs(reply, 302);
    assertThatReplyRedirectUriIs(reply, "someAuthorizationUrl");
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
}