package com.clouway.hr.adapter.db;

import com.clouway.hr.core.IncorrectVacationStatusException;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vercer.engine.persist.ObjectDatastore;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepository implements VacationRepository {

  private final Provider<ObjectDatastore> datastore;
  private Provider<VacationStatus> statuses;

  @Inject
  public PersistentVacationRepository(Provider<ObjectDatastore> datastore, Provider<VacationStatus> statuses) {
    this.datastore = datastore;
    this.statuses = statuses;
  }

  @Override
  public void updateStatus(Long id, String status) {
    if (!statuses.get().getStatuses().contains(status)) {
      throw new IncorrectVacationStatusException();
    }
    VacationEntity entity = datastore.get().load(VacationEntity.class, id);
    VacationEntity vacationEntity = new VacationEntity(entity.getVacationId(), status);

    datastore.get().store(vacationEntity);
  }

  @Override
  public void add(Long id, String status) {
    datastore.get().store(new VacationEntity(id, status));
  }

  @Override
  public String getStatus(Long id) {
    VacationEntity entity = datastore.get().load(VacationEntity.class, id);

    return entity.getStatus();
  }
}
