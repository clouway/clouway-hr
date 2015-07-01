package com.clouway.hr.adapter.http;

import com.clouway.hr.adapter.db.PersistentVacationRepository;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.util.Providers;
import com.google.sitebricks.headless.Reply;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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

  @Before
  public void setUp() throws Exception {
    helper.setUp();
//    statuses.add("pending");
//    statuses.add("approve");
//    statuses.add("getReject");

    VacationRepository repository = new PersistentVacationRepository(Providers.of(datastore), Providers.of(statuses));
    employeeService = new EmployeeService(repository);
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
            .fromDate(1L)
            .toDate(2L)
            .build();

    Reply<Object> actualReply = employeeService.requestVacation(fakeVacationRequest);

    assertThat(actualReply, is(Reply.saying().ok()));
  }

  @Test
  public void endDateVacationBiggerThanFromDate() {
    fakeVacationRequest.dto = VacationRequestDto.newBuilder().userId("ivan@gmail.com").fromDate(2L).toDate(1L).build();

    Reply<Object> actualReply = employeeService.requestVacation(fakeVacationRequest);

    assertThat(actualReply, is(Reply.saying().error()));
  }

}
