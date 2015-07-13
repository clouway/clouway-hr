package com.clouway.hr.core;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface UserRepository {

  boolean isRegistered(String email);

  boolean isAdmin(String email);

}
