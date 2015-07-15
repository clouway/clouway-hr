package com.clouway.hr.adapter.http;

import com.clouway.hr.core.UserRepository;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.clouway.hr.adapter.http.ReplyMatcher.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class AdministrationServiceTest {
  private AdministrationService administrationService;
  private FakeRequest fakeRequest;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  UserRepository userRepository;

  @Before
  public void setUp() throws Exception {
    administrationService = new AdministrationService(userRepository);
    fakeRequest = new FakeRequest();
  }

  @Test
  public void addEmployee(){
    final EmployeeDto employee = new EmployeeDto("ivan.genchev@gmail.com", "OSS", "ivan");
    fakeRequest.dummyObject = employee;
    context.checking(new Expectations() {{
      oneOf(userRepository).addEmployee(employee);
    }});
    Reply<Object> actualReply = administrationService.addEmployee(fakeRequest);
    assertThat(actualReply, is(Reply.saying().ok()));
  }

  @Test
  public void addAnotherEmployee(){
    final EmployeeDto employee = new EmployeeDto("marian.zlatev@gmail.com", "Incubator", "marian");
    fakeRequest.dummyObject = employee;
    context.checking(new Expectations(){{
      oneOf(userRepository).addEmployee(employee);
    }});
    Reply<Object> actualReply = administrationService.addEmployee(fakeRequest);
    assertThat(actualReply, is(Reply.saying().ok()));
  }

  @Test
  public void editEmployeeTeam(){
    final EmployeeDto editedEmployee = new EmployeeDto("tihomir.kehayov@gmail.com", "BSS", "tihomir");
    fakeRequest.dummyObject = editedEmployee;
    context.checking(new Expectations(){{
      oneOf(userRepository).editEmployeeTeam(editedEmployee);
    }});
    Reply<Object> actualReply = administrationService.editEmployee(fakeRequest);
    assertThat(actualReply, is(Reply.saying().ok()));
  }

  @Test
  public void showAllEmployees(){
    final List<EmployeeDto> employees = new ArrayList<>();
    employees.add(new EmployeeDto("ivan.petrov@gmail.com", "WSS", "ivan"));
    employees.add(new EmployeeDto("petar.georgiev@gmail.com", "BSS", "petar"));
    context.checking(new Expectations() {{
      oneOf(userRepository).findAllEmployees();
      will(returnValue(employees));
    }});
    Reply<List<EmployeeDto>> actualReply = administrationService.showEmployees();
    assertThat(actualReply, contains(employees));
  }
}