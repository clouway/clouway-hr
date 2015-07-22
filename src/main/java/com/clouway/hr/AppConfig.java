package com.clouway.hr;

import com.clouway.hr.adapter.db.persistence.PersistenceModule;
import com.clouway.hr.adapter.frontend.user.UserService;
import com.clouway.hr.adapter.user.google.oauth.OAuthCredentialsFilter;
import com.clouway.hr.adapter.user.google.oauth.OAuthModule;
import com.clouway.hr.adapter.user.google.oauth.OAuthService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class AppConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new PersistenceModule(), new OAuthModule(),
            new ServletModule() {
              @Override
              protected void configureServlets() {

                filter("/").through(OAuthCredentialsFilter.class);

              }
            },

            new SitebricksModule() {
              @Override
              protected void configureSitebricks() {
                at("/oauth").serve(OAuthService.class);
                at("/userservices").serve(UserService.class);
              }
            }
    );
  }
}
