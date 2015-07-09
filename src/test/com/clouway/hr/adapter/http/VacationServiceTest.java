package com.clouway.hr.adapter.http;

import com.clouway.hr.core.VacationRepository;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.clouway.hr.adapter.http.ReplyMatcher.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationServiceTest {
  @Mock
  public VacationRepository vacationRepository;

  private VacationRepository persistentVacationRepository;
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void changeVacationStatus() {
    VacationService employerService = new VacationService(vacationRepository);
    final Long id = 1l;

    context.checking(new Expectations() {{
      oneOf(vacationRepository).updateStatus(id, "approved");
    }});

    Reply<ResponseMessageDto> actualReply = employerService.changeVacationStatus(id.toString(), "approved");
    Reply<ResponseMessageDto> success = Reply.with(new ResponseMessageDto("success")).as(Json.class);

    assertThat(actualReply, is(success));
  }
}