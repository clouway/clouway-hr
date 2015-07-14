package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.VacationRequestDto;
import com.clouway.hr.adapter.http.VacationResponseDto;
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

import java.util.List;

import static com.clouway.hr.adapter.http.RandomGenerator.generateVacationRequestDto;
import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepositoryTest {
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private ObjectDatastore datastore = new AnnotationObjectDatastore();
  private Status status = new VacationStatus("approved", "pending", "reject");
  private VacationRepository vacationRepository;
  private VacationRequestDto vacationRequestDto;
  private List<VacationResponseDto> vacationResponseDtos;

  @Before
  public void setUp() {
    helper.setUp();
    vacationRepository = new PersistentVacationRepository(of(datastore), of(status));
    vacationRequestDto = generateVacationRequestDto();
    vacationRepository.add(vacationRequestDto);
    vacationResponseDtos = vacationRepository.get(vacationRequestDto.getUserId());
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void addAndUpdateVacationStatus() {
    vacationRepository.updateStatus(1L, status.getAccept());

    List<VacationResponseDto> vacationResponseDtos = vacationRepository.get(vacationRequestDto.getUserId());

    assertThat(vacationResponseDtos.size(), is(1));
    assertThat(vacationResponseDtos.get(0).getStatus(), is(status.getAccept()));
  }

  @Test
  public void addAndGetPendingVacationRequest() {
    assertThat(vacationResponseDtos.size(), is(1));
    assertThat(vacationResponseDtos.get(0).getStatus(), is(status.getPending()));
  }

  @Test
  public void addAndGetNotViewedVacationStatuses() {
    vacationRepository.add(vacationRequestDto);

    vacationRepository.hide(1L);
    List<VacationResponseDto> vacationResponseDtos = vacationRepository.getUnHidden(getCurrentUser());

    assertThat(vacationResponseDtos.size(), is(1));
    assertThat(vacationResponseDtos.get(0).getStatus(), is(status.getPending()));
    assertThat(vacationResponseDtos.get(0).getVacationId(), is(2L));
    assertFalse(vacationResponseDtos.get(0).isHidden());
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


}