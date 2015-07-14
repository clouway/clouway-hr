package com.clouway.hr.core;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */

//todo just for demo. it will be replaced with currentUser
public class CurrentUser implements User {
  @Override
  public String getEmail() {
    return "marin@gmail.com";
  }

  @Override
  public boolean isAdmin() {
    return true;
  }
}