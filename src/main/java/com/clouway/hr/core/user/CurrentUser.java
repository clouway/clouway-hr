package com.clouway.hr.core.user;

/**
 * Created on 15-7-20.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class CurrentUser {

  public final String email;
  public final boolean isAdmin;

  public CurrentUser(String email, boolean isAdmin) {
    this.email = email;
    this.isAdmin = isAdmin;
  }
}