package com.clouway.hr.http;

import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployerPage {

  public Reply<String> approveVacation() {

    return Reply.with("approve");
  }
}
