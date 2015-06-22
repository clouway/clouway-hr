package com.clouway.hr.adapter.http;

import com.clouway.hr.adapter.db.PersistentVacationRepository;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployerServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  public VacationRepository vacationRepository;

  private ObjectDatastore datastore = new AnnotationObjectDatastore();
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private VacationRepository repository;
  private VacationStatus status = new VacationStatus();

  @Before
  public void setUp() {
    helper.setUp();
    repository = new PersistentVacationRepository(of(datastore), of(status));
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void changeVacationStatus() {
    EmployerService employerService = new EmployerService(vacationRepository);
    final Long id = 1l;

    context.checking(new Expectations() {{
      oneOf(vacationRepository).updateStatus(id, "approved");
    }});

    Reply<ResponseMessageDto> actualReply = employerService.changeVacationStatus(id.toString(), "approved");
    Reply<ResponseMessageDto> success = Reply.with(new ResponseMessageDto("success")).as(Json.class);

    assertThat(actualReply, is(success));
  }

  @Test
  public void changeIncorrectStatus() {
    final String incorrectStatus = "incorrectStatus";
    addRequestVacation();

    EmployerService employer = new EmployerService(repository);
    employer.changeVacationStatus("1", incorrectStatus);
    String nonExistentStatus = repository.getStatus(1L);

    assertFalse(nonExistentStatus.equals(incorrectStatus));
  }

  private void addRequestVacation() {
    EmployeeService service = new EmployeeService(repository);
    FakeRequest fakeRequest = new FakeRequest();

    fakeRequest.dto = new VacationRequestDto(1L, 2L, 1L, "some description");
//            .vacationId(1L)
//            .dateFrom(2l)
//            .dateTo(2l)
//            .userId(1l)
//            .status("pending")
//            .build();

    service.requestVacation(fakeRequest);
  }
}