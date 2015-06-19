package com.clouway.hr.core;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface CredentialRepository {

  GoogleCredential get(String userId);

  void store(String userId, GoogleCredential googleCredential);
}
