package com.clouway.hr.adapter.http;

import com.clouway.hr.core.UserRepository;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/hr/employer")
@Service
public class AdministrationService {
  private final UserRepository userRepository;

  @Inject
  public AdministrationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @At("/addEmployee")
  @Post
  public Reply addEmployee(Request request){
    EmployeeDto employeeDto = request.read(EmployeeDto.class).as(Json.class);
    userRepository.addEmployee(employeeDto);
    return Reply.saying().ok();
  }

  @At("/editEmployee")
  @Post
  public Reply editEmployee(Request request) {
    EmployeeDto editedEmployee = request.read(EmployeeDto.class).as(Json.class);
    userRepository.editEmployeeTeam(editedEmployee);
    return Reply.saying().ok();
  }

  @At("/getAll")
  @Get
  public Reply showEmployees() {
    List<EmployeeDto> allEmployees = userRepository.findAllEmployees();
    return Reply.with(allEmployees).as(Json.class);
  }

  @At("/refreshTeams")
  @Get
  public Reply refreshEmployeeTeams() {
    List<EmployeeDto> employeesWithRefreshedTeams = userRepository.refreshEmployeeTeams();
    return Reply.with(employeesWithRefreshedTeams).as(Json.class);
  }
}
