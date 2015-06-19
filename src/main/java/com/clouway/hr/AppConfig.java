package com.clouway.hr;

import com.clouway.hr.adapter.db.PersistentCredentialRepository;
import com.clouway.hr.adapter.db.PersistentVacationRepository;
import com.clouway.hr.adapter.http.SecurityFilter;
import com.clouway.hr.adapter.http.oauth2.OAuth2Provider;
import com.clouway.hr.adapter.http.oauth2.OAuthAuthentication;
import com.clouway.hr.adapter.http.oauth2.OAuthCredentialsFilter;
import com.clouway.hr.core.CredentialRepository;
import com.clouway.hr.core.OAuthHelper;
import com.clouway.hr.core.OAuthScopes;
import com.clouway.hr.core.VacationRepository;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class AppConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
            new ServletModule() {
              @Override
              protected void configureServlets() {

                filter("/home").through(OAuthCredentialsFilter.class);
                filter("/oauth/*").through(SecurityFilter.class);
              }
            },

            new SitebricksModule() {
              @Override
              protected void configureSitebricks() {

                scan(AppConfig.class.getPackage());
              }
            },

            new AbstractModule() {
              @Override
              protected void configure() {

                bind(VacationRepository.class).to(PersistentVacationRepository.class);
                bind(CredentialRepository.class).to(PersistentCredentialRepository.class);
                bind(OAuthHelper.class).to(OAuthAuthentication.class);

              }

              @Provides
              public UserService getUserService() {
                return UserServiceFactory.getUserService();
              }

              @Provides
              public ObjectDatastore getDataStore() {
                return new AnnotationObjectDatastore();
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

                        OAuth2Provider.GOOGLE_USER_INFO_EMAIL,
                        OAuth2Provider.GOOGLE_USER_INFO_PROFILE_URL,
                        DirectoryScopes.ADMIN_DIRECTORY_USER,
                        DirectoryScopes.ADMIN_DIRECTORY_GROUP);
              }

            });
  }
}
