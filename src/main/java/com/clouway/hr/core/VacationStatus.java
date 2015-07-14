package com.clouway.hr.core;

import com.google.appengine.repackaged.com.google.api.client.util.Lists;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationStatus implements Status {
  private final String acceptMessage;
  private final String pendingMessage;
  private final String rejectMessage;

  public VacationStatus(String acceptMessage, String pendingMessage, String rejectMessage) {
    this.acceptMessage = acceptMessage;
    this.pendingMessage = pendingMessage;
    this.rejectMessage = rejectMessage;
  }

  @Override
  public String getAccept() {
    return acceptMessage;
  }

  @Override
  public String getReject() {
    return rejectMessage;
  }

  @Override
  public String getPending() {
    return pendingMessage;
  }
}
