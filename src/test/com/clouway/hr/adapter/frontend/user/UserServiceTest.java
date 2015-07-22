package com.clouway.hr.adapter.frontend.user;

import com.clouway.hr.core.user.CurrentUser;
import com.google.sitebricks.headless.Reply;
import org.junit.Test;

import static com.clouway.hr.test.custom.matchers.SitebricksReplyMatchers.contains;
import static com.clouway.hr.test.custom.matchers.SitebricksReplyMatchers.hasStatusCode;
import static org.junit.Assert.*;

/**
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