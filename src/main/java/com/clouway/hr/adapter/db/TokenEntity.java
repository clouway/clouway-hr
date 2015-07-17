package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

class TokenEntity {

  @Key
  private String userId;
  private String accessToken;
  private String refreshToken;

  public TokenEntity() {
  }

  public TokenEntity(String userId, String accessToken, String refreshToken) {
    this.userId = userId;
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
