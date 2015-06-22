package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.VacationRequestDto;
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

  private final ObjectDatastore datastore;
  private Provider<VacationStatus> statuses;

  @Inject
  public PersistentVacationRepository(Provider<ObjectDatastore> datastore, Provider<VacationStatus> statuses) {
    this.datastore = datastore.get();
    this.statuses = statuses;
  }

  @Override
  public void updateStatus(Long vacationId, String status) {
    checkStatus(status);

    VacationEntity entity = datastore.load(VacationEntity.class, vacationId);
    entity = VacationEntity.newBuilder()
            .vacationId(entity.getVacationId())
            .status(status)
            .build();

    datastore.store(entity);
  }

  @Override
  public void add(VacationRequestDto vacation) {
    VacationEntity entity = VacationEntity.newBuilder()
            .vacationId(vacation.getUserId())
            .dateFrom(vacation.getFromDate())
            .dateTo(vacation.getToDate())
            .status("pending")
            .description(vacation.getDescription())
            .build();

    datastore.store(entity);
  }

  @Override
  public String getStatus(Long id) {
    VacationEntity entity = datastore.load(VacationEntity.class, id);

    return entity.getStatus();
  }

  private void checkStatus(String status) {
    if (!statuses.get().getStatuses().contains(status)) {
      throw new IncorrectVacationStatusException();
    }
  }
}
