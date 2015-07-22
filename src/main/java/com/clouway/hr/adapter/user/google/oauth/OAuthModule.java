package com.clouway.hr.adapter.user.google.oauth;

import com.clouway.hr.adapter.db.persistence.oauth.token.PersistentTokenRepository;
import com.clouway.hr.adapter.user.google.oauth.token.TokenRepository;
import com.clouway.hr.core.user.CurrentUser;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthModule extends AbstractModule {
  @Override
  protected void configure() {

    bind(TokenRepository.class).to(PersistentTokenRepository.class);
    bind(OAuthAuthentication.class).to(OAuthAuthenticationImpl.class);
    bind(OAuthUser.class).to(OAuthUserImpl.class);

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

  @Provides
  @RequestScoped
  public CurrentUser getCurrentUser(OAuthUser oAuthUser) {

    final String email = oAuthUser.getEmail();
    final Set<String> roles = oAuthUser.getRoles();
    final CurrentUser currentUser = new CurrentUser(email, roles.contains("OWNER") || roles.contains("MANAGER"));

    return currentUser;
  }

}
