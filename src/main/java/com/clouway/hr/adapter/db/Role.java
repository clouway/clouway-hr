package com.clouway.hr.adapter.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class Role {
  @Key
  private long roleId;
  private String roleName;

  public Role(long roleId, String roleName) {
    this.roleId = roleId;
    this.roleName = roleName;
  }

  public Role() {
  }

  public long getRoleId() {
    return roleId;
  }

  public String getRoleName() {
    return roleName;
  }
}
