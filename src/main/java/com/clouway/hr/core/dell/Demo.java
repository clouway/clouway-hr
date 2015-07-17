package com.clouway.hr.core.dell;

import com.clouway.hr.core.vacationstate.ApproveVacationStatus;
import com.clouway.hr.core.vacationstate.RejectVacationStatus;
import com.clouway.hr.core.vacationstate.State;
import com.clouway.hr.core.vacationstate.VacationStatus;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class Demo {
  public static void main(String[] args) {
    VacationStatus status = new VacationStatus();
    State approveVacationStatus = new ApproveVacationStatus();
    State rejectVacationStatus = new RejectVacationStatus();

    status.setState(approveVacationStatus);

    status.setState(rejectVacationStatus);

    System.out.println(status.getState().asString());
  }
}
