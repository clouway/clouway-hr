package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.EmployeeDto;
import com.clouway.hr.core.UserRepository;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.auto.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentUserRepositoryTest {
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private UserRepository userRepository;
  private ObjectDatastore datastore = new AnnotationObjectDatastore();

  @Before
  public void setUp() {
    helper.setUp();
    userRepository = new PersistentUserRepository(of(datastore));
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void addEmployee() {
    userRepository.addEmployee(new EmployeeDto("petko.ivanov@gmail.com", "WSS", "petko"));
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.size(), is(1));
    assertThat(result.get(0).email, is("petko.ivanov@gmail.com"));
    assertThat(result.get(0).team, is("WSS"));
    assertThat(result.get(0).name, is("petko"));
  }

  @Test
  public void addAnotherEmployee(){
    userRepository.addEmployee(new EmployeeDto("petko.ivanov@gmail.com", "WSS", "petko"));
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.size(), is(1));
    assertThat(result.get(0).email, is("petko.ivanov@gmail.com"));
    assertThat(result.get(0).team, is("WSS"));
    assertThat(result.get(0).name, is("petko"));
  }

  @Test
  public void addTwoEmployees(){
    userRepository.addEmployee(new EmployeeDto("petko.ivanov@gmail.com", "WSS", "petko"));
    userRepository.addEmployee(new EmployeeDto("ivan.petrov@gmail.com", "Incubator", "ivan"));
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.size(), is(2));
    assertThat(result.get(1).email, is("petko.ivanov@gmail.com"));
    assertThat(result.get(0).email, is("ivan.petrov@gmail.com"));
    assertThat(result.get(1).team, is("WSS"));
    assertThat(result.get(0).team, is("Incubator"));
    assertThat(result.get(1).name, is("petko"));
    assertThat(result.get(0).name, is("ivan"));
  }

  @Test
  public void checkForExistingEmployee(){
    userRepository.addEmployee(new EmployeeDto("petar.petrov@gmail.com", "Incubator", "Petar Petrov"));
    userRepository.addEmployee(new EmployeeDto("georgi.georgiev@gmail.com", "WSS", "georgi"));
    boolean isExistingUser = userRepository.checkForExistingUser("georgi.georgiev@gmail.com");
    assertThat(isExistingUser, is(true));
  }

  @Test
  public void checkForNonExistingEmployee(){
    userRepository.addEmployee(new EmployeeDto("petar.petrov@gmail.com", "Incubator", "Petar Petrov"));
    userRepository.addEmployee(new EmployeeDto("georgi.georgiev@gmail.com", "WSS", "georgi"));
    boolean isExistingUser = userRepository.checkForExistingUser("ivan.petrov@gmail.com");
    assertThat(isExistingUser, is(false));
  }
}