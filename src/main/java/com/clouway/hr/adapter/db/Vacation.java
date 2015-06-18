package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class Vacation {
  @Key
  private long vacationId;
  private long dateFrom;
  private long dateTo;
  private long userId;
  private String description;
  private String status;

  public Vacation(long vacationId, long dateFrom, long dateTo, long userId, String description, String status) {
    this.vacationId = vacationId;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.userId = userId;
    this.description = description;
    this.status = status;
  }

  public Vacation() {

  }

  public long getVacationId() {
    return vacationId;
  }

  public long getDateFrom() {
    return dateFrom;
  }

  public long getDateTo() {
    return dateTo;
  }

  public long getUserId() {
    return userId;
  }

  public String getDescription() {
    return description;
  }

  public String getStatus() {
    return status;
  }
}
