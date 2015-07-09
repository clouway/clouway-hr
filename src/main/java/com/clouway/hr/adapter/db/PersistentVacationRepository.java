package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.VacationRequestDto;
import com.clouway.hr.adapter.http.VacationResponseDto;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vercer.engine.persist.FindCommand.RootFindCommand;
import com.vercer.engine.persist.ObjectDatastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static com.google.appengine.api.datastore.Query.FilterOperator.IN;
import static com.google.appengine.api.datastore.Query.FilterOperator.NOT_EQUAL;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepository implements VacationRepository {
  private final ObjectDatastore datastore;
  private final Status statuses;

  @Inject
  public PersistentVacationRepository(Provider<ObjectDatastore> datastore, Provider<Status> statuses) {
    this.datastore = datastore.get();
    this.statuses = statuses.get();
  }

  @Override
  public void updateStatus(Long vacationId, String status) {
    VacationEntity entity = datastore.load(VacationEntity.class, vacationId);
    VacationEntity newVacation = newVacationEntity(vacationId, entity.getUserId(), entity.getDescription(), entity.getDateFrom(), entity.getDateTo(), status);

    datastore.store(newVacation);
  }

  @Override
  public void add(VacationRequestDto vacation) {
    datastore.store(newVacationEntity(vacation));
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

      vacations.add(newResponseVacationDto(entity));
    }

    return vacations;
  }

  @Override
  public List<VacationResponseDto> getStatus(String type) {
    Iterator<VacationEntity> statuses = datastore.find()
            .type(VacationEntity.class)
            .addFilter("status", EQUAL, type)
            .returnResultsNow();

    List<VacationResponseDto> responseDtoList = Lists.newArrayList();
    while (statuses.hasNext()) {
      VacationEntity vacationEntity = statuses.next();

      responseDtoList.add(newResponseVacationDto(vacationEntity));
    }

    return responseDtoList;
  }

  @Override
  public List<VacationResponseDto> getHistory(User currentUser) {
    RootFindCommand<VacationEntity> filter = filterUser(currentUser);
    sortUser(filter);

    return getHistory(filter);
  }

  private void sortUser(RootFindCommand<VacationEntity> filter) {
    filter.addSort("dateTo", SortDirection.DESCENDING);
  }

  private RootFindCommand<VacationEntity> filterUser(User currentUser) {
    RootFindCommand<VacationEntity> filter = getFinder();
    boolean isAdmin = currentUser.isAdmin();
    if (isAdmin) {
      ArrayList<String> statusList = Lists.newArrayList();
      statusList.add(statuses.getAccept());
      statusList.add(statuses.getReject());

      filter.addFilter("status", IN, statusList);
    }
    if (!isAdmin) {
      filter.addFilter("userId", EQUAL, currentUser.getEmail());
    }

    return filter;
  }

  private List<VacationResponseDto> getHistory(RootFindCommand<VacationEntity> rootFindCommand) {
    List<VacationResponseDto> responseDtoList = Lists.newArrayList();

    QueryResultIterator<VacationEntity> vacationEntityIterator = rootFindCommand.returnResultsNow();
    while (vacationEntityIterator.hasNext()) {
      VacationEntity vacationEntity = vacationEntityIterator.next();

      responseDtoList.add(newResponseVacationDto(vacationEntity));
    }

    return responseDtoList;
  }

  private VacationResponseDto newResponseVacationDto(VacationEntity vacationEntity) {
    return VacationResponseDto.newBuilder()
            .userId(vacationEntity.getUserId())
            .vacationId(vacationEntity.getId())
            .description(vacationEntity.getDescription())
            .dateFrom(vacationEntity.getDateFrom())
            .dateTo(vacationEntity.getDateTo())
            .status(vacationEntity.getStatus())
            .build();
  }

  private VacationEntity newVacationEntity(VacationRequestDto vacation) {
    return VacationEntity.newBuilder()
            .userId(vacation.getUserId())
            .description(vacation.getDescription())
            .dateFrom(vacation.getFromDate())
            .dateTo(vacation.getToDate())
            .status(statuses.getPending())
            .build();
  }

  private VacationEntity newVacationEntity(Long vacationId, String userId, String description, Long dateFrom, Long dateTo, String status) {
    return VacationEntity.newBuilder()
            .vacationId(vacationId)
            .userId(userId)
            .description(description)
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .status(status)
            .build();
  }

  private RootFindCommand<VacationEntity> getFinder() {
    return datastore.find()
            .type(VacationEntity.class);
  }
}