package com.clouway.hr.adapter.http.oauth2;

import com.clouway.hr.adapter.http.DirectoryDto;
import com.clouway.hr.adapter.http.EmployeeDto;
import com.clouway.hr.core.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created on 15-6-17.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@At("/oauth-callback")
@Show("index.html")
public class OAuthCallback {

  private final Provider<HttpServletRequest> requestProvider;
  private final UserRepository userRepository;

  @Inject
  public OAuthCallback(Provider<HttpServletRequest> requestProvider, UserRepository userRepository) {
    this.requestProvider = requestProvider;
    this.userRepository = userRepository;
  }


  @Get
  public String processCallback() throws IOException {
    OAuth oAuth = new OAuth();
    try {
      HttpServletRequest request = requestProvider.get();
      String authorizationCode = request.getParameter("code");

      final GoogleTokenResponse googleTokenResponse = oAuth.getGoogleTokenResponse(authorizationCode);
      final GoogleCredential credential = oAuth.getGoogleCredential(googleTokenResponse);
      DirectoryDto.directory = oAuth.getDirectoryService(credential);;
      String email = oAuth.getUserInfo(credential).getEmail();
      String name = oAuth.getUserInfo(credential).getName();
      if (!userRepository.checkForExistingUser(email)){
        userRepository.addEmployee(new EmployeeDto(email, "no team", name));
      }

      return "/#hr/employee";

    } catch (GoogleJsonResponseException e) {
      return "/login?errorMessage=You are not authorizes!";
    }
  }

  private boolean stateIsValid() {

    String incomingState = requestProvider.get().getParameter("state");
    String expectedState = (String) requestProvider.get().getSession().getAttribute("oauth2-state");

    return StringUtils.equals(incomingState, expectedState);
  }
}
