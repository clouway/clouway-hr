package com.clouway.hr.adapter.http;

import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

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

  @Get
  @At("/change/:id/type/:status")
  public Reply changeStatus(@Named("id") String id, @Named("status") String status) {
    vacationRepository.updateStatus(Long.parseLong(id), status);

    return Reply.saying().ok();
  }
}
