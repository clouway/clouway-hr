package com.clouway.hr.adapter.frontend.employee;

import com.clouway.hr.adapter.frontend.FakeRequest;
import com.clouway.hr.core.Employee;
import com.clouway.hr.core.EmployeeRepository;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.clouway.hr.test.custom.matchers.SitebricksReplyMatchers.contains;
import static com.clouway.hr.test.custom.matchers.SitebricksReplyMatchers.hasStatusCode;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class EmployeeServiceTest {
  private EmployeeService employeeService;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  EmployeeRepository employeeRepository;

  @Before
  public void setUp() throws Exception {
    employeeService = new EmployeeService(employeeRepository);
  }

  @Test
  public void editEmployeeTeamSuccessfully() {
    final EmployeeDto editedEmployee = new EmployeeDto("tihomir.kehayov@gmail.com", "BSS", "tihomir");
    final Employee employee = employeeService.dtoToDomain(editedEmployee);
    context.checking(new Expectations() {{
      oneOf(employeeRepository).editEmployeeTeam(employee.email, employee.team);
    }});
    Reply<Object> actualReply = employeeService.editTeam(FakeRequest.newRequestWith(editedEmployee));
    assertThat(actualReply, hasStatusCode(200));
  }

  @Test
  public void showAllEmployees() {
    final List<Employee> employees = new ArrayList<>();
    Employee employee = employeeService.dtoToDomain(new EmployeeDto("dimitar.dimitrov", "OSS" ,"@milena-m.com"));
    Employee employee1 = employeeService.dtoToDomain(new EmployeeDto("ivan.petrov", "BSS", "@milena-m.com"));
    employees.add(employee);
    employees.add(employee1);
    List<EmployeeDto> employeeDtos = employeeService.domainToDto(employees);
    context.checking(new Expectations() {{
      oneOf(employeeRepository).findAllEmployees();
      will(returnValue(employees));
    }});
    Reply<List<EmployeeDto>> actualReply = employeeService.showAll();
    assertThat(actualReply, contains(employeeDtos));
  }

  @Test
  public void getEmployeeTeamByEmail() {
    final EmployeeDto editedEmployee = new EmployeeDto("tihomir.kehayov@gmail.com", null, null);
    final String employeeTeam = "OSS";
    context.checking(new Expectations() {{
      oneOf(employeeRepository).findTeam(editedEmployee.getEmail());
      will(returnValue(employeeTeam));
    }});
    Reply<Object> actualReply = employeeService.getTeam(FakeRequest.newRequestWith(editedEmployee));
    assertThat(actualReply, hasStatusCode(200));
    assertThat(actualReply, contains(employeeTeam));
  }
}