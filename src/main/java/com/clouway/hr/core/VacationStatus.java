package com.clouway.hr.core;

import com.google.appengine.repackaged.com.google.api.client.util.Lists;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationStatus implements Status {
  private int status;
  private List<String> statuses = Lists.newArrayList();

  public void add(String type) {
    statuses.add(type);
  }

  public List<String> getStatuses() {
    return statuses;
  }

  @Override
  public int accept() {
    return 0;
  }

  @Override
  public int reject() {
    return 1;
  }

  @Override
  public int pending() {
    return 2;
  }
}
