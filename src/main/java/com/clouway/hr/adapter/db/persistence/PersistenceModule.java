package com.clouway.hr.adapter.db.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class PersistenceModule extends AbstractModule {
  @Override
  protected void configure() {

  }

  @Provides
  public ObjectDatastore getDataStore() {
    return new AnnotationObjectDatastore();
  }
}
