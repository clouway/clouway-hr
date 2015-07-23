package com.clouway.hr.adapter.apis.google.user.oauth.token;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;

import java.io.IOException;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */


public class TokenRefreshListener implements CredentialRefreshListener {

  private final TokenRepository tokenRepository;
  private final UserService userService;

  @Inject
  public TokenRefreshListener(TokenRepository tokenRepository, UserService userService) {
    this.tokenRepository = tokenRepository;
    this.userService = userService;
  }

  @Override
  public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
    String newAccessToken = tokenResponse.getAccessToken();

    tokenRepository.storeNewAccessToken(userService.getCurrentUser().getEmail(), newAccessToken);
  }

  @Override
  public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {

  }

}