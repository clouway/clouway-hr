package com.clouway.hr.adapter.apis.google.organization;

import java.util.Set;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface Organization {

  /**
   * Gets user role from Google Apps
   * @param email - is the email of the user, which we want to get his role
   * @return set with roles
   */
  Set<String> getUserRoles(String email);
}