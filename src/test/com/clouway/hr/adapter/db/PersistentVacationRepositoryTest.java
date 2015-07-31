package com.clouway.hr.adapter.db;

import com.clouway.hr.core.Vacation;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.user.User;
import com.clouway.hr.core.vacationstate.ApproveVacationStatus;
import com.clouway.hr.core.vacationstate.State;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.clouway.hr.adapter.http.RandomGenerator.generateVacationDo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepositoryTest {
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private ObjectDatastore datastore = new AnnotationObjectDatastore();
  private VacationRepository vacationRepository;
  private Vacation vacation;
  private List<Vacation> vacationResponseDtos;
  private com.clouway.hr.core.vacationstate.VacationStatus vacationStatus;

  @Before
  public void setUp() {
    helper.setUp();
    State state = new ApproveVacationStatus();
    vacationStatus = new com.clouway.hr.core.vacationstate.VacationStatus();
    vacationRepository = new PersistentVacationRepository(datastore, state);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void approveVacation() {
    addVacation(1L);

    vacationStatus.id = 1L;
    vacationRepository.approve(vacationStatus);

    assertThat(vacationRepository.get(vacation.getUserId()).get(0).getVacationId(), is(1L));
    assertThat(vacationRepository.get(vacation.getUserId()).get(0).getStatus(), is("approve"));
  }

  @Test
  public void rejectVacation() {
    addVacation(1L);

    vacationStatus.id = 1L;
    vacationRepository.reject(vacationStatus);

    assertThat(vacationRepository.get(vacation.getUserId()).get(0).getVacationId(), is(1L));
    assertThat(vacationRepository.get(vacation.getUserId()).get(0).getStatus(), is("reject"));
  }

  @Test
  public void addAndGetPendingVacationRequest() {
    vacation = generateVacationDo(1L);
    vacationRepository.add(vacation);
    vacationResponseDtos = getVacations();

    assertThat(vacationResponseDtos.size(), is(1));
    assertThat(vacationResponseDtos.get(0).getStatus(), is("pending"));
  }

  @Test
  public void addAndGetNotViewedVacationStatuses() {
    addVacation(1L);

    vacationRepository.hide(1L);
    List<Vacation> vacationResponseDtos = getUnHidden();

    assertThat(vacationResponseDtos.size(), is(1));
    assertThat(vacationResponseDtos.get(0).getStatus(), is("pending"));
    assertThat(vacationResponseDtos.get(0).getVacationId(), is(2L));
    assertFalse(vacationResponseDtos.get(0).isHidden());
  }

  private List<Vacation> getUnHidden() {
    return vacationRepository.getUnHidden(getCurrentUser());
  }

  private User getCurrentUser() {
    return new User() {
      @Override
      public String getEmail() {
        return vacationResponseDtos.get(0).getUserId();
      }

      @Override
      public boolean isAdmin() {
        return false;
      }
    };
  }

  private void addVacation(Long vacationId) {
    vacation = generateVacationDo(vacationId);
    vacationRepository.add(vacation);
    vacationResponseDtos = getVacations();
    vacationRepository.add(vacation);
  }

  private List<Vacation> getVacations() {
    return vacationRepository.get(vacation.getUserId());
  }
}