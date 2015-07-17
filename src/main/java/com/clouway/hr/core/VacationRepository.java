package com.clouway.hr.core;


import com.clouway.hr.core.user.User;
import com.clouway.hr.core.vacationstate.VacationStatus;

import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface VacationRepository {
  void updateStatus(Long id, String status);

  void add(Vacation vacation);

  List<Vacation> get(String userId);

  List<Vacation> getStatus(String vacationType);

  void hide(Long vacationId);

  List<Vacation> getUnHidden(User currentUser);

  void approve(com.clouway.hr.core.vacationstate.VacationStatus vacationStatus);

  void reject(VacationStatus vacationStatus);
}