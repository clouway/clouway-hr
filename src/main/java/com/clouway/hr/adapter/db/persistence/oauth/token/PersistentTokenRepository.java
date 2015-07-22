package com.clouway.hr.adapter.db.persistence.oauth.token;

import com.clouway.hr.adapter.user.google.oauth.OAuthAuthentication;
import com.clouway.hr.adapter.user.google.oauth.token.TokenRepository;
import com.clouway.hr.adapter.user.google.oauth.token.UserTokens;
import com.google.inject.Inject;
import com.vercer.engine.persist.ObjectDatastore;

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
  public UserTokens get(String userId) {

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);

    if (tokenEntity != null) {

      final String accessToken = tokenEntity.getAccessToken();
      final String refreshToken = tokenEntity.getRefreshToken();

      return new UserTokens(accessToken, refreshToken);
    }

    return null;
  }


  @Override
  public void store(String userId, UserTokens userTokens) {

    final TokenEntity tokenEntity = new TokenEntity(userId, userTokens.getAccessToken(), userTokens.getRefreshToken());

    datastore.store(tokenEntity);
  }


}
