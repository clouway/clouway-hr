package com.clouway.hr.vacationstate;

import com.clouway.hr.core.vacationstate.ApproveVacationStatus;
import com.clouway.hr.core.vacationstate.State;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationStateModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(State.class).annotatedWith(Names.named("vacationState")).to(ApproveVacationStatus.class);
  }
}
