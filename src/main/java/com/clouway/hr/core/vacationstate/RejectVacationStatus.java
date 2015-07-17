package com.clouway.hr.core.vacationstate;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class RejectVacationStatus implements State {
  @Override
  public void changeStatus(VacationStatus vacationStatus) {

  }

  @Override
  public String asString() {
    return "reject";
  }
}
