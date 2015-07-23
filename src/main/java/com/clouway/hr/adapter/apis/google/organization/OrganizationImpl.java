package com.clouway.hr.adapter.apis.google.organization;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.Group;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OrganizationImpl implements Organization {

  private final DirectoryServiceFactory directoryServiceFactory;

  @Inject
  public OrganizationImpl(DirectoryServiceFactory directoryServiceFactory) {

    this.directoryServiceFactory = directoryServiceFactory;
  }

  @Override
  public Set<String> getUserRoles(String email) {

    final Directory directory = directoryServiceFactory.create(email);

    List<Group> userGroups;
    Set<String> roles = Sets.newHashSet();
    String role;

    try {

      userGroups = directory.groups().list().setUserKey(email).execute().getGroups();

      for (Group group : userGroups) {

        role = directory.members().get(group.getEmail(), email).execute().getRole();
        roles.add(role);

      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return roles;
  }


}
