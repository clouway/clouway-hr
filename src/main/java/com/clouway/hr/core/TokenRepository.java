package com.clouway.hr.core;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface TokenRepository {

  UserTokens get(String userId);

  void store(String userId, UserTokens tokens);
}
