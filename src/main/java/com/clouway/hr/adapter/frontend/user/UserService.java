package com.clouway.hr.adapter.frontend.user;

import com.clouway.hr.core.user.User;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Service
@At("/userservices")
public class UserService {

  private final User currentUser;

  @Inject
  public UserService(User currentUser) {

    this.currentUser = currentUser;
  }

  @At("/currentuser")
  @Get
  public Reply getCurrentUser() {

    return Reply.with(currentUser).as(Json.class);
  }

}