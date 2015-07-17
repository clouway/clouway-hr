package com.clouway.hr.adapter.http;

import com.clouway.hr.core.CurrentDate;
import com.clouway.hr.core.Vacation;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.user.User;
import com.clouway.hr.core.vacationstate.PendingVacationStatus;
import com.clouway.hr.core.vacationstate.State;
import com.clouway.hr.core.vacationstate.VacationStatus;
import com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */

@At("/r/vacation")
@Service
public class VacationService {
  private final VacationRepository vacationRepository;
  private User currentUser;
  private CurrentDate currentDate;
  private VacationStatus vacationStatus;

  @Inject
  public VacationService(VacationRepository vacationRepository, User currentUser, CurrentDate currentDate, VacationStatus vacationStatus) {
    this.vacationRepository = vacationRepository;
    this.currentUser = currentUser;
    this.currentDate = currentDate;
    this.vacationStatus = vacationStatus;
  }

  @Post
  @At("/:id/type/accept")
  public Reply<EmployeeVacationRequestDto> approveVacation(@Named("id") Long id) {
    vacationStatus.id = id;
    vacationRepository.approve(vacationStatus);

    return replyWith("success");
  }

  @Post
  @At("/:id/type/reject")
  public Reply<EmployeeVacationRequestDto> rejectVacation(@Named("id") Long id) {
    vacationStatus.id = id;
    vacationRepository.reject(vacationStatus);

    return replyWith("success");
  }

  @Get
  @At("/changedStatus")
  public Reply<List<EmployeeVacationRequestDto>> getChangedStatuses() {
    List<Vacation> vacations = vacationRepository.get(currentUser.getEmail());
    List<EmployeeVacationRequestDto> employeeVacationRequestDtos = vacationToDto(vacations);

    return Reply.with(employeeVacationRequestDtos).as(Json.class);
  }

  @Post
  @At("/hide/:vacationId")
  public Reply hideVacation(@Named("vacationId") Long vacationId) {
    vacationRepository.hide(vacationId);

    return Reply.saying().ok();
  }

  @Get
  @At("/unhidden")
  public Reply<List<EmployeeVacationRequestDto>> getUnHidden() {
    List<Vacation> unHiddenVacations = vacationRepository.getUnHidden(currentUser);
    List<EmployeeVacationRequestDto> employeeVacationRequestDtos = vacationToDto(unHiddenVacations);

    return Reply.with(employeeVacationRequestDtos).as(Json.class);
  }

  @Get
  @At("/type/pending")
  public Reply getPendingVacationRequest() {
    State pendingVacationStatus = new PendingVacationStatus();
    vacationStatus.setState(pendingVacationStatus);
    List<Vacation> vacations = vacationRepository.getStatus(pendingVacationStatus.asString());
    List<EmployeeVacationRequestDto> employeeVacationRequestDto = vacationToDto(vacations);

    return Reply.with(employeeVacationRequestDto).as(Json.class);
  }

  @Post
  @At("/vacationRequest")
  public Reply requestVacation(Request request) {
    EmployeeVacationRequestDto employeeVacationRequestDto = request.read(EmployeeVacationRequestDto.class).as(Json.class);

    Vacation vacation = dtoToVacation(employeeVacationRequestDto);
    if (!vacation.isValid(employeeVacationRequestDto, currentDate)) {
      return replyError();
    }
    vacationRepository.add(vacation);

    return replyOk();
  }

  private List<EmployeeVacationRequestDto> vacationToDto(List<Vacation> vacations) {
    ArrayList<EmployeeVacationRequestDto> employeeVacationRequestDtos = Lists.newArrayList();
    for (Vacation vacation : vacations) {
      EmployeeVacationRequestDto employeeVacationRequestDto = EmployeeVacationRequestDto.newBuilder()
              .userId(vacation.getUserId())
              .isHidden(vacation.isHidden())
              .status(vacation.getStatus())
              .description(vacation.getDescription())
              .dateFrom(vacation.getDateFrom())
              .dateTo(vacation.getDateTo())
              .vacationId(vacation.getVacationId())
              .build();

      employeeVacationRequestDtos.add(employeeVacationRequestDto);
    }
    return employeeVacationRequestDtos;
  }

  private Vacation dtoToVacation(EmployeeVacationRequestDto dto) {
    return Vacation.newBuilder()
            .userId(currentUser.getEmail())
            .description(dto.getDescription())
            .dateTo(dto.getToDate())
            .dateFrom(dto.getFromDate())
            .build();
  }

  private Reply<EmployeeVacationRequestDto> replyWith(String message) {
    return Reply.with(EmployeeVacationRequestDto.newBuilder().description(message).build()).as(Json.class);
  }

  private Reply<Object> replyError() {
    return Reply.saying().error();
  }

  private Reply<Object> replyOk() {
    return Reply.saying().ok();
  }
}