package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.OAuthAuthentication;
import com.clouway.hr.core.OAuthUser;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.Group;
import com.google.appengine.api.users.UserService;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created on 15-7-20.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuthUserImpl implements OAuthUser {

  private final UserService userService;
  private final OAuthAuthentication oAuthAuthentication;

  @Inject
  public OAuthUserImpl(UserService userService, OAuthAuthentication oAuthAuthentication) {

    this.userService = userService;
    this.oAuthAuthentication = oAuthAuthentication;
  }


  @Override
  public String getEmail() {
    return userService.getCurrentUser().getEmail();
  }


  @Override
  public Set<String> getRoles() {

    final String email = userService.getCurrentUser().getEmail();

    final Directory directory = oAuthAuthentication.getGoogleDirectoryService(email);

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