package com.clouway.hr.adapter.user.google.oauth.token;

/**
 * Created on 15-7-21.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class UserTokens {

  private final String accessToken;
  private final String refreshToken;

  public UserTokens(String accessToken, String refreshToken) {

    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
