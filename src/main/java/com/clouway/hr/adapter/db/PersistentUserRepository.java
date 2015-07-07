package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.EmployeeDto;
import com.clouway.hr.core.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vercer.engine.persist.ObjectDatastore;

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
      UserEntity employee = new UserEntity(employeeDto.email, employeeDto.team, employeeDto.name, employeeDto.isAdmin);
      datastoreProvider.get().store(employee);
  }

  @Override
  public void editEmployee(EmployeeDto editedEmployee) {
    ObjectDatastore datastore = datastoreProvider.get();
    UserEntity employee = datastore.load(UserEntity.class, editedEmployee.email);
    employee.setTeam(editedEmployee.team);
    employee.setName(editedEmployee.name);
    employee.setIsAdmin(editedEmployee.isAdmin);
    datastore.update(employee);
  }

  @Override
  public void deleteEmployee(String employeeEmail) {
    ObjectDatastore datastore = datastoreProvider.get();
    UserEntity employee = datastore.load(UserEntity.class, employeeEmail);
    datastore.delete(employee);
  }

  @Override
  public List<EmployeeDto> findAllEmployees() {
    List<EmployeeDto> users = new ArrayList<>();
    Iterator<UserEntity> userIterator = datastoreProvider.get().find(UserEntity.class);
    while (userIterator.hasNext()) {
      UserEntity employee = userIterator.next();
      users.add(new EmployeeDto(employee.getEmail(), employee.getTeam(), employee.getName(), employee.isAdmin()));
    }
    return users;
  }

  @Override
  public List<EmployeeDto> searchEmployeesByName(String searchedName) {
    List<EmployeeDto> allUsers = findAllEmployees();
    List<EmployeeDto> searchedUsers = new ArrayList<>();
    for (EmployeeDto employee : allUsers) {
      if (employee.name.toLowerCase().contains(searchedName.toLowerCase())) {
        searchedUsers.add(employee);
      }
    }
    return searchedUsers;
  }
}
