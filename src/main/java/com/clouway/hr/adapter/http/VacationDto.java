package com.clouway.hr.adapter.http;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class VacationDto {
  private long vacationId;
  private String status;

  public VacationDto(long vacationId, String status) {
    this.vacationId = vacationId;
    this.status = status;
  }

  public VacationDto() {
  }

  public long getVacationId() {
    return vacationId;
  }

  public String getStatus() {
    return status;
  }
}
