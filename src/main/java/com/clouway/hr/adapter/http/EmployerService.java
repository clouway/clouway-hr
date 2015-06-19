package com.clouway.hr.adapter.http;

import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Put;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/rest/employer")
@Service
public class EmployerService {
  VacationRepository vacationRepository;

  @Inject
  public EmployerService(VacationRepository vacationRepository) {
    this.vacationRepository = vacationRepository;
  }

  @Put
  @At("/change/:id/type/:status")
  public Reply<ResponseMessageDto> changeStatus(@Named("id") String id, @Named("status") String status) {
    vacationRepository.updateStatus(Long.parseLong(id), status);

    return Reply.with(new ResponseMessageDto("success")).as(Json.class);
  }
}
