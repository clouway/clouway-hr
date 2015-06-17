package com.clouway.hr.http;

import com.clouway.hr.FakeEmployeeRequest;
import com.google.sitebricks.headless.Reply;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployerTest {

  @Test
  public void happyPath() {
    EmployerPage employer = new EmployerPage();

    Reply<String> employerReply = employer.approveVacation();

    assertThat(employerReply, is(Reply.with("approve")));
  }
}
