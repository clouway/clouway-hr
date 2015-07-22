package com.clouway.hr.adapter.user.google.oauth;

import com.clouway.hr.adapter.user.google.oauth.token.TokenRepository;
import com.clouway.hr.adapter.user.google.oauth.token.UserTokens;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Service
@At("/oauth")
public class OAuthService {
  private final Provider<HttpServletRequest> requestProvider;
  private final OAuthAuthentication oAuthAuthentication;
  private final TokenRepository tokenRepository;
  private final OAuthUser oAuthUser;


  @Inject
  public OAuthService(Provider<HttpServletRequest> requestProvider,
                      OAuthAuthentication oAuthAuthentication,
                      TokenRepository tokenRepository,
                      OAuthUser oAuthUser) {

    this.requestProvider = requestProvider;
    this.oAuthAuthentication = oAuthAuthentication;
    this.tokenRepository = tokenRepository;
    this.oAuthUser = oAuthUser;
  }


  @At("/callback")
  @Get
  public Reply processOAuthCallback() throws IOException {

    final HttpServletRequest request = requestProvider.get();

    final String authorizationCode = request.getParameter("code");
    final GoogleTokenResponse googleTokenResponse = oAuthAuthentication.getGoogleTokenResponse(authorizationCode);
    final GoogleCredential googleCredential = oAuthAuthentication.getGoogleCredential(googleTokenResponse);
    final String email = oAuthUser.getEmail();

    final String accessToken = googleCredential.getAccessToken();
    final String refreshToken = googleCredential.getRefreshToken();
    final UserTokens userTokens = new UserTokens(accessToken, refreshToken);
    tokenRepository.store(email, userTokens);

    return Reply.saying().redirect("/");

  }

  @At("/credential")
  @Get
  public Reply createNewCredentialsFlow() {

    final GoogleAuthorizationCodeFlow flow = oAuthAuthentication.getGoogleAuthorizationFlow();
    final String securityState = oAuthAuthentication.generateGoogleSecurityState();
    final String authorizationUrl = oAuthAuthentication.getAuthorizationUrl(flow, securityState);

    final HttpServletRequest request = requestProvider.get();

    oAuthAuthentication.setGoogleSecurityState(request, securityState);

    return Reply.saying().redirect(authorizationUrl);
  }

  @At("/logout")
  @Get
  public Reply logOut() {

    final UserService userService = UserServiceFactory.getUserService();
    final String logoutURL = userService.createLogoutURL("http://google.com");

    return Reply.saying().redirect(logoutURL);
  }


}