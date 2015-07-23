package com.clouway.hr.adapter.apis.google.user.oauth.token;

import java.io.Serializable;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class UserTokens implements Serializable {

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
