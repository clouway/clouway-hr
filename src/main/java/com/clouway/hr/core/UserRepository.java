package com.clouway.hr.core;

import com.clouway.hr.adapter.http.EmployeeDto;

import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface UserRepository {
  void addEmployee(EmployeeDto employee);

  void editEmployeeTeam(EmployeeDto editedEmployee);

  List<EmployeeDto> findAllEmployees();

  boolean checkForExistingUser(String email);

  List<EmployeeDto> refreshEmployeeTeams();
}
