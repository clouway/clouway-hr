package com.clouway.hr.core;

import com.google.appengine.repackaged.com.google.api.client.util.Lists;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationStatus {
  private List<String> statuses = Lists.newArrayList();

  public void add(String type) {
    statuses.add(type);
  }

  public List<String> getStatuses() {
    return statuses;
  }
}
