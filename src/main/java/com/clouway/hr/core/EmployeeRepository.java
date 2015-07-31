package com.clouway.hr.core;

import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface EmployeeRepository {

  /**
   * Move user from one group to another in Google Apps
   *
   * @param email   - is the email of the user, who we want to move
   * @param newTeam - is the name of the group, where we want to move the user
   */
  void editEmployeeTeam(String email, String newTeam);

  /**
   * Get all users of organization
   *
   * @return - list with all users for that organization
   */
  List<Employee> findAllEmployees();

  /**
   * Get group of the user from Google Apps
   *
   * @param email - email of the user, which we want to get his group
   * @return - group of the user
   */
  String findTeam(String email);

}
