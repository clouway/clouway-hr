package com.clouway.hr.adapter.db;

import com.clouway.hr.core.OAuthAuthentication;
import com.clouway.hr.core.TokenRepository;
import com.google.inject.Inject;
import com.vercer.engine.persist.ObjectDatastore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class PersistentTokenRepository implements TokenRepository {

  private final ObjectDatastore datastore;
  private final OAuthAuthentication oAuthAuthentication;


  @Inject
  public PersistentTokenRepository(ObjectDatastore datastore, OAuthAuthentication oAuthAuthentication) {

    this.datastore = datastore;
    this.oAuthAuthentication = oAuthAuthentication;
  }


  @Override
  public Map<String, String> get(String userId) {

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);

    if (tokenEntity != null) {

      final String accessToken = tokenEntity.getAccessToken();
      final String refreshToken = tokenEntity.getRefreshToken();

      return new HashMap<String, String>() {{
        put("accessToken", accessToken);
        put("refreshToken", refreshToken);
      }};
    }

    return null;
  }


  @Override
  public void store(String userId, String accessToken, String refreshToken) {

    final TokenEntity tokenEntity = new TokenEntity(userId, accessToken, refreshToken);

    datastore.store(tokenEntity);
  }


}
