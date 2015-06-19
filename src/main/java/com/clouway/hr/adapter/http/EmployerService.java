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
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/rest/employer")
@Service
public class EmployerService {
  private VacationRepository vacationRepository;

  @Inject
  public EmployerService(VacationRepository vacationRepository) {
    this.vacationRepository = vacationRepository;
  }

  @Put
  @At("/vacation/:id/type/:status")
  public Reply<ResponseMessageDto> changeVacationStatus(@Named("id") String id, @Named("status") String status) {
    try {
      vacationRepository.updateStatus(Long.parseLong(id), status);
    } catch (IncorrectVacationStatusException e) {
      return Reply.with(new ResponseMessageDto("incorrect status")).as(Json.class);
    }

    return Reply.with(new ResponseMessageDto("success")).as(Json.class);
  }
}
