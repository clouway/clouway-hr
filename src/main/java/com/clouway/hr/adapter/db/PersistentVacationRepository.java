package com.clouway.hr.adapter.db;

import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vercer.engine.persist.ObjectDatastore;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepository implements VacationRepository {

  private final Provider<ObjectDatastore> datastore;

  @Inject
  public PersistentVacationRepository(Provider<ObjectDatastore> datastore) {
    this.datastore = datastore;
  }

  @Override
  public void updateStatus(Long id, String status) {

    VacationEntity entity = datastore.get().load(VacationEntity.class, id);

    entity.changeStatus(status);

    datastore.get().update(entity);
  }

  @Override
  public void add(Long id, String status) {
    datastore.get().store(new VacationEntity(id, status));
  }
}
