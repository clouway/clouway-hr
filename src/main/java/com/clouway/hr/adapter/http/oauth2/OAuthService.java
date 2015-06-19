package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.CredentialRepository;
import com.clouway.hr.core.OAuthHelper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.appengine.api.users.User;
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
import java.security.Principal;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Service
@At("/oauth")
class OAuthService {
  private final Provider<HttpServletRequest> requestProvider;
  private final OAuthHelper oAuthHelper;
  private final CredentialRepository credentialRepository;
  private final UserService userService;


  @Inject
  public OAuthService(Provider<HttpServletRequest> requestProvider,
                      OAuthHelper oAuthHelper,
                      CredentialRepository credentialRepository,
                      UserService userService) {

    this.requestProvider = requestProvider;
    this.oAuthHelper = oAuthHelper;
    this.credentialRepository = credentialRepository;
    this.userService = userService;
  }


  @At("/callback")
  @Get
  public Reply processOAuthCallback() throws IOException {

    HttpServletRequest request = requestProvider.get();

    final String authorizationCode = request.getParameter("code");
    final GoogleTokenResponse googleTokenResponse = oAuthHelper.getGoogleTokenResponse(authorizationCode);
    final GoogleCredential googleCredential = oAuthHelper.getGoogleCredential(googleTokenResponse);

    final User currentUser = userService.getCurrentUser();
    final String email = currentUser.getEmail();

    credentialRepository.store(email, googleCredential);

    return Reply.saying().redirect("/home");

  }


  @At("/currentuser")
  @Get
  public Reply<String> getCurrentUser() {

    final HttpServletRequest request = requestProvider.get();
    final Principal principal = request.getUserPrincipal();

    return Reply.with(principal.getName()).as(Json.class);
  }


  @At("/credential")
  @Get
  public Reply createNewCredentialsFlow() {

    final GoogleAuthorizationCodeFlow flow = oAuthHelper.getGoogleAuthorizationFlow();
    final String securityState = oAuthHelper.generateGoogleSecurityState();
    final String authorizationUrl = oAuthHelper.getAuthorizationUrl(flow, securityState);

    final HttpServletRequest request = requestProvider.get();

    oAuthHelper.setGoogleSecurityState(request, securityState);

    return Reply.saying().redirect(authorizationUrl);
  }


  @At("/logout")
  @Get
  public Reply logOut() {

    final UserService userService = UserServiceFactory.getUserService();
    final String logoutURL = userService.createLogoutURL("/home");

    return Reply.saying().redirect(logoutURL);
  }


}
