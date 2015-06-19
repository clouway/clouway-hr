package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
class VacationEntity {

  @Key
  private Long vacationId;
  private String status;

  public VacationEntity(Long vacationId, String status) {
    this.vacationId = vacationId;
    this.status = status;
  }

  public VacationEntity() {
  }

  public String getStatus() {
    return this.status;
  }

  public Long getVacationId() {
    return vacationId;
  }
}
