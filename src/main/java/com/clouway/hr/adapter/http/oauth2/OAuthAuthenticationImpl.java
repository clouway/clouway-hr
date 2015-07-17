package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.OAuthAuthentication;
import com.clouway.hr.core.TokenRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
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
import java.util.Map;

/**
 * Created on 15-7-9.
 *
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
  public GoogleCredential getGoogleCredential(String accessToken, String refreshToken) {
    return new GoogleCredential.Builder()

            .setJsonFactory(jsonFactory)
            .setTransport(httpTransport)
            .setClientSecrets(OAuth2Provider.GOOGLE_CLIENT_SECRET, OAuth2Provider.GOOGLE_CLIENT_ID).build()
            .setAccessToken(accessToken)
            .setRefreshToken(refreshToken);
  }

  @Override
  public Directory getGoogleDirectoryService(GoogleCredential credential) {
    return new Directory.Builder(
            httpTransport,
            jsonFactory,
            credential)
            .build();
  }

  @Override
  public Directory getGoogleDirectoryService(String email) {

    final Map<String, String> tokens = tokenRepository.get(email);
    final String accessToken = tokens.get("accessToken");
    final String refreshToken = tokens.get("refreshToken");
    final GoogleCredential googleCredential = getGoogleCredential(accessToken, refreshToken);

    return getGoogleDirectoryService(googleCredential);
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

  static void requestNewAccessToken(String refreshToken) throws IOException {

    GoogleRefreshTokenRequest refreshTokenRequest = new GoogleRefreshTokenRequest(
            new NetHttpTransport(),
            new JacksonFactory(),
            refreshToken,
            OAuth2Provider.GOOGLE_CLIENT_ID,
            OAuth2Provider.GOOGLE_CLIENT_SECRET);

    final GoogleTokenResponse googleTokenResponse = refreshTokenRequest.execute();

    System.out.println(googleTokenResponse.getAccessToken());
    System.out.println(googleTokenResponse.getRefreshToken());

//      RefreshAccessTokenRequest request = new RefreshAccessTokenRequest();
//      request.clientId = CLIENT_ID;
//      request.clientSecret = CLIENT_SECRET;
//      request.refreshToken = refreshToken;
//      AccessTokenResponse response =
//              request.execute().parseAs(AccessTokenResponse.class);
//      System.out.println("Access token: " + response.accessToken);
//    } catch (HttpResponseException e) {
//      AccessTokenErrorResponse response =
//              e.response.parseAs(AccessTokenErrorResponse.class);
//      System.out.println("Error: " + response.error);
  }


  private String getRedirectUrl() {

    final HttpServletRequest request = requestProvider.get();
    final String scheme = request.getScheme();
    final String host = request.getHeader("Host");
    final String resultPath = scheme + "://" + host + "/oauth/callback";

    return resultPath;
  }

}
