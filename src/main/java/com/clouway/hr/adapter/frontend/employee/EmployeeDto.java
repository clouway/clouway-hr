package com.clouway.hr.adapter.frontend.employee;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployeeDto {
  private String email;
  private String team;
  private String name;

  public EmployeeDto() {
  }

  public EmployeeDto(String email, String team, String name) {
    this.email = email;
    this.team = team;
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public String getTeam() {
    return team;
  }

  public String getName() {
    return name;
  }
}
