package com.clouway.hr.adapter.http;

import com.clouway.hr.core.IncorrectVacationStatusException;
import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Put;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */

@At("/r/vacation")
@Service
public class VacationService {
  private final VacationRepository vacationRepository;
  private User currentUser;

  @Inject
  public VacationService(VacationRepository vacationRepository, User currentUser) {
    this.vacationRepository = vacationRepository;
    this.currentUser = currentUser;
  }

  @Put
  @At("/:id/type/:status")
  public Reply<ResponseMessageDto> changeVacationStatus(@Named("id") String id, @Named("status") String status) {

    try {
      vacationRepository.updateStatus(Long.parseLong(id), status);
    } catch (IncorrectVacationStatusException e) {
      return replyWith("incorrect status");
    }

    return replyWith("success");
  }

  @Get
  @At("/changedStatus")
  public Reply<List<VacationResponseDto>> getChangedStatuses() {
    List<VacationResponseDto> vacationResponseDtos = vacationRepository.get(currentUser.getEmail());

    return Reply.with(vacationResponseDtos).as(Json.class);
  }

  private Reply<ResponseMessageDto> replyWith(String message) {
    return Reply.with(new ResponseMessageDto(message)).as(Json.class);
  }

  @Put
  @At("/hide/:vacationId")
  public Reply hideVacation(@Named("vacationId") Long vacationId) {
    vacationRepository.hide(vacationId);

    return Reply.saying().ok();
  }

  @Get
  @At("/unhidden")
  public Reply<List<VacationResponseDto>> getUnHidden() {
    List<VacationResponseDto> unHidden = vacationRepository.getUnHidden(currentUser);

    return Reply.with(unHidden).as(Json.class);
  }
}
