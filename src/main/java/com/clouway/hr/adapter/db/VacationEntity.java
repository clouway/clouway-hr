package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
class VacationEntity {
  @Key
  public Long id;

  public String status;
  public Long dateFrom;
  public Long dateTo;
  public String userId;
  public String description;
  public boolean isHidden;

  public Long getId() {
    return id;
  }

  public String getStatus() {
    return status;
  }

  public Long getDateFrom() {
    return dateFrom;
  }

  public Long getDateTo() {
    return dateTo;
  }

  public String getUserId() {
    return userId;
  }

  public String getDescription() {
    return description;
  }

  public boolean isHidden() {
    return isHidden;
  }
}