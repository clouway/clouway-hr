package com.clouway.hr.adapter.http;

import com.clouway.hr.core.CurrentDate;
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
  private CurrentDate currentDate;

  @Inject
  public EmployeeService(VacationRepository vacationRepository, CurrentDate currentDate) {
    this.vacationRepository = vacationRepository;
    this.currentDate = currentDate;
  }

  @Post
  @At("/vacationRequest")
  public Reply requestVacation(Request request) {
    VacationRequestDto vacation = request.read(VacationRequestDto.class).as(Json.class);
    Long endDate = vacation.getToDate();
    Long fromDate = vacation.getFromDate();

    //todo have to retrieve from current user
    if (vacation.getUserId() == null) {
      vacation.setUserId("marin@gmail.com");
    }
    if (isNotInRangeRequestDates(vacation)) {
      return replyError();
    }
    vacationRepository.add(vacation);

    return isBiggerOf(endDate, fromDate) ? replyOk() : replyError();
  }

  private Reply<Object> replyError() {
    return Reply.saying().error();
  }

  private Reply<Object> replyOk() {
    return Reply.saying().ok();
  }

  private boolean isNotInRangeRequestDates(VacationRequestDto vacation) {
    Long endDate = vacation.getToDate();
    Long fromDate = vacation.getFromDate();
    Long now = currentDate.getTime();

    return isBiggerOf(now, endDate) || isBiggerOf(now, fromDate);
  }

  private boolean isBiggerOf(Long startDate, Long secondDate) {

    return startDate.compareTo(secondDate) == 1;
  }
}
