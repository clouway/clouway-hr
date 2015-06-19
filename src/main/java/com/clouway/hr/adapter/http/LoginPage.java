package com.clouway.hr.adapter.http;

import com.clouway.hr.adapter.http.oauth2.OAuth2Provider;
import com.clouway.hr.adapter.http.oauth2.Util;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created on 15-6-19.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@At("/login")
@Show("oauth-login.html")
public class LoginPage {

  public String email;
  public String password;
  public String message;
  public String errorMessage;
  public String oauthURL;

  private final Provider<HttpServletRequest> requestProvider;

  @Inject
  public LoginPage(Provider<HttpServletRequest> requestProvider) {

    this.requestProvider = requestProvider;
  }


  @Post
  public String login() {

//    List<String> errorList = validator.validateRequestParams();
//
//    if (errorList.size() != 0) {
//      return "/login?errorMessage=" + errorList.get(0);
//    }
//
//    boolean authorizationResult = userRepository.authorize(email, password);
//
//    if (!authorizationResult) {
//      return "/login?errorMessage=User do not exist!";
//    }
//
//    userSession.create(email);
//
    return "/wallet";

  }

  @Get
  public void createAuthentificationLink() throws URISyntaxException, MalformedURLException {

    HttpServletRequest request = requestProvider.get();

    String state = RandomStringUtils.random(32, true, true);
    request.getSession().setAttribute("oauth2-state", state);

    URIBuilder builder = new URIBuilder(OAuth2Provider.GOOGLE_AUTH_URL);

    builder.addParameter("state", state);

    URL redirectUrl = Util.getAbsoluteUrl(request, "oauth-callback");

    builder.addParameter("redirect_uri", redirectUrl.toString());
    System.out.println(redirectUrl.toString());

    builder.addParameter("client_id", OAuth2Provider.GOOGLE_CLIENT_ID);
    builder.addParameter("response_type", "code");
    builder.addParameter("scope", "profile email");
    builder.addParameter("approval_prompt", "force");

    oauthURL = builder.toString();
  }
}
