package com.clouway.hr.adapter.db;

import com.clouway.hr.core.CredentialRepository;
import com.clouway.hr.core.OAuthAuthentication;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.inject.Inject;
import com.vercer.engine.persist.ObjectDatastore;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class PersistentCredentialRepository implements CredentialRepository {

  private final ObjectDatastore datastore;
  private final OAuthAuthentication oAuthAuthentication;


  @Inject
  public PersistentCredentialRepository(ObjectDatastore datastore, OAuthAuthentication oAuthAuthentication) {

    this.datastore = datastore;
    this.oAuthAuthentication = oAuthAuthentication;
  }


  @Override
  public GoogleCredential get(String userId) {

    final CredentialEntity credentialEntity = datastore.load(CredentialEntity.class, userId);

    if (credentialEntity != null) {

      final String accessToken = credentialEntity.getAccessToken();
      final String refreshToken = credentialEntity.getRefreshToken();

      return oAuthAuthentication.getGoogleCredential(accessToken, refreshToken);
    }

    return null;
  }


  @Override
  public void store(String userId, GoogleCredential googleCredential) {

    final String accessToken = googleCredential.getAccessToken();
    final String refreshToken = googleCredential.getRefreshToken();

    CredentialEntity credentialEntity = new CredentialEntity(userId, accessToken, refreshToken);

    datastore.store(credentialEntity);
  }


}
