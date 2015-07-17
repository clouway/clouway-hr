package com.clouway.hr.persistence;

import com.clouway.hr.adapter.db.PersistentVacationRepository;
import com.clouway.hr.core.CurrentDate;
import com.clouway.hr.core.CurrentDateTime;
import com.clouway.hr.core.VacationRepository;
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
    bind(VacationRepository.class).to(PersistentVacationRepository.class);
    bind(CurrentDate.class).to(CurrentDateTime.class);
  }

  @Provides
  public ObjectDatastore getDataStore() {
    return new AnnotationObjectDatastore();
  }
}