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
@At("/r/employee")
@Service
public class EmployeeService {
  private final VacationRepository vacationRepository;

  @Inject
  public EmployeeService(VacationRepository vacationRepository) {
    this.vacationRepository = vacationRepository;
  }

  @Post
  @At("/vacationRequest")
  public Reply requestVacation(Request request) {
    VacationRequestDto vacation = request.read(VacationRequestDto.class).as(Json.class);
    //todo have to retrieve from current user
    if (vacation.getUserId() == null) {
      vacation.setUserId("ivan@gmail.com");
    }
    vacationRepository.add(vacation);

    return vacation.getToDate() > vacation.getFromDate() ? Reply.saying().ok() : Reply.saying().error();
  }
}
