package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.VacationRequestDto;
import com.clouway.hr.adapter.http.VacationResponseDto;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.VacationRepository;
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
  private Provider<Status> statuses;

  @Inject
  public PersistentVacationRepository(Provider<ObjectDatastore> datastore, Provider<Status> statuses) {
    this.datastore = datastore.get();
    this.statuses = statuses;
  }

  @Override
  public void updateStatus(Long vacationId, String status) {
    VacationEntity entity = datastore.load(VacationEntity.class, vacationId);
    VacationEntity newVacation = VacationEntity.newBuilder()
            .vacationId(vacationId)
            .userId(entity.getUserId())
            .description(entity.getDescription())
            .dateFrom(entity.getDateFrom())
            .dateTo(entity.getDateFrom())
            .status(status)
            .build();

    datastore.store(newVacation);
  }

  @Override
  public void add(VacationRequestDto vacation) {
    VacationEntity entity = VacationEntity.newBuilder()
            .userId(vacation.getUserId())
            .description(vacation.getDescription())
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
  public List<VacationResponseDto> get(String userId) {
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
              .vacationId(vacationEntity.getId())
              .dateFrom(vacationEntity.getDateFrom())
              .dateTo(vacationEntity.getDateTo())
              .description(vacationEntity.getDescription())
              .build();

      responseDtoList.add(responseDto);
    }

    return responseDtoList;
  }
}
