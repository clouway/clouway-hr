package com.clouway.hr.adapter.user.google.oauth;

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