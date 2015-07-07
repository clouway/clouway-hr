package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.EmployeeDto;
import com.clouway.hr.core.UserRepository;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
  public void addEmployeeAsUser() {
    userRepository.addEmployee(new EmployeeDto("petko.ivanov@gmail.com", "WSS", "petko", false));
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.size(), is(1));
    assertThat(result.get(0).email, is("petko.ivanov@gmail.com"));
    assertThat(result.get(0).team, is("WSS"));
    assertThat(result.get(0).name, is("petko"));
    assertThat(result.get(0).isAdmin, is(false));
  }

  @Test
  public void addEmployeeAsAdministrator(){
    userRepository.addEmployee(new EmployeeDto("petko.ivanov@gmail.com", "WSS", "petko", true));
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.size(), is(1));
    assertThat(result.get(0).email, is("petko.ivanov@gmail.com"));
    assertThat(result.get(0).team, is("WSS"));
    assertThat(result.get(0).name, is("petko"));
    assertThat(result.get(0).isAdmin, is(true));
  }

  @Test
  public void addTwoEmployees(){
    userRepository.addEmployee(new EmployeeDto("petko.ivanov@gmail.com", "WSS", "petko", false));
    userRepository.addEmployee(new EmployeeDto("ivan.petrov@gmail.com", "Incubator", "ivan", false));
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
  public void editEmployeeTeam() {
    EmployeeDto employee = new EmployeeDto("petar.georgiev@gmail.com", "BSS", "petar", false);
    userRepository.addEmployee(employee);
    employee.team = "Incubator";
    userRepository.editEmployee(employee);
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.get(0).team, is("Incubator"));
  }

  @Test
  public void editEmployeeName(){
    EmployeeDto employee = new EmployeeDto("evgeni.petrov@gmail.com", "OSS", "evgeasdv", false);
    userRepository.addEmployee(employee);
    employee.name = "evgeni";
    userRepository.editEmployee(employee);
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.get(0).name, is("evgeni"));
  }

  @Test
  public void editEmployeeNameAndTeam(){
    EmployeeDto employee = new EmployeeDto("georgi.petrov@gmail.com", "sdga", "georgdsfig", false);
    userRepository.addEmployee(employee);
    employee.team = "OSS";
    employee.name = "georgi";
    userRepository.editEmployee(employee);
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.get(0).name, is("georgi"));
    assertThat(result.get(0).team, is("OSS"));
  }

  @Test
  public void editEmployeeRole(){
    EmployeeDto employee = new EmployeeDto("qnko.georgiev@gmail.com", "OSS", "qnko", false);
    userRepository.addEmployee(employee);
    employee.isAdmin = true;
    userRepository.editEmployee(employee);
    List<EmployeeDto> result = userRepository.findAllEmployees();
    assertThat(result.get(0).isAdmin, is(true));
  }

  @Test
  public void deleteEmployee(){
    String employeeEmail = "georgi.ivanov@gmail.com";
    EmployeeDto employee = new EmployeeDto(employeeEmail, "BSS", "georgi", false);
    userRepository.addEmployee(employee);
    userRepository.deleteEmployee(employeeEmail);
    assertThat(userRepository.findAllEmployees().size(), is(0));
  }

  @Test
  public void searchEmployeeByName(){
    userRepository.addEmployee(new EmployeeDto("ivan.petrov@gmail.com", "Incubator", "ivan", false));
    userRepository.addEmployee(new EmployeeDto("georgi.georgiev@gmail.com", "WSS", "georgi", false));
    List<EmployeeDto> result = userRepository.searchEmployeesByName("georgi");
    assertThat(result.size(), is(1));
    assertThat(result.get(0).email, is("georgi.georgiev@gmail.com"));
    assertThat(result.get(0).team, is("WSS"));
    assertThat(result.get(0).name, is("georgi"));
  }

  @Test
  public void searchForTwoEmployeesWithSameNames(){
    userRepository.addEmployee(new EmployeeDto("ivan.petrov@gmail.com", "Incubator", "ivan", false));
    userRepository.addEmployee(new EmployeeDto("petar.ivanov@gmail.com", "BSS", "petar", false));
    userRepository.addEmployee(new EmployeeDto("petko.georgiev@gmail.com", "OSS", "petko", false));
    List<EmployeeDto> result = userRepository.searchEmployeesByName("pet");
    assertThat(result.size(), is(2));
    assertThat(result.get(0).email, is("petar.ivanov@gmail.com"));
    assertThat(result.get(1).email, is("petko.georgiev@gmail.com"));
    assertThat(result.get(0).team, is("BSS"));
    assertThat(result.get(1).team, is("OSS"));
    assertThat(result.get(0).name, is("petar"));
    assertThat(result.get(1).name, is("petko"));
  }

  @Test
  public void searchForEmployeeIgnoringUpperCases(){
    userRepository.addEmployee(new EmployeeDto("petar.petrov@gmail.com", "Incubator", "Petar Petrov", false));
    userRepository.addEmployee(new EmployeeDto("georgi.georgiev@gmail.com", "WSS", "georgi", false));
    List<EmployeeDto> result = userRepository.searchEmployeesByName("petar");
    assertThat(result.size(), is(1));
    assertThat(result.get(0).email, is("petar.petrov@gmail.com"));
    assertThat(result.get(0).team, is("Incubator"));
    assertThat(result.get(0).name, is("Petar Petrov"));
  }
}