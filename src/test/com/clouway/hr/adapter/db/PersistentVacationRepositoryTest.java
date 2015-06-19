package com.clouway.hr.adapter.db;

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
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentVacationRepositoryTest {
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private ObjectDatastore datastore = new AnnotationObjectDatastore();
  private VacationStatus status = new VacationStatus();

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
    status.add("pending");
    status.add("approved");

    VacationRepository vacationRepository = new PersistentVacationRepository(of(datastore), of(status));
    vacationRepository.add(1l, "pending");

    vacationRepository.updateStatus(1l, "approved");
    VacationEntity vacation = datastore.load(VacationEntity.class, 1l);

    assertThat(vacation.getStatus(), is("approved"));
    assertThat(vacation.getVacationId(), is(1l));
  }


}