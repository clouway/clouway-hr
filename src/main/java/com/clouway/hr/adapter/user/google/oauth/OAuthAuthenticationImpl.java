package com.clouway.hr.adapter.user.google.oauth;

import com.clouway.hr.adapter.user.google.oauth.token.TokenRepository;
import com.clouway.hr.adapter.user.google.oauth.token.UserTokens;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2.Userinfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.lang.RandomStringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthAuthenticationImpl implements OAuthAuthentication {

  private final List<String> scopes;
  private final JacksonFactory jsonFactory;
  private final HttpTransport httpTransport;
  private final Provider<HttpServletRequest> requestProvider;
  private final TokenRepository tokenRepository;

  @Inject
  public OAuthAuthenticationImpl(@OAuthScopes List<String> scopes,
                                 JacksonFactory jsonFactory,
                                 HttpTransport httpTransport,
                                 Provider<HttpServletRequest> requestProvider,
                                 TokenRepository tokenRepository) {

    this.scopes = scopes;
    this.jsonFactory = jsonFactory;
    this.httpTransport = httpTransport;
    this.requestProvider = requestProvider;
    this.tokenRepository = tokenRepository;
  }

  @Override
  public GoogleAuthorizationCodeFlow getGoogleAuthorizationFlow() {
    return new GoogleAuthorizationCodeFlow.Builder(

            httpTransport,
            jsonFactory,
            OAuth2Provider.GOOGLE_CLIENT_ID,
            OAuth2Provider.GOOGLE_CLIENT_SECRET,
            scopes)
            .setAccessType("offline")
            .setApprovalPrompt("force").build();
  }

  @Override
  public String getAuthorizationUrl(GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow, String securityState) {

    final String redirectUrl = getRedirectUrl();

    final GoogleAuthorizationCodeRequestUrl urlBuilder =
            googleAuthorizationCodeFlow

                    .newAuthorizationUrl()
                    .setRedirectUri(redirectUrl)
                    .setState(securityState);

    return urlBuilder.build();
  }

  @Override
  public GoogleTokenResponse getGoogleTokenResponse(String authorizationCode) throws IOException {

    final String redirectUrl = getRedirectUrl();

    return new GoogleAuthorizationCodeTokenRequest(

            httpTransport,
            jsonFactory,
            OAuth2Provider.GOOGLE_CLIENT_ID,
            OAuth2Provider.GOOGLE_CLIENT_SECRET,
            authorizationCode,
            redirectUrl)
            .execute();
  }

  @Override
  public GoogleCredential getGoogleCredential(GoogleTokenResponse tokenResponse) {
    return new GoogleCredential.Builder()

            .setJsonFactory(jsonFactory)
            .setTransport(httpTransport)
            .setClientSecrets(OAuth2Provider.GOOGLE_CLIENT_SECRET, OAuth2Provider.GOOGLE_CLIENT_ID).build()
            .setFromTokenResponse(tokenResponse);
  }

  @Override
  public Directory getGoogleDirectoryService(String email) {

    final UserTokens tokens = tokenRepository.get(email);

    final Directory directory = createDirectory(tokens);

    return directory;
  }

  @Override
  public Userinfoplus getGoogleUserInfo(GoogleCredential credentials) throws IOException {
    Oauth2 oauth2 = new Oauth2.Builder(

            httpTransport,
            jsonFactory,
            credentials)
            .build();

    final Userinfo.Get get = oauth2.userinfo().get();
    return get.execute();
  }

  @Override
  public String generateGoogleSecurityState() {
    return RandomStringUtils.random(32, true, true);
  }

  @Override
  public void setGoogleSecurityState(HttpServletRequest request, String state) {

    HttpSession httpSession = request.getSession();
    httpSession.setAttribute("oauth2-state", state);

  }

  @Override
  public boolean hasValidateGoogleSecurityState(HttpServletRequest request) {

    final String incomingState = request.getParameter("state");
    final HttpSession session = request.getSession();
    final String expectedState = (String) session.getAttribute("oauth2-state");

    return incomingState.equals(expectedState);
  }

  private String getRedirectUrl() {

    final HttpServletRequest request = requestProvider.get();
    final String scheme = request.getScheme();
    final String host = request.getHeader("Host");
    final String resultPath = scheme + "://" + host + "/oauth/callback";

    return resultPath;
  }

  private Directory createDirectory(UserTokens tokens) {

    final GoogleCredential credential = new Builder()
            .setJsonFactory(jsonFactory)
            .setTransport(httpTransport)
            .setClientSecrets(OAuth2Provider.GOOGLE_CLIENT_SECRET, OAuth2Provider.GOOGLE_CLIENT_ID).build()
            .setAccessToken(tokens.getAccessToken())
            .setRefreshToken(tokens.getRefreshToken());

    return new Directory.Builder(
            httpTransport,
            jsonFactory,
            credential)
            .build();
  }

}