package com.clouway.hr.adapter.frontend.user;

import com.clouway.hr.core.user.CurrentUser;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

/**
 * Created on 15-7-22.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Service
@At("/userservices")
public class UserService {

  private final CurrentUser currentUser;

  @Inject
  public UserService(CurrentUser currentUser) {

    this.currentUser = currentUser;
  }

  @At("/currentuser")
  @Get
  public Reply getCurrentUser() {

    return Reply.with(currentUser).as(Json.class);
  }

}