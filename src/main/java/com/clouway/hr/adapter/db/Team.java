package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class Team {
  @Key
  private long teamId;
  private String teamName;

  public Team(long teamId, String teamName) {
    this.teamId = teamId;
    this.teamName = teamName;
  }

  public Team() {
  }

  public long getTeamId() {
    return teamId;
  }

  public String getTeamName() {
    return teamName;
  }
}
