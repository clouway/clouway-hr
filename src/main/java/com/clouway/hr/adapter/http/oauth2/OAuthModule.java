package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.adapter.db.PersistentTokenRepository;
import com.clouway.hr.core.OAuthAuthentication;
import com.clouway.hr.core.OAuthScopes;
import com.clouway.hr.core.TokenRepository;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15-7-15.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthModule extends AbstractModule {
  @Override
  protected void configure() {

    bind(TokenRepository.class).to(PersistentTokenRepository.class);
    bind(OAuthAuthentication.class).to(OAuthAuthenticationImpl.class);

  }

  @Provides
  public UserService getUserService() {
    return UserServiceFactory.getUserService();
  }

  @Provides
  public HttpTransport getHttpTransport() throws GeneralSecurityException, IOException {
    return new NetHttpTransport();
  }

  @Provides
  public JacksonFactory getJsonFactory() throws GeneralSecurityException, IOException {
    return new JacksonFactory();
  }

  @Provides
  @Singleton
  @OAuthScopes
  public List<String> getScopes() {
    return Arrays.asList(

            DirectoryScopes.ADMIN_DIRECTORY_USER,
            DirectoryScopes.ADMIN_DIRECTORY_GROUP);
  }

}
