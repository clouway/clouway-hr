package com.clouway.hr.adapter.http;

import com.clouway.hr.core.VacationRepository;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployeeServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  VacationRepository vacationRepository;

  @Test
  public void requestVacation() {
    EmployeeService employeeService = new EmployeeService(vacationRepository);
    FakeRequest fakeRequest = new FakeRequest();
    final Long id = 1l;

    final VacationRequestDto vacation = new VacationRequestDto(id, 1L, 2L, "description");
//            .vacationId(id)
//            .status("pending")
//            .build();

    fakeRequest.dto = vacation;

    context.checking(new Expectations() {{
      oneOf(vacationRepository).add(vacation);
    }});
    Reply<Object> actualReply = employeeService.requestVacation(fakeRequest);

    assertThat(actualReply, is(Reply.saying().ok()));
  }
}
