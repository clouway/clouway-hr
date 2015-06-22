package com.clouway.hr.adapter.http;

import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/rest/employee")
@Service
public class EmployeeService {
  private final VacationRepository vacationRepository;

  @Inject
  public EmployeeService(VacationRepository vacationRepository) {
    this.vacationRepository = vacationRepository;
  }

  @Post
  @At("/vacation-request")
  public Reply requestVacation(Request request) {
    VacationRequestDto vacation = request.read(VacationRequestDto.class).as(Json.class);

    vacationRepository.add(vacation);

    return Reply.saying().ok();
  }
}
