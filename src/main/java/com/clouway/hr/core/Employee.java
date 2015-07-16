package com.clouway.hr.core;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class Employee {
  public final String email;
  public final String team;
  public final String name;

  public Employee(String email, String team, String name) {
    this.email = email;
    this.team = team;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Employee)) return false;

    Employee employee = (Employee) o;

    if (email != null ? !email.equals(employee.email) : employee.email != null) return false;
    if (team != null ? !team.equals(employee.team) : employee.team != null) return false;
    return !(name != null ? !name.equals(employee.name) : employee.name != null);

  }

  @Override
  public int hashCode() {
    int result = email != null ? email.hashCode() : 0;
    result = 31 * result + (team != null ? team.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
}
