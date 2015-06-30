package com.clouway.hr.adapter.http;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class VacationResponseDto {
  private long vacationId;
  private String status;
  private Long dateFrom;
  private Long dateTo;
  private String userId;
  private String description;

  public VacationResponseDto() {
  }

  private VacationResponseDto(Builder builder) {
    vacationId = builder.vacationId;
    status = builder.status;
    dateFrom = builder.dateFrom;
    dateTo = builder.dateTo;
    userId = builder.userId;
    description = builder.description;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {

    private long vacationId;
    private String status;
    private Long dateFrom;
    private Long dateTo;
    private String userId;
    private String description;

    private Builder() {
    }

    public Builder vacationId(long vacationId) {
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

    public VacationResponseDto build() {
      return new VacationResponseDto(this);
    }

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

  public String getUserId() {
    return userId;
  }

  public String getDescription() {
    return description;
  }
}
