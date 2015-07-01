package com.clouway.hr.adapter.http;

import com.clouway.hr.core.IncorrectVacationStatusException;
import com.clouway.hr.core.Status;
import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Put;

import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/r/employer")
@Service
public class EmployerService {
  private VacationRepository vacationRepository;
  private Status statuses;

  @Inject
  public EmployerService(VacationRepository vacationRepository, Provider<Status> statuses) {
    this.vacationRepository = vacationRepository;
    this.statuses = statuses.get();
  }

  @Put
  @At("/vacation/:id/type/:status")
  public Reply<ResponseMessageDto> changeVacationStatus(@Named("id") String id, @Named("status") String status) {

    try {
      vacationRepository.updateStatus(Long.parseLong(id), status);
    } catch (IncorrectVacationStatusException e) {
      return replyWith("incorrect status");
    }

    return replyWith("success");
  }

  @Get
  @At("/vacation/type/pending")
  public Reply getPendingVacationRequest() {

    List<VacationResponseDto> vacations = vacationRepository.getStatus(statuses.getPending());

    return Reply.with(vacations).as(Json.class);
  }

  private Reply<ResponseMessageDto> replyWith(String message) {
    return Reply.with(new ResponseMessageDto(message)).as(Json.class);
  }
}
