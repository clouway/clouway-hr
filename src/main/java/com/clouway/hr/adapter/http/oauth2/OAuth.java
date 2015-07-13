package com.clouway.hr.adapter.http.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2.Userinfo;
import com.google.api.services.oauth2.model.Userinfoplus;

import java.io.IOException;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class OAuth {
  private final JsonFactory jsonFactory = new JacksonFactory();
  private final HttpTransport httpTransport = new NetHttpTransport();

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

  public GoogleCredential getGoogleCredential(GoogleTokenResponse tokenResponse) {
    return new GoogleCredential.Builder()

            .setJsonFactory(jsonFactory)
            .setTransport(httpTransport)
            .setClientSecrets(OAuth2Provider.GOOGLE_CLIENT_SECRET, OAuth2Provider.GOOGLE_CLIENT_ID).build()
            .setFromTokenResponse(tokenResponse);
  }

  public Directory getDirectoryService(GoogleCredential credential) throws IOException {
    return new Directory.Builder(
            httpTransport, jsonFactory, credential)
            .build();
  }

  public Userinfoplus getUserInfo(Credential credentials) throws IOException {
    Oauth2 oauth2 =
            new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credentials).build();
    final Userinfo.Get get = oauth2.userinfo().get();
    return get.execute();
  }
}
