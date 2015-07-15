package com.clouway.hr.adapter.http;

import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/rest/employer/")
@Service
public class EmployerService {
  private final VacationRepository vacationRepository;

  @Inject
  public EmployerService(VacationRepository vacationRepository) {
    this.vacationRepository = vacationRepository;
  }


  public Reply approveVacation(String id, String status) {

    vacationRepository.updateStatus(Long.parseLong(id), status);

    return Reply.saying().ok();
  }
}
