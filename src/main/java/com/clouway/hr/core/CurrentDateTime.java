package com.clouway.hr.core;

import java.util.Date;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class CurrentDateTime implements CurrentDate {
  @Override
  public Long getTime() {
    return new Date().getTime();
  }
}
