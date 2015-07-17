package com.clouway.hr.core;

import java.util.Map;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface TokenRepository {

  Map<String, String> get(String userId);

  void store(String userId, String accessToken, String refreshToken);
}
