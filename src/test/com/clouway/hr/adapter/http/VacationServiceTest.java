package com.clouway.hr.adapter.http;

import com.clouway.hr.core.CurrentDate;
import com.clouway.hr.core.Vacation;
import com.clouway.hr.core.VacationRepository;
import com.clouway.hr.core.user.User;
import com.clouway.hr.core.vacationstate.VacationStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.Lists;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.clouway.hr.adapter.frontend.FakeRequest.newRequestWith;
import static com.clouway.hr.adapter.http.RandomGenerator.generateVacationDo;
import static com.clouway.hr.test.custom.matchers.SitebricksReplyMatchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationServiceTest {
  @Mock
  public VacationRepository vacationRepository;
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private VacationService vacationService;
  private User user;
  @Mock
  private CurrentDate currentDate;

  @Before
  public void setUp() {
    helper.setUp();

    user = new User() {
      @Override
      public String getEmail() {
        return "ivan@gmail.com";
      }

      @Override
      public boolean isAdmin() {
        return false;
      }
    };

    VacationStatus vacationStatus = new VacationStatus();
    vacationService = new VacationService(vacationRepository, user, currentDate, vacationStatus);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void addVacationRequestAndGetPendingRequest() {
    final List<Vacation> vacations = Lists.newArrayList();
    vacations.add(Vacation.newBuilder()
            .status("rejected")
            .dateFrom(2L)
            .dateTo(10L)
            .description("moon vacation")
            .build());

    context.checking(new Expectations() {{
      oneOf(vacationRepository).get("ivan@gmail.com");
      will(returnValue(vacations));
    }});
    List<EmployeeVacationRequestDto> expected = vacationToDto(vacations);
    Reply<List<EmployeeVacationRequestDto>> actual = vacationService.getChangedStatuses();

    assertThat(actual, contains(expected));
  }

  @Test
  public void hideVacation() {
    context.checking(new Expectations() {{
      oneOf(vacationRepository).hide(2L);
    }});
    Reply<Object> reply = vacationService.hideVacation(2L);

    assertThat(reply, is(Reply.saying().ok()));
  }

  @Test
  public void retrieveNotHiddenVacations() {
    final List<Vacation> vacations = Lists.newArrayList();
    vacations.add(Vacation.newBuilder()
            .isHidden(false)
            .build());

    context.checking(new Expectations() {{
      oneOf(vacationRepository).getUnHidden(user);
      will(returnValue(vacations));
    }});
    List<EmployeeVacationRequestDto> expected = vacationToDto(vacations);
    Reply<List<EmployeeVacationRequestDto>> actual = vacationService.getUnHidden();

    assertThat(actual, contains(expected));
  }

  @Test
  public void requestVacation() {
    EmployeeVacationRequestDto vacationRequestDto = EmployeeVacationRequestDto.newBuilder()
            .userId("ivan@gmail.com")
            .dateFrom(30L)
            .dateTo(40L)
            .description("some")
            .build();

    Vacation vacation = Vacation.newBuilder()
            .description("some")
            .userId("ivan@gmail.com")
            .dateFrom(30L)
            .dateTo(40L)
            .build();

    mockWithDateOf(20L, vacation);

    assertThat(requestVacations(newRequestWith(vacationRequestDto)), is(Reply.saying().ok()));
  }

  @Test
  public void endDateVacationBiggerThanFromDate() {
    EmployeeVacationRequestDto vacationRequestDto = EmployeeVacationRequestDto.newBuilder()
            .userId("ivan@gmail.com")
            .dateFrom(70L)
            .dateTo(60L)
            .build();
    mockWithDateOf(50L);

    assertThat(requestVacations(newRequestWith(vacationRequestDto)), is(Reply.saying().error()));
  }

  @Test
  public void endDateVacationLessThanCurrentDate() {
    EmployeeVacationRequestDto vacationRequestDto = EmployeeVacationRequestDto.newBuilder()
            .userId("ivan@gmail.com")
            .dateFrom(10L)
            .dateTo(40L)
            .build();
    mockWithDateOf(50L);

    assertThat(requestVacations(newRequestWith(vacationRequestDto)), is(Reply.saying().error()));
  }

  @Test
  public void startDateVacationLessThanCurrentDate() {
    EmployeeVacationRequestDto vacationRequestDto = EmployeeVacationRequestDto.newBuilder()
            .userId("ivan@gmail.com")
            .dateFrom(10L)
            .dateTo(160L)
            .build();
    mockWithDateOf(40L);

    assertThat(requestVacations(newRequestWith(vacationRequestDto)), is(Reply.saying().error()));
  }

  @Test
  public void approveVacation() {
    final VacationStatus vacationStatus = new VacationStatus();
    vacationStatus.id = 1L;
    context.checking(new Expectations() {{
      oneOf(vacationRepository).approve(vacationStatus);
    }});
    Reply<EmployeeVacationRequestDto> actual = vacationService.approveVacation(vacationStatus.id);
    EmployeeVacationRequestDto expected = EmployeeVacationRequestDto.newBuilder().description("success").build();

    assertThat(actual, contains(expected));
  }

  @Test
  public void getPendingVacations() {
    Vacation vacation = generateVacationDo(1L, "pending");
    final List<Vacation> vacations = Lists.newArrayList();
    vacations.add(vacation);

    context.checking(new Expectations() {{
      oneOf(vacationRepository).getStatus("pending");
      will(returnValue(vacations));
    }});

    Reply actual = vacationService.getPendingVacationRequest();
    List<EmployeeVacationRequestDto> expected = vacationToDto(vacations);

    assertThat(actual, contains(expected));
  }

  @Test
  public void rejectVacation() {
    final VacationStatus vacationStatus = new VacationStatus();
    vacationStatus.id = 1L;
    context.checking(new Expectations() {{
      oneOf(vacationRepository).reject(vacationStatus);
    }});
    Reply<EmployeeVacationRequestDto> actual = vacationService.rejectVacation(vacationStatus.id);
    EmployeeVacationRequestDto expected = EmployeeVacationRequestDto.newBuilder().description("success").build();

    assertThat(actual, contains(expected));
  }

  private Reply<Object> requestVacations(Request fakeVacationRequest) {
    return vacationService.requestVacation(fakeVacationRequest);
  }

  private List<EmployeeVacationRequestDto> vacationToDto(List<Vacation> vacations) {
    ArrayList<EmployeeVacationRequestDto> employeeVacationRequestDtos = com.google.api.client.util.Lists.newArrayList();
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

  private void mockWithDateOf(final Long date) {
    context.checking(new Expectations() {{
      oneOf(currentDate).getTime();
      will(returnValue(date));
    }});
  }

  private void mockWithDateOf(final Long date, final Vacation vacation) {
    context.checking(new Expectations() {{
      oneOf(currentDate).getTime();
      will(returnValue(date));
      oneOf(vacationRepository).add(vacation);
    }});
  }
}