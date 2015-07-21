package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.CurrentUser;
import com.clouway.hr.core.OAuthAuthentication;
import com.clouway.hr.core.OAuthUser;
import com.clouway.hr.core.TokenRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.admin.directory.model.Group;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    tokenRepository.store(email, accessToken, refreshToken);

    return Reply.saying().redirect("/");

  }


  @At("/currentuser")
  @Get
  public Reply<CurrentUser> getCurrentUser() {

    final String email = oAuthUser.getEmail();
    final List<Group> userGroups = oAuthUser.getGroups(email);
    final Set<String> roles = oAuthUser.getRoles(email, userGroups);
    final CurrentUser currentUser = new CurrentUser(email, roles.contains("OWNER") || roles.contains("MANAGER"));

    return Reply.with(currentUser).as(Json.class);
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