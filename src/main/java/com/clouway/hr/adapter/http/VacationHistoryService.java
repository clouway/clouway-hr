package com.clouway.hr.adapter.http;

import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/r/history")
@Service
public class VacationHistoryService {
  private User currentUser;
  private VacationRepository vacationRepositoryProvider;

  @Inject
  public VacationHistoryService(VacationRepository vacationRepositoryProvider, Provider<User> currentUser) {
    this.vacationRepositoryProvider = vacationRepositoryProvider;
    this.currentUser = currentUser.get();
  }

  @Get
  @At("/vacations")
  public Reply<List<VacationResponseDto>> getVacations() {
    List<VacationResponseDto> historyList = vacationRepositoryProvider.getHistory(currentUser);

    return Reply.with(historyList).as(Json.class);
  }
}
