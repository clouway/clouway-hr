package com.clouway.hr.core.vacationstate;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ApproveVacationStatus implements State {
  @Override
  public void changeStatus(VacationStatus vacationStatus) {
    vacationStatus.setState(this);
  }

  @Override
  public String asString() {
    return "approve";
  }
}
