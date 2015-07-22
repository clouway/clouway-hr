package com.clouway.hr.adapter.frontend.user;

import com.clouway.hr.core.CurrentUser;
import com.google.sitebricks.headless.Reply;
import org.junit.Test;

import static com.clouway.hr.adapter.http.matchers.SitebricksReplyMatchers.contains;
import static com.clouway.hr.adapter.http.matchers.SitebricksReplyMatchers.hasStatusCode;
import static org.junit.Assert.*;

/**
 * Created on 15-7-22.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class UserServiceTest {

  @Test
  public void currentUser() throws Exception {

    final String email = "user@email.com";
    final boolean isAdmin = true;
    final CurrentUser currentUser = new CurrentUser(email, isAdmin);
    UserService userService = new UserService(currentUser);

    final Reply reply = userService.getCurrentUser();

    assertThat(reply, contains(currentUser));
    assertThat(reply, hasStatusCode(200));

  }
}