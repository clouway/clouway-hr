package com.clouway.hr.adapter.db.persistence.oauth.token;

import com.clouway.hr.adapter.apis.google.user.oauth.token.TokenRepository;
import com.clouway.hr.adapter.apis.google.user.oauth.token.UserTokens;
import com.clouway.hr.core.cache.Cache;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.vercer.engine.persist.ObjectDatastore;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class PersistentTokenRepository implements TokenRepository {

  private final ObjectDatastore datastore;
  private final Cache cache;


  @Inject
  public PersistentTokenRepository(ObjectDatastore datastore, Cache cache) {

    this.datastore = datastore;
    this.cache = cache;
  }


  @Override
  public UserTokens get(String userId) {

    if (cache.contains(userId)) {

      final Optional<UserTokens> optional = cache.get(userId);

      return optional.get();
    }

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);

    if (tokenEntity != null) {

      final String accessToken = tokenEntity.getAccessToken();
      final String refreshToken = tokenEntity.getRefreshToken();

      cache.put(userId, new UserTokens(accessToken, refreshToken));

      return new UserTokens(accessToken, refreshToken);
    }

    return null;
  }


  @Override
  public void store(String userId, UserTokens userTokens) {

    final TokenEntity tokenEntity = new TokenEntity(userId, userTokens.getAccessToken(), userTokens.getRefreshToken());

    cache.put(userId, userTokens);
    datastore.store(tokenEntity);
  }

  @Override
  public void storeNewAccessToken(String userId, String newAccessToken) {

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);
    final String refreshToken = tokenEntity.getRefreshToken();
    final TokenEntity newTokenEntity = new TokenEntity(userId, newAccessToken, refreshToken);

    datastore.store(newTokenEntity);
    cache.put(userId, new UserTokens(newAccessToken, refreshToken));

  }

  @Override
  public boolean containsTokens(String userId) {

    if (cache.contains(userId)) {
      return true;
    }

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);

    return tokenEntity != null;
  }


}
