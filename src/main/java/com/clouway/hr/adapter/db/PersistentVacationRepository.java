package com.clouway.hr.adapter.db;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */

import com.clouway.hr.core.Vacation;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.user.User;
import com.clouway.hr.core.vacationstate.ApproveVacationStatus;
import com.clouway.hr.core.vacationstate.RejectVacationStatus;
import com.clouway.hr.core.vacationstate.State;
import com.clouway.hr.core.vacationstate.VacationStatus;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.vercer.engine.persist.FindCommand;
import com.vercer.engine.persist.ObjectDatastore;

import java.util.Iterator;
import java.util.List;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

public class PersistentVacationRepository implements VacationRepository {
  private final ObjectDatastore datastore;

  private State state;

  @Inject
  public PersistentVacationRepository(ObjectDatastore datastore, @Named("vacationState") State state) {
    this.datastore = datastore;
    this.state = state;
  }

  @Override
  public void updateStatus(Long vacationId, String status) {
    VacationEntity entity = datastore.load(VacationEntity.class, vacationId);
    entity.status = status;

    datastore.update(entity);
  }

  @Override
  public void add(Vacation vacation) {
    datastore.store(newVacationEntity(vacation));
  }

  @Override
  public List<Vacation> get(String userId) {
    FindCommand queryResultIteratorFuture = datastore.find();
    QueryResultIterator<VacationEntity> ids = queryResultIteratorFuture.type(VacationEntity.class)
            .addFilter("userId", EQUAL, userId)
            .returnResultsNow();

    return iterateVacations(ids);
  }

  @Override
  public List<Vacation> getStatus(String vacationType) {
    FindCommand queryResultIteratorFuture = datastore.find();
    QueryResultIterator<VacationEntity> statuses = queryResultIteratorFuture.type(VacationEntity.class)
            .addFilter("status", EQUAL, vacationType)
            .returnResultsNow();

    return iterateVacations(statuses);
  }

  @Override
  public void hide(Long vacationId) {
    VacationEntity vacationEntity = datastore.load(VacationEntity.class, vacationId);
    vacationEntity.isHidden = true;

    datastore.update(vacationEntity);
  }

  @Override
  public List<Vacation> getUnHidden(User currentUser) {
    FindCommand queryResultIteratorFuture = datastore.find();
    Iterator results = queryResultIteratorFuture.type(VacationEntity.class)
            .addFilter("isHidden", EQUAL, false)
            .addFilter("userId", EQUAL, currentUser.getEmail())
            .returnResultsNow();

    return iterateVacations(results);
  }

  @Override
  public void approve(VacationStatus vacationStatus) {
    state = new ApproveVacationStatus();
    vacationStatus.setState(state);
    VacationEntity entity = datastore.load(VacationEntity.class, vacationStatus.id);

    entity.status = vacationStatus.getState().asString();

    datastore.update(entity);
  }

  @Override
  public void reject(VacationStatus vacationStatus) {
    state = new RejectVacationStatus();
    vacationStatus.setState(state);
    VacationEntity entity = datastore.load(VacationEntity.class, vacationStatus.id);

    entity.status = vacationStatus.getState().asString();

    datastore.update(entity);
  }

  private List<Vacation> iterateVacations(Iterator<VacationEntity> vacationIterator) {
    List<Vacation> vacations = Lists.newArrayList();
    while (vacationIterator.hasNext()) {
      VacationEntity entity = vacationIterator.next();
      vacations.add(newVacation(entity));
    }

    return vacations;
  }

  private Vacation newVacation(VacationEntity vacationEntity) {
    return Vacation.newBuilder()
            .userId(vacationEntity.userId)
            .vacationId(vacationEntity.id)
            .description(vacationEntity.description)
            .dateFrom(vacationEntity.dateFrom)
            .dateTo(vacationEntity.dateTo)
            .status(vacationEntity.status)
            .isHidden(vacationEntity.isHidden)
            .build();
  }

  private VacationEntity newVacationEntity(Vacation vacation) {
    VacationEntity vacationEntity = new VacationEntity();
    vacationEntity.userId = vacation.getUserId();
    vacationEntity.description = vacation.getDescription();
    vacationEntity.dateFrom = vacation.getDateFrom();
    vacationEntity.dateTo = vacation.getDateTo();
    vacationEntity.status = "pending";

    return vacationEntity;
  }
}