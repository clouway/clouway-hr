package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
//remove public
public class UserEntity {
  @Key
  private String key;
  private String email;
  private String team;
  private String name;
  private boolean isAdmin;

  public UserEntity(String email, String team, String name, boolean isAdmin) {
    this.key = email;
    this.email = email;
    this.team = team;
    this.name = name;
    this.isAdmin = isAdmin;
  }

  public UserEntity() {
  }

  public String getEmail() {
    return email;
  }

  public String getTeam() {
    return team;
  }

  public String getName() {
    return name;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIsAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  @Override
  public String toString() {
    return "UserEntity{" +
            "email='" + email + '\'' +
            ", team='" + team + '\'' +
            ", name='" + name + '\'' +
            ", isAdmin=" + isAdmin +
            '}';
  }
}
