package com.clouway.hr.adapter.http;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployeeDto {
  public String email;
  public String team;
  public String name;
  public boolean isAdmin;

  public EmployeeDto(String email, String team, String name, boolean isAdmin) {
    this.email = email;
    this.team = team;
    this.name = name;
    this.isAdmin = isAdmin;
  }

  public EmployeeDto() {
  }
}
