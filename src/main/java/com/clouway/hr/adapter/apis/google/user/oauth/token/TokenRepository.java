package com.clouway.hr.adapter.apis.google.user.oauth.token;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface TokenRepository {

  UserTokens get(String userId);

  void store(String userId, UserTokens tokens);

  void storeNewAccessToken(String userId, String accessToken);

  boolean containsTokens(String userId);
}
