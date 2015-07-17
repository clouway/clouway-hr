package com.clouway.hr.adapter.http;


/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployeeVacationRequestDto {
  private Long vacationId;
  private String status;
  private Long fromDate;
  private Long toDate;
  private String userId;
  private String description;
  private boolean isHidden;


  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {

    private Long vacationId;
    private String status;
    private Long dateFrom;
    private Long dateTo;
    private String userId;
    private String description;
    private boolean isHidden;

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

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder isHidden(boolean isHidden) {
      this.isHidden = isHidden;
      return this;
    }

    public EmployeeVacationRequestDto build() {
      return new EmployeeVacationRequestDto(this);
    }

    private Builder() {
    }
  }

  public EmployeeVacationRequestDto() {
  }

  private EmployeeVacationRequestDto(Builder builder) {
    vacationId = builder.vacationId;
    status = builder.status;
    fromDate = builder.dateFrom;
    toDate = builder.dateTo;
    userId = builder.userId;
    description = builder.description;
    isHidden = builder.isHidden;
  }

  public Long getVacationId() {
    return vacationId;
  }

  public String getStatus() {
    return status;
  }

  public Long getFromDate() {
    return fromDate;
  }

  public Long getToDate() {
    return toDate;
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