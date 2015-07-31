package com.clouway.hr;

import com.clouway.hr.adapter.apis.google.organization.OrganizationModule;
import com.clouway.hr.adapter.apis.google.user.oauth.OAuthCredentialsFilter;
import com.clouway.hr.adapter.apis.google.user.oauth.OAuthModule;
import com.clouway.hr.adapter.apis.google.user.oauth.OAuthService;
import com.clouway.hr.adapter.cache.memcache.CacheModule;
import com.clouway.hr.adapter.db.persistence.PersistenceModule;
import com.clouway.hr.adapter.frontend.employee.EmployeeService;
import com.clouway.hr.adapter.frontend.user.UserService;
import com.clouway.hr.adapter.http.VacationService;
import com.clouway.hr.vacationstate.VacationStateModule;
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
    return Guice.createInjector(new PersistenceModule(), new OAuthModule(), new OrganizationModule(), new VacationStateModule(), new CacheModule(),
            new ServletModule() {
              @Override
              protected void configureServlets() {

                filter("/spa").through(OAuthCredentialsFilter.class);

              }
            },

            new SitebricksModule() {
              @Override
              protected void configureSitebricks() {
                at("/employee").serve(EmployeeService.class);
                at("/oauth").serve(OAuthService.class);
                at("/r/vacation").serve(VacationService.class);
                at("/userservices").serve(UserService.class);
                at("/app").show(AppPage.class);
              }
            }
    );
  }
}
