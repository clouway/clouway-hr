package com.clouway.hr.adapter.user.google.oauth;

import java.util.Set;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface OAuthUser {

  String getEmail();

  Set<String> getRoles();

}