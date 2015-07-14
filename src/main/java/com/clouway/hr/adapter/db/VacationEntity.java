package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
class VacationEntity {
  @Key
  private Long id;

  private String status;
  private Long dateFrom;
  private Long dateTo;
  private String userId;
  private String description;
  private boolean isHidden;

  public VacationEntity() {
  }

  private VacationEntity(Builder builder) {
    id = builder.vacationId;
    status = builder.status;
    dateFrom = builder.dateFrom;
    dateTo = builder.dateTo;
    userId = builder.userId;
    description = builder.description;
    isHidden = builder.isHidden;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public void setIsHidden(boolean isHidden) {
    this.isHidden = isHidden;
  }

  public static final class Builder {
    private Long vacationId;
    private String status;
    private Long dateFrom;
    private Long dateTo;
    private String userId;
    private String description;
    private boolean isHidden;

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

    public VacationEntity build() {
      return new VacationEntity(this);
    }

  }

  public long getId() {
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