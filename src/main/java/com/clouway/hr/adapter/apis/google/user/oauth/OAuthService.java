package com.clouway.hr.adapter.apis.google.user.oauth;

import com.clouway.hr.adapter.apis.google.user.oauth.token.TokenRepository;
import com.clouway.hr.adapter.apis.google.user.oauth.token.UserTokens;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Service
@At("/oauth")
public class OAuthService {
  private final Provider<HttpServletRequest> requestProvider;
  private final OAuthAuthentication oAuthAuthentication;
  private final TokenRepository tokenRepository;
  private final UserService userService;


  @Inject
  public OAuthService(Provider<HttpServletRequest> requestProvider,
                      OAuthAuthentication oAuthAuthentication,
                      TokenRepository tokenRepository,
                      UserService userService) {

    this.requestProvider = requestProvider;
    this.oAuthAuthentication = oAuthAuthentication;
    this.tokenRepository = tokenRepository;
    this.userService = userService;
  }

  @At("/callback")
  @Get
  public Reply processOAuthCallback() throws IOException {

    final HttpServletRequest request = requestProvider.get();

    if (oAuthAuthentication.hasValidateGoogleSecurityState(request)) {

      final String authorizationCode = request.getParameter("code");
      final GoogleTokenResponse googleTokenResponse = oAuthAuthentication.getGoogleTokenResponse(authorizationCode);
      final String email = userService.getCurrentUser().getEmail();

      final String accessToken = googleTokenResponse.getAccessToken();
      final String refreshToken = googleTokenResponse.getRefreshToken();

      final UserTokens userTokens = new UserTokens(accessToken, refreshToken);
      tokenRepository.store(email, userTokens);

      return Reply.saying().redirect("/");

    }

    return Reply.saying().redirect("/oauth/credential");

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

    final HttpServletRequest request = requestProvider.get();
    final String scheme = request.getScheme();
    final String host = request.getHeader("Host");

    final String logoutURL = userService.createLogoutURL(scheme + "://" + host);

    return Reply.saying().redirect(logoutURL);
  }
}