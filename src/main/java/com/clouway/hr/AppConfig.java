package com.clouway.hr;

import com.clouway.hr.adapter.db.PersistentVacationRepository;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class AppConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
            new SitebricksModule() {
              @Override
              protected void configureSitebricks() {
                scan(AppConfig.class.getPackage());
              }
            },
            new ServletModule() {
              @Override
              protected void configureServlets() {
              }
            },
            new AbstractModule() {
              @Override
              protected void configure() {
                bind(VacationRepository.class).to(PersistentVacationRepository.class);
              }

              @Provides
              public ObjectDatastore getDataStore() {
                return new AnnotationObjectDatastore();
              }

              @Provides
              public VacationStatus getVacationStatuses() {
                VacationStatus statuses = new VacationStatus();
                statuses.add("pending");
                statuses.add("accept");
                statuses.add("reject");

                return statuses;
              }
            });
  }
}
