package com.clouway.hr.adapter.http;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationRequestDto {
  private String userId;
  private Long fromDate;
  private Long toDate;
  private String description;

  public VacationRequestDto() {
  }

  private VacationRequestDto(Builder builder) {
    userId = builder.userId;
    fromDate = builder.fromDate;
    toDate = builder.toDate;
    description = builder.description;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String userId;
    private Long fromDate;
    private Long toDate;
    private String description;

    private Builder() {
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder fromDate(Long fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    public Builder toDate(Long toDate) {
      this.toDate = toDate;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public VacationRequestDto build() {
      return new VacationRequestDto(this);
    }
  }

  //todo will remove after getting current user
  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
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
