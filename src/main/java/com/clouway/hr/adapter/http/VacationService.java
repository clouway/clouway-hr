package com.clouway.hr.adapter.http;

import com.clouway.hr.core.IncorrectVacationStatusException;
import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Put;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */

@At("/r/vacation")
@Service
public class VacationService {
  private final VacationRepository vacationRepository;

  @Inject
  public VacationService(VacationRepository vacationRepository) {
    this.vacationRepository = vacationRepository;
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

  private Reply<ResponseMessageDto> replyWith(String message) {
    return Reply.with(new ResponseMessageDto(message)).as(Json.class);
  }
}
