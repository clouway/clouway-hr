package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.OAuthHelper;
import com.clouway.hr.core.OAuthScopes;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2.Userinfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.inject.Inject;
import org.apache.commons.lang.RandomStringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthAuthentication implements OAuthHelper {

  private final List<String> scopes;
  private final JacksonFactory jsonFactory;
  private final HttpTransport httpTransport;


  @Inject
  public OAuthAuthentication(@OAuthScopes List<String> scopes, JacksonFactory jsonFactory, HttpTransport httpTransport) {

    this.scopes = scopes;
    this.jsonFactory = jsonFactory;
    this.httpTransport = httpTransport;
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
    final GoogleAuthorizationCodeRequestUrl urlBuilder =
            googleAuthorizationCodeFlow

                    .newAuthorizationUrl()
                    .setRedirectUri(OAuth2Provider.REDIRECT_URI)
                    .setState(securityState);

    return urlBuilder.build();
  }

  @Override
  public GoogleTokenResponse getGoogleTokenResponse(String authorizationCode) throws IOException {
    return new GoogleAuthorizationCodeTokenRequest(

            httpTransport,
            jsonFactory,
            OAuth2Provider.GOOGLE_CLIENT_ID,
            OAuth2Provider.GOOGLE_CLIENT_SECRET,
            authorizationCode,
            OAuth2Provider.REDIRECT_URI)
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

}
