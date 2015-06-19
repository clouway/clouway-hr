package com.clouway.hr.core;

/**
 * Created on 15-6-25.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class CurrentUser {

  public String id;
  public boolean isAdmin;

  public CurrentUser(String id, boolean isAdmin) {
    this.id = id;
    this.isAdmin = isAdmin;
  }

}
