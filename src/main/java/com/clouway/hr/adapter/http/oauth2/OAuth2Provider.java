package com.clouway.hr.adapter.http.oauth2;

/**
 * Created on 15-6-19.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OAuth2Provider {

  public final static String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
  public final static String GOOGLE_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
  public final static String GOOGLE_USER_INFO_PLUS_URL = "https://www.googleapis.com/auth/userinfo.profile";
  public final static String GOOGLE_USER_INFO_PLUS_URL2 = "https://www.googleapis.com/auth/userinfo.email";
  public final static String GOOGLE_USER_INFO_URL_JSON = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=";

  public final static String GOOGLE_CLIENT_SECRET = "PliEQGwsNAikGJ3bhdVRWDyv";
  public final static String GOOGLE_CLIENT_ID = "356050885992-5lpa69jrg0smoabd3tvuppgv32e08la8.apps.googleusercontent.com";

//    public final static String REDIRECT_URI = "http://localhost:8080/oauth-callback";
  public final static String REDIRECT_URI = "http://clouway-hr2.appspot.com/oauth-callback";


}
