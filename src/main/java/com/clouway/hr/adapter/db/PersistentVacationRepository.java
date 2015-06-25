package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.VacationRequestDto;
import com.clouway.hr.adapter.http.VacationResponseDto;
import com.clouway.hr.core.IncorrectVacationStatusException;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vercer.engine.persist.ObjectDatastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

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
            .userId(vacation.getUserId())
            .description(vacation.getDescription())
            .vacationId(vacation.getUserId())
            .dateFrom(vacation.getFromDate())
            .dateTo(vacation.getToDate())
            .status("pending")
            .build();

    datastore.store(entity);
  }

  @Override
  public String getStatus(Long id) {
    VacationEntity entity = datastore.load(VacationEntity.class, id);

    return entity.getStatus();
  }

  @Override
  public List<VacationResponseDto> get(long userId) {
    Iterator<VacationEntity> vacationIterator = datastore.find(VacationEntity.class);
    List<VacationResponseDto> vacations = new ArrayList<>();

    while (vacationIterator.hasNext()) {
      VacationEntity entity = vacationIterator.next();

      VacationResponseDto responseDto = VacationResponseDto.newBuilder()
              .dateFrom(entity.getDateFrom())
              .dateTo(entity.getDateTo())
              .status(entity.getStatus())
              .build();

      vacations.add(responseDto);
    }

    return vacations;
  }

  @Override
  public List<VacationResponseDto> getStatus(String type) {
    Iterator<VacationEntity> statuses = datastore.find()
            .type(VacationEntity.class)
            .addFilter("status", EQUAL, "pending")
            .returnResultsNow();

    List<VacationResponseDto> responseDtoList = Lists.newArrayList();
    while (statuses.hasNext()) {
      VacationEntity vacationEntity = statuses.next();

      VacationResponseDto responseDto = VacationResponseDto.newBuilder()
              .status(vacationEntity.getStatus())
              .userId(vacationEntity.getUserId())
              .vacationId(vacationEntity.getVacationId())
              .dateFrom(vacationEntity.getDateFrom())
              .dateTo(vacationEntity.getDateTo())
              .description(vacationEntity.getDescription())
              .build();

      responseDtoList.add(responseDto);
    }

    return responseDtoList;
  }

  private void checkStatus(String status) {
    if (!statuses.get().getStatuses().contains(status)) {
      throw new IncorrectVacationStatusException();
    }
  }
}
