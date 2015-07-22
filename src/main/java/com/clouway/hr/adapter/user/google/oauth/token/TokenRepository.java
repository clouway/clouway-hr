package com.clouway.hr.adapter.user.google.oauth.token;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface TokenRepository {

  UserTokens get(String userId);

  void store(String userId, UserTokens tokens);
}
