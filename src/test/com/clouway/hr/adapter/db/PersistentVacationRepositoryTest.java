package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.VacationRequestDto;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.VacationStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.auto.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.inject.util.Providers.of;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepositoryTest {
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private ObjectDatastore datastore = new AnnotationObjectDatastore();
  private Status status = new VacationStatus("", "", "");

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void updateVacationStatus() {
    VacationRepository vacationRepository = new PersistentVacationRepository(of(datastore), of(status));
    VacationRequestDto vacation = VacationRequestDto.newBuilder()
            .userId("gosho@gmail.com")
            .fromDate(2L)
            .toDate(2L)
            .description("some description")
            .build();
    vacationRepository.add(vacation);
    vacationRepository.updateStatus(1l, "approved");
  }


}