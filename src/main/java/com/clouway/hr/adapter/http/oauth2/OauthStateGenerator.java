package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.core.RandomStringGenerator;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Created on 15-6-23.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OauthStateGenerator implements RandomStringGenerator{
  @Override
  public String random() {
    return RandomStringUtils.random(32,true,true);
  }
}
