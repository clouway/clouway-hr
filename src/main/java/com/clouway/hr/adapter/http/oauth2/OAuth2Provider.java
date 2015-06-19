package com.clouway.hr.adapter.http.oauth2;

/**
 * Created on 15-6-19.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuth2Provider {

  public final static String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
  public final static String GOOGLE_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
  public final static String GOOGLE_USER_INFO_PROFILE_URL = "https://www.googleapis.com/auth/userinfo.profile";
  public final static String GOOGLE_USER_INFO_EMAIL = "https://www.googleapis.com/auth/userinfo.email";
  public final static String GOOGLE_USER_INFO_URL_JSON = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=";

  public final static String GOOGLE_CLIENT_SECRET = "I01KZ8qt45eorg3HN_QYvd2r";
  public final static String GOOGLE_CLIENT_ID = "515564595569-v12drf6ltbjthgtligs46uue6a3mkmi0.apps.googleusercontent.com";

//  public final static String REDIRECT_URI = "http://localhost:8080/oauth/callback";
  public final static String REDIRECT_URI = "https://clouway-hr-2.appspot.com/oauth/callback";
}
