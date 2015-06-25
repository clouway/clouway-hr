package com.clouway.hr.adapter.db;


import com.vercer.engine.persist.annotation.Child;
import com.vercer.engine.persist.annotation.Key;

import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
 class UserEntity {
 @Key
 private String email;
 private String team;
 private String name;
 private boolean isAdmin;
 @Child
 private List<VacationEntity> vacations;

 public UserEntity(String email, String team, String name , boolean isAdmin) {

  this.email = email;
  this.team = team;
  this.name = name;
  this.isAdmin = isAdmin;
 }

 public UserEntity(List<VacationEntity> vacations) {
  this.vacations = vacations;
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

 public List<VacationEntity> getVacations() {
  return vacations;
 }

 public void setTeam(String team) {
  this.team = team;
 }

 public void setName(String name) {
  this.name = name;
 }
}
