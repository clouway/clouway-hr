package com.clouway.hr.adapter.http;

import com.clouway.hr.core.CurrentDate;
import com.clouway.hr.core.CurrentDateTime;
import com.clouway.hr.core.Vacation;
import com.clouway.hr.core.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class RandomGenerator {
  @NotNull
  public static EmployeeVacationRequestDto generateVacationRequestDto() {
    return EmployeeVacationRequestDto.newBuilder()
            .userId(generateRandomString(10))
            .dateFrom(generateRandomLong())
            .dateTo(generateRandomLong())
            .description(generateRandomString(10))
            .build();
  }

  public static Vacation generateVacationDo(Long vacationId) {
    return Vacation.newBuilder()
            .vacationId(vacationId)
            .userId(generateRandomString(10))
            .dateFrom(generateRandomLong())
            .dateTo(generateRandomLong())
            .description(generateRandomString(10))
            .build();
  }

  public static Vacation generateVacationDo(Long vacationId, String status) {
    return Vacation.newBuilder()
            .vacationId(vacationId)
            .userId(generateRandomString(10))
            .dateFrom(generateRandomLong())
            .dateTo(generateRandomLong())
            .status(status)
            .description(generateRandomString(10))
            .build();
  }

  public static User generateUser(final boolean isAdmin) {
    return new User() {
      @Override
      public String getEmail() {
        return generateRandomString(5);
      }

      @Override
      public boolean isAdmin() {
        return isAdmin;
      }
    };
  }

  private static String generateRandomString(int size) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int index = 0; index < size; index++) {
      int i = (new Random().nextInt(22)) + 100;
      stringBuilder.append((char) i);
    }
    return stringBuilder.toString();
  }

  private static Long generateRandomLong() {
    CurrentDate currentDate = new CurrentDateTime();
    Long currentTime = currentDate.getTime();

    return currentTime;
  }
}