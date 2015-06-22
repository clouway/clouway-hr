package com.clouway.hr.adapter.http;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class VacationResponseDto {
  private long vacationId;
  private String status;
  private Long dateFrom;
  private Long dateTo;
  private Long userId;

  public VacationResponseDto(long vacationId, String status, Long dateFrom, Long dateTo, Long userId) {
    this.vacationId = vacationId;
    this.status = status;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.userId = userId;
  }

  public VacationResponseDto() {
  }

  private VacationResponseDto(Builder builder) {
    vacationId = builder.vacationId;
    status = builder.status;
    dateFrom = builder.dateFrom;
    dateTo = builder.dateTo;
    userId = builder.userId;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private long vacationId;
    private String status;
    private Long dateFrom;
    private Long dateTo;

    private Long userId;

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

    public Builder userId(Long userId) {
      this.userId = userId;
      return this;
    }

    public VacationResponseDto build() {
      return new VacationResponseDto(this);
    }

  }

  public long getVacationId() {
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

  public Long getUserId() {
    return userId;
  }


}
