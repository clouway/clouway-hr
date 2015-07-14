package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.VacationRequestDto;
import com.clouway.hr.adapter.http.VacationResponseDto;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vercer.engine.persist.FindCommand;
import com.vercer.engine.persist.FindCommand.RootFindCommand;
import com.vercer.engine.persist.ObjectDatastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static com.google.appengine.api.datastore.Query.FilterOperator.IN;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepository implements VacationRepository {
  private final ObjectDatastore datastore;
  private final Status statuses;

  private class CriteriaSearch {
    private final String field;
    private final FilterOperator operator;
    private final Object value;

    public CriteriaSearch(String field, FilterOperator operator, Object value) {
      this.field = field;
      this.operator = operator;
      this.value = value;
    }
  }

  @Inject
  public PersistentVacationRepository(Provider<ObjectDatastore> datastore, Provider<Status> statuses) {
    this.datastore = datastore.get();
    this.statuses = statuses.get();
  }

  @Override
  public void updateStatus(Long vacationId, String status) {
    VacationEntity entity = datastore.load(VacationEntity.class, vacationId);
    VacationEntity newVacation = newVacationEntity(entity, status);

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
    Iterator vacationIterator = findWith(new CriteriaSearch("userId", EQUAL, userId));

    return iterateVacations(vacationIterator);
  }

  @Override
  public List<VacationResponseDto> getStatus(String vacationType) {
    Iterator<VacationEntity> statuses = findWith(new CriteriaSearch("status", EQUAL, vacationType));

    return iterateVacations(statuses);
  }

  @Override
  public List<VacationResponseDto> getHistory(User currentUser) {
    RootFindCommand<VacationEntity> filter = filterUser(currentUser);
    sortUser(filter);

    return getHistory(filter);
  }

  @Override
  public void hide(Long vacationId) {
    VacationEntity vacationEntity = datastore.load(VacationEntity.class, vacationId);
    VacationEntity newVacationEntity = VacationEntity.newBuilder()
            .vacationId(vacationEntity.getId())
            .userId(vacationEntity.getUserId())
            .dateFrom(vacationEntity.getDateFrom())
            .dateTo(vacationEntity.getDateTo())
            .description(vacationEntity.getDescription())
            .status(vacationEntity.getStatus())
            .isHidden(true)
            .build();

    datastore.store(newVacationEntity);
  }

  @Override
  public List<VacationResponseDto> getUnHidden(User currentUser) {
    Iterator results = findWith(new CriteriaSearch("isHidden", EQUAL, false), new CriteriaSearch("userId", EQUAL, currentUser.getEmail()));

    return iterateVacations(results);
  }

  private List<VacationResponseDto> iterateVacations(Iterator<VacationEntity> vacationIterator) {
    List<VacationResponseDto> vacations = Lists.newArrayList();
    while (vacationIterator.hasNext()) {
      VacationEntity entity = vacationIterator.next();
      vacations.add(newResponseVacationDto(entity));
    }

    return vacations;
  }

  private void sortUser(RootFindCommand<VacationEntity> filter) {
    filter.addSort("dateTo", SortDirection.DESCENDING);
  }

  private Iterator<VacationEntity> findWith(CriteriaSearch... criteriaSearches) {
    FindCommand queryResultIteratorFuture = datastore.find();
    RootFindCommand<VacationEntity> type = queryResultIteratorFuture.type(VacationEntity.class);

    for (CriteriaSearch criteriaSearch : criteriaSearches) {
      type.addFilter(criteriaSearch.field, criteriaSearch.operator, criteriaSearch.value);
    }
    QueryResultIterator<VacationEntity> vacationEntityQueryResultIterator = type.returnResultsNow();

    return vacationEntityQueryResultIterator;
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
    QueryResultIterator<VacationEntity> vacationEntityIterator = rootFindCommand.returnResultsNow();

    return iterateVacations(vacationEntityIterator);
  }

  private VacationResponseDto newResponseVacationDto(VacationEntity vacationEntity) {
    return VacationResponseDto.newBuilder()
            .userId(vacationEntity.getUserId())
            .vacationId(vacationEntity.getId())
            .description(vacationEntity.getDescription())
            .dateFrom(vacationEntity.getDateFrom())
            .dateTo(vacationEntity.getDateTo())
            .status(vacationEntity.getStatus())
            .isHidden(vacationEntity.isHidden())
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

  private VacationEntity newVacationEntity(VacationEntity entity, String status) {
    return VacationEntity.newBuilder()
            .vacationId(entity.getId())
            .userId(entity.getUserId())
            .description(entity.getDescription())
            .dateFrom(entity.getDateFrom())
            .dateTo(entity.getDateTo())
            .status(status)
            .build();
  }

  private RootFindCommand<VacationEntity> getFinder() {
    return datastore.find()
            .type(VacationEntity.class);
  }
}