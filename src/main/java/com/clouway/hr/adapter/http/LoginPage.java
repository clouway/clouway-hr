package com.clouway.hr.adapter.http;

import com.clouway.hr.adapter.http.oauth2.OAuth2Provider;
import com.clouway.hr.core.RandomStringGenerator;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15-6-19.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@At("/login")
@Show("oauth-login.html")
public class LoginPage {

  public String message;
  public String oauthURL;
  private final List<String> SCOPES = Arrays.asList(
          DirectoryScopes.ADMIN_DIRECTORY_USER,
          DirectoryScopes.ADMIN_DIRECTORY_GROUP,
          OAuth2Provider.GOOGLE_USER_INFO_PLUS_URL,
          OAuth2Provider.GOOGLE_USER_INFO_PLUS_URL2
          );

  private final Provider<HttpServletRequest> requestProvider;
  private final RandomStringGenerator oauthStateGenerator;
  private final HttpTransport httpTransport = new NetHttpTransport();
  private final JacksonFactory jsonFactory = new JacksonFactory();

  @Inject
  public LoginPage(Provider<HttpServletRequest> requestProvider,
                   RandomStringGenerator oauthStateGenerator) {

    this.requestProvider = requestProvider;
    this.oauthStateGenerator = oauthStateGenerator;
  }

  @Get
  public void createAuthenticationLink() throws URISyntaxException, IOException {

    HttpServletRequest request = requestProvider.get();
    String state = oauthStateGenerator.random();
    HttpSession httpSession = request.getSession();
    httpSession.setAttribute("oauth2-state", state);

    oauthURL = getAuthorizationUrl("user_id", state);
  }

  private String getAuthorizationUrl(String userId, String state) throws IOException {
    GoogleAuthorizationCodeRequestUrl urlBuilder =
            getFlow().newAuthorizationUrl().setRedirectUri(OAuth2Provider.REDIRECT_URI).setState(state);
    urlBuilder.set("user_id", userId);
    return urlBuilder.build();
  }

  private GoogleAuthorizationCodeFlow getFlow() throws IOException {
    return new GoogleAuthorizationCodeFlow.Builder(
            httpTransport,
            jsonFactory,
            OAuth2Provider.GOOGLE_CLIENT_ID,
            OAuth2Provider.GOOGLE_CLIENT_SECRET,
            SCOPES)
            .setAccessType("offline")
            .setApprovalPrompt("force").build();
  }
}

