package com.clouway.hr.core;

import com.clouway.hr.adapter.http.EmployeeVacationRequestDto;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class Vacation {
  private Long vacationId;
  private String status;
  private Long dateFrom;
  private Long dateTo;
  private boolean isHidden;
  private String userId;
  private String description;

  private Vacation(Builder builder) {
    vacationId = builder.vacationId;
    status = builder.status;
    dateFrom = builder.dateFrom;
    dateTo = builder.dateTo;
    isHidden = builder.isHidden;
    userId = builder.userId;
    description = builder.description;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Long getVacationId() {
    return vacationId;
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

  public boolean isHidden() {
    return isHidden;
  }

  public String getUserId() {
    return userId;
  }

  public String getDescription() {
    return description;
  }

  public static final class Builder {
    private Long vacationId;
    private String status;
    private Long dateFrom;
    private Long dateTo;
    private boolean isHidden;
    private String userId;
    private String description;

    private Builder() {
    }

    public Builder vacationId(Long vacationId) {
      this.vacationId = vacationId;
      return this;
    }

    public Builder status(String status) {
      this.status = status;
      return this;
    }

    public Builder dateFrom(Long dateFrom) {
      this.dateFrom = dateFrom;
      return this;
    }

    public Builder dateTo(Long dateTo) {
      this.dateTo = dateTo;
      return this;
    }

    public Builder isHidden(boolean isHidden) {
      this.isHidden = isHidden;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Vacation build() {
      return new Vacation(this);
    }
  }

  public boolean isValid(EmployeeVacationRequestDto vacation, CurrentDate currentDate) {
    Long endDate = vacation.getToDate();
    Long fromDate = vacation.getFromDate();
    Long now = currentDate.getTime();

    if (isFromDateBiggerThanEndDate(endDate, fromDate)) {
      return false;
    }

    return isCurrentDateLessThenComparedDates(fromDate, endDate, now);
  }

  private boolean isFromDateBiggerThanEndDate(Long endDate, Long fromDate) {
    return isBiggerOf(fromDate, endDate);
  }

  private boolean isCurrentDateLessThenComparedDates(Long fromDate, Long endDate, Long now) {
    return isBiggerOf(endDate, now) && isBiggerOf(fromDate, now);
  }

  private boolean isBiggerOf(Long startDate, Long secondDate) {
    return startDate.compareTo(secondDate) == 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Vacation vacation = (Vacation) o;

    if (isHidden != vacation.isHidden) return false;
    if (vacationId != null ? !vacationId.equals(vacation.vacationId) : vacation.vacationId != null) return false;
    if (status != null ? !status.equals(vacation.status) : vacation.status != null) return false;
    if (dateFrom != null ? !dateFrom.equals(vacation.dateFrom) : vacation.dateFrom != null) return false;
    if (dateTo != null ? !dateTo.equals(vacation.dateTo) : vacation.dateTo != null) return false;
    if (userId != null ? !userId.equals(vacation.userId) : vacation.userId != null) return false;
    return !(description != null ? !description.equals(vacation.description) : vacation.description != null);

  }

  @Override
  public int hashCode() {
    int result = vacationId != null ? vacationId.hashCode() : 0;
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
    result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
    result = 31 * result + (isHidden ? 1 : 0);
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
