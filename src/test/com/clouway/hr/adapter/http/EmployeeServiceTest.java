package com.clouway.hr.adapter.http;

import com.clouway.hr.adapter.db.PersistentVacationRepository;
import com.clouway.hr.core.CurrentDate;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
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
public class EmployeeServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  private EmployeeService employeeService;
  private FakeRequest<VacationRequestDto> fakeVacationRequest;
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final ObjectDatastore datastore = new AnnotationObjectDatastore();
  private final Status statuses = new VacationStatus("accept", "pending", "reject");

  @Mock
  private CurrentDate currentDate;

  @Before
  public void setUp() throws Exception {
    helper.setUp();

    VacationRepository repository = new PersistentVacationRepository(of(datastore), of(statuses));
    employeeService = new EmployeeService(repository, currentDate);
    fakeVacationRequest = new FakeRequest();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void requestVacation() {
    fakeVacationRequest.dto = VacationRequestDto.newBuilder()
            .userId("gosho@gmail.com")
            .fromDate(55L)
            .toDate(60L)
            .build();
    mockWithDateOf(30L);

    assertThat(requestVacations(), is(Reply.saying().ok()));
  }

  @Test
  public void endDateVacationBiggerThanFromDate() {
    fakeVacationRequest.dto = VacationRequestDto.newBuilder()
            .userId("ivan@gmail.com")
            .fromDate(2L)
            .toDate(1L)
            .build();
    mockWithDateOf(50L);

    assertThat(requestVacations(), is(Reply.saying().error()));
  }

  @Test
  public void endDateVacationLessThanCurrentDate() {
    fakeVacationRequest.dto = VacationRequestDto.newBuilder()
            .userId("ivan@gmail.com")
            .fromDate(10L)
            .toDate(10L)
            .build();

    mockWithDateOf(50L);

    assertThat(requestVacations(), is(Reply.saying().error()));
  }

  @Test
  public void startDateVacationLessThanCurrentDate() {
    fakeVacationRequest.dto = VacationRequestDto.newBuilder()
            .userId("ivan@gmail.com")
            .fromDate(10L)
            .toDate(160L)
            .build();

    mockWithDateOf(40L);

    assertThat(requestVacations(), is(Reply.saying().error()));
  }

  private Reply<Object> requestVacations() {
    return employeeService.requestVacation(fakeVacationRequest);
  }

  private void mockWithDateOf(final Long date) {
    context.checking(new Expectations() {{
      oneOf(currentDate).getTime();
      will(returnValue(date));
//      oneOf(currentUser).isAdmin();
//      will(returnValue(true));
    }});
  }
}
