package com.clouway.hr.core.user;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class CurrentUser implements User {
  private final String email;
  private final boolean isAdmin;

  public CurrentUser(String email, boolean isAdmin) {
    this.email = email;
    this.isAdmin = isAdmin;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public boolean isAdmin() {
    return isAdmin;
  }
}