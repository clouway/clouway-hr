package com.clouway.hr.adapter.apis.google.organization;

import com.google.inject.AbstractModule;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OrganizationModule extends AbstractModule {
  @Override
  protected void configure() {

    bind(Organization.class).to(OrganizationImpl.class);
    bind(DirectoryServiceFactory.class).to(DirectoryServiceFactoryImpl.class);

  }
}
