package com.clouway.hr.adapter.http;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class VacationDto {
  private long vacationId;
  private String username;
  private String teamName;
  private String dateFrom;
  private String dateTo;
  private String status;

  public VacationDto(long vacationId, String username, String teamName, String dateFrom, String dateTo, String status) {
    this.vacationId = vacationId;
    this.username = username;
    this.teamName = teamName;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.status = status;
  }

  public VacationDto() {
  }

  public long getVacationId() {
    return vacationId;
  }

  public String getUsername() {
    return username;
  }

  public String getTeamName() {
    return teamName;
  }

  public String getDateFrom() {
    return dateFrom;
  }

  public String getDateTo() {
    return dateTo;
  }

  public String getStatus() {
    return status;
  }
}
