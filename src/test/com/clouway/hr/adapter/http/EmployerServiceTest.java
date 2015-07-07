package com.clouway.hr.adapter.http;

import com.clouway.hr.core.UserRepository;
import com.clouway.hr.core.VacationRepository;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployerServiceTest {
  private EmployerService employerService;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  VacationRepository vacationRepository;

  @Before
  public void setUp() throws Exception {
    employerService = new EmployerService(vacationRepository);
  }

  @Test
  public void approveVacation() {

    final Long id = 1l;

    context.checking(new Expectations() {{
      oneOf(vacationRepository).updateStatus(id, "approved");
    }});
    Reply<Object> actualReply = employerService.approveVacation(id.toString(), "approved");

    assertThat(actualReply, is(Reply.saying().ok()));
  }
}