package com.clouway.hr.adapter.http;

import com.clouway.hr.core.VacationRepository;
import com.google.sitebricks.headless.Reply;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployerServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  VacationRepository vacationRepository;

  @Test
  public void approveVacation() {
    EmployerService employerService = new EmployerService(vacationRepository);
    final Long id = 1l;

    context.checking(new Expectations() {{
      oneOf(vacationRepository).updateStatus(id, "approved");
    }});
    Reply<Object> actualReply = employerService.approveVacation(id.toString(), "approved");

    assertThat(actualReply, is(Reply.saying().ok()));
  }
}