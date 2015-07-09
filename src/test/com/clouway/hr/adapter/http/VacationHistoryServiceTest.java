package com.clouway.hr.adapter.http;

import com.clouway.hr.adapter.db.PersistentVacationRepository;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationHistoryServiceTest {
  private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private Status statuses;
  private ObjectDatastore datastore = new AnnotationObjectDatastore();

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    statuses = new VacationStatus("accept", "pending", "reject");
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void getHistory() {
    VacationRepository vacationRepository = new PersistentVacationRepository(of(datastore), of(statuses));

    VacationRequestDto vacationRequest = VacationRequestDto.newBuilder()
            .userId("george@abv.bg")
            .fromDate(2L)
            .toDate(10L)
            .build();
    vacationRepository.add(vacationRequest);
    User employerUser = getUser(true, "george@abv.bg");
    String email = employerUser.getEmail();
    assertTrue(vacationRepository.getHistory(employerUser).isEmpty());

    User employeeUser = getUser(false, "george@abv.bg");
    assertThat(vacationRepository.getHistory(employeeUser).size(), is(1));
    assertThat(vacationRepository.getHistory(employeeUser).get(0).getUserId(), is("george@abv.bg"));
    assertThat(vacationRepository.getHistory(employeeUser).get(0).getDateTo(), is(10L));
  }

  @Test
  public void orderVacationsByEndDate() {
    VacationRepository vacationRepository = new PersistentVacationRepository(of(datastore), of(statuses));

    VacationRequestDto vacationWithHigherDateTo = VacationRequestDto.newBuilder()
            .userId("michael@abv.bg")
            .fromDate(2L)
            .toDate(10L)
            .build();
    VacationRequestDto vacationWithLowerDateTo = VacationRequestDto.newBuilder()
            .userId("michael@abv.bg")
            .fromDate(4L)
            .toDate(5L)
            .build();
    vacationRepository.add(vacationWithLowerDateTo);
    vacationRepository.add(vacationWithHigherDateTo);

    User employeeUser = getUser(false, "michael@abv.bg");

    assertThat(vacationRepository.getHistory(employeeUser).get(1).getDateTo(), is(5L));
    assertThat(vacationRepository.getHistory(employeeUser).get(0).getDateTo(), is(10L));
  }

  private User getUser(final boolean isAdmin, final String userId) {
    return new User() {
      @Override
      public String getEmail() {
        return userId;
      }

      @Override
      public boolean isAdmin() {
        return isAdmin;
      }
    };
  }
}