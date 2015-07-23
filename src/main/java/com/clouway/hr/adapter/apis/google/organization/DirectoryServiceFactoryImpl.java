package com.clouway.hr.adapter.apis.google.organization;

import com.clouway.hr.adapter.apis.google.user.oauth.OAuthAuthentication;
import com.clouway.hr.adapter.apis.google.user.oauth.token.TokenRepository;
import com.clouway.hr.adapter.apis.google.user.oauth.token.UserTokens;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.inject.Inject;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class DirectoryServiceFactoryImpl implements DirectoryServiceFactory {

  private final OAuthAuthentication oAuthAuthentication;
  private final TokenRepository tokenRepository;
  private final JacksonFactory jsonFactory;
  private final HttpTransport httpTransport;

  @Inject
  public DirectoryServiceFactoryImpl(OAuthAuthentication oAuthAuthentication,
                                     TokenRepository tokenRepository,
                                     JacksonFactory jsonFactory,
                                     HttpTransport httpTransport) {

    this.oAuthAuthentication = oAuthAuthentication;
    this.tokenRepository = tokenRepository;
    this.jsonFactory = jsonFactory;
    this.httpTransport = httpTransport;
  }

  @Override
  public Directory create(String email) {

    final UserTokens userTokens = tokenRepository.get(email);

    final GoogleCredential credential = oAuthAuthentication.getGoogleCredential(email, userTokens);

    return new Directory.Builder(
            httpTransport,
            jsonFactory,
            credential)
            .build();

  }

}