package com.clouway.hr.adapter.http;

import com.clouway.hr.core.CurrentDate;
import com.clouway.hr.core.CurrentDateTime;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class RandomGenerator {
  @NotNull
  public static VacationRequestDto generateVacationRequestDto() {
    return VacationRequestDto.newBuilder()
            .userId(generateRandomString(10))
            .fromDate(generateRandomLong())
            .toDate(generateRandomLong())
            .description(generateRandomString(10))
            .build();
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