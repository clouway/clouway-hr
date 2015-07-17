package com.clouway.hr.adapter.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

/**
 * Created on 15-7-14.
 *
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
