package com.clouway.hr.adapter.apis.google.user.oauth;

import com.clouway.hr.adapter.apis.google.user.oauth.token.UserTokens;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface OAuthAuthentication {

  GoogleAuthorizationCodeFlow getGoogleAuthorizationFlow();

  String getAuthorizationUrl(GoogleAuthorizationCodeFlow flow, String securityState);

  GoogleTokenResponse getGoogleTokenResponse(String authorizationCode) throws IOException;

  GoogleCredential getGoogleCredential(GoogleTokenResponse tokenResponse);

  GoogleCredential getGoogleCredential(String email, UserTokens userTokens);

  String generateGoogleSecurityState();

  void setGoogleSecurityState(HttpServletRequest request, String state);

  boolean hasValidateGoogleSecurityState(HttpServletRequest request);

}