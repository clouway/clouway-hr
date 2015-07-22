package com.clouway.hr.core;

import com.google.api.services.admin.directory.model.Group;

import java.util.List;
import java.util.Set;

/**
 * Created on 15-7-20.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface OAuthUser {

  String getEmail();

  Set<String> getRoles();

}