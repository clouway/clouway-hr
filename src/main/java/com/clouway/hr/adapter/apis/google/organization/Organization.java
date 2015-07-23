package com.clouway.hr.adapter.apis.google.organization;

import java.util.Set;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface Organization {

  Set<String> getUserRoles(String email);

}