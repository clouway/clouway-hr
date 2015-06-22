package com.clouway.hr.adapter.http;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationRequestDto {
  private Long userId;
  private Long fromDate;
  private Long toDate;
  private String description;

  public VacationRequestDto() {
  }

  public VacationRequestDto(Long userId, Long fromDate, Long toDate, String description) {
    this.userId = userId;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.description = description;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getFromDate() {
    return fromDate;
  }

  public Long getToDate() {
    return toDate;
  }

  public String getDescription() {
    return description;
  }
}
