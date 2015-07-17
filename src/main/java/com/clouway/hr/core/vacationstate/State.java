package com.clouway.hr.core.vacationstate;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface State {
  void changeStatus(VacationStatus vacationStatus);

  String asString();
}
