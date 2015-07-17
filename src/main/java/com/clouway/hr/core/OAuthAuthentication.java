package com.clouway.hr.core;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.oauth2.model.Userinfoplus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface OAuthAuthentication {

  GoogleAuthorizationCodeFlow getGoogleAuthorizationFlow();

  String getAuthorizationUrl(GoogleAuthorizationCodeFlow flow, String securityState);

  GoogleTokenResponse getGoogleTokenResponse(String authorizationCode) throws IOException;

  GoogleCredential getGoogleCredential(GoogleTokenResponse tokenResponse);

  GoogleCredential getGoogleCredential(String accessToken, String refreshToken);

  Directory getGoogleDirectoryService(GoogleCredential credential);

  Userinfoplus getGoogleUserInfo(GoogleCredential credential) throws IOException;

  String generateGoogleSecurityState();

  void setGoogleSecurityState(HttpServletRequest request, String state);

  boolean hasValidateGoogleSecurityState(HttpServletRequest request);

}
