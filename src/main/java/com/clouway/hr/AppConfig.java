package com.clouway.hr;

import com.google.inject.AbstractModule;
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


              }
            });
  }
}
