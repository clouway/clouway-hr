package com.clouway.hr.adapter.http;

import com.clouway.hr.core.User;
import com.clouway.hr.core.VacationRepository;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import org.jetbrains.annotations.NotNull;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.clouway.hr.adapter.http.ReplyMatcher.contains;
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
  private User user;
  private VacationService vacationService;

  @Before
  public void setUp() {
    helper.setUp();
    user = new User() {
      @Override
      public String getEmail() {
        return "george_washington@gmail.com";
      }

      @Override
      public boolean isAdmin() {
        return false;
      }
    };

    vacationService = new VacationService(vacationRepository, user);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void changeVacationStatus() {
    final Long id = 1l;

    context.checking(new Expectations() {{
      oneOf(vacationRepository).updateStatus(id, "approved");
    }});

    Reply<ResponseMessageDto> actualReply = vacationService.changeVacationStatus(id.toString(), "approved");
    Reply<ResponseMessageDto> success = Reply.with(new ResponseMessageDto("success")).as(Json.class);

    assertThat(actualReply, is(success));
  }

  @Test
  public void addVacationRequestAndGetPendingRequest() {
    final List<VacationResponseDto> vacationResponseDtos = Lists.newArrayList();
    vacationResponseDtos.add(VacationResponseDto.newBuilder()
            .status("rejected")
            .dateFrom(2L)
            .dateTo(10L)
            .description("moon vacation")
            .build());

    context.checking(new Expectations() {{
      oneOf(vacationRepository).get("george_washington@gmail.com");
      will(returnValue(vacationResponseDtos));
    }});
    Reply<List<VacationResponseDto>> actual = vacationService.getChangedStatuses();

    assertThat(actual, contains(vacationResponseDtos));
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
    final List<VacationResponseDto> vacations = Lists.newArrayList();
    vacations.add(VacationResponseDto.newBuilder()
            .isHidden(false)
            .build());

    context.checking(new Expectations() {{
      oneOf(vacationRepository).getUnHidden(user);
      will(returnValue(vacations));
    }});
    Reply<List<VacationResponseDto>> actual = vacationService.getUnHidden();

    assertThat(actual, contains(vacations));
  }
}