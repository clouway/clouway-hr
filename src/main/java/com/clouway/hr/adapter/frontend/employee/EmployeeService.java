package com.clouway.hr.adapter.frontend.employee;

import com.clouway.hr.core.Employee;
import com.clouway.hr.core.EmployeeRepository;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/employee")
@Service
public class EmployeeService {


  private final EmployeeRepository employeeRepository;

  @Inject
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @At("/editTeam")
  @Post
  public Reply editTeam(Request request) {
    EmployeeDto employeeDto = request.read(EmployeeDto.class).as(Json.class);
    Employee employee = dtoToDomain(employeeDto);
    String email = employee.email;
    String newTeam = employee.team;
    employeeRepository.editEmployeeTeam(email, newTeam);
    return Reply.saying().ok();
  }

  @At("/getAll")
  @Get
  public Reply showAll() {
    List<Employee> allEmployees = null;
    allEmployees = employeeRepository.findAllEmployees();
    List<EmployeeDto> employees = domainToDto(allEmployees);
    return Reply.with(employees).as(Json.class);
  }

  @At("/getTeam")
  @Post
  public Reply getTeam(Request request) {
    String email = request.read(EmployeeDto.class).as(Json.class).getEmail();
    String employeeTeam = employeeRepository.findTeam(email);
    return Reply.with(employeeTeam).ok();
  }

  protected Employee dtoToDomain(EmployeeDto employeeDto) {
    return new Employee(employeeDto.getEmail(), employeeDto.getTeam(), employeeDto.getName());
  }

  protected List<EmployeeDto> domainToDto(List<Employee> allEmployees) {
    List<EmployeeDto> employees = new ArrayList<>();
    for (Employee employee : allEmployees) {
      employees.add(new EmployeeDto(employee.email, employee.team, employee.name));
    }
    return employees;
  }
}
