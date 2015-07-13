package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.DirectoryDto;
import com.clouway.hr.adapter.http.EmployeeDto;
import com.clouway.hr.core.UserRepository;
import com.google.api.services.admin.directory.model.Member;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vercer.engine.persist.ObjectDatastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
  private final Provider<ObjectDatastore> datastoreProvider;

  @Inject
  public PersistentUserRepository(Provider<ObjectDatastore> datastoreProvider) {
    this.datastoreProvider = datastoreProvider;
  }

  @Override
  public void addEmployee(EmployeeDto employeeDto) {
      UserEntity employee = new UserEntity(employeeDto.email, employeeDto.team, employeeDto.name);
      datastoreProvider.get().store(employee);
  }

  @Override
  public void editEmployeeTeam(EmployeeDto editedEmployee) {
    ObjectDatastore datastore = datastoreProvider.get();
    UserEntity employee = datastore.load(UserEntity.class, editedEmployee.email);
    editEmployeeTeamInGoogleApps(employee.getEmail(), employee.getTeam(), editedEmployee.team);
    employee.setTeam(editedEmployee.team);
    datastore.update(employee);
  }

  @Override
  public List<EmployeeDto> findAllEmployees() {
    List<EmployeeDto> users = new ArrayList<>();
    Iterator<UserEntity> userIterator = datastoreProvider.get().find(UserEntity.class);
    while (userIterator.hasNext()) {
      UserEntity employee = userIterator.next();
      users.add(new EmployeeDto(employee.getEmail(), employee.getTeam(), employee.getName()));
    }
    return users;
  }

  @Override
  public boolean checkForExistingUser(String email) {
    Iterator<UserEntity> user = datastoreProvider.get().find().type(UserEntity.class).addFilter("email", FilterOperator.EQUAL, email).returnResultsNow();
    return user.hasNext();
  }

  @Override
  public List<EmployeeDto> refreshEmployeeTeams() {
    List<EmployeeDto> employees = findAllEmployees();
    for (EmployeeDto employee : employees){
      try {
        employee.team = DirectoryDto.directory.groups().list().setUserKey(employee.email).execute().getGroups().get(0).getName();
        refreshTeamInDb(employee);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return employees;
  }

  private void editEmployeeTeamInGoogleApps(String email, String oldTeam, String newTeam){
    String domain = "@milena-m.com";
    try {
      Member member = DirectoryDto.directory.members().get(oldTeam + domain, email).execute();
      DirectoryDto.directory.members().insert(newTeam + domain, member).execute();
      DirectoryDto.directory.members().delete(oldTeam + domain, email).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void refreshTeamInDb(EmployeeDto editedEmployee){
    ObjectDatastore datastore = datastoreProvider.get();
    UserEntity employee = datastore.load(UserEntity.class, editedEmployee.email);
    employee.setTeam(editedEmployee.team);
    datastore.update(employee);
  }
}
