package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class User {
  @Key
  private long userId;
  private String username;
  private long teamId;
  private int daysLeft;
  private String role;

  public User(long userId, String username, long teamId, int daysLeft, String role) {
    this.userId = userId;
    this.username = username;
    this.teamId = teamId;
    this.daysLeft = daysLeft;
    this.role = role;
  }

  public User() {
  }

  public long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public long getTeamId() {
    return teamId;
  }

  public int getDaysLeft() {
    return daysLeft;
  }

  public String getRole() {
    return role;
  }
}
