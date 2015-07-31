package com.clouway.hr.adapter.apis.google.organization;

import com.clouway.hr.core.Employee;
import com.clouway.hr.core.EmployeeRepository;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.Group;
import com.google.api.services.admin.directory.model.Member;
import com.google.api.services.admin.directory.model.User;
import com.google.appengine.api.users.UserService;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class OrganizationImpl implements Organization, EmployeeRepository {

  private final DirectoryServiceFactory directoryServiceFactory;
  private final UserService userService;

  @Inject
  public OrganizationImpl(DirectoryServiceFactory directoryServiceFactory, UserService userService) {

    this.directoryServiceFactory = directoryServiceFactory;
    this.userService = userService;
  }

  @Override
  public Set<String> getUserRoles(String email) {

    final Directory directory = directoryServiceFactory.create(email);

    List<Group> userGroups;
    Set<String> roles = Sets.newHashSet();
    String role;

    try {

      userGroups = directory.groups().list().setUserKey(email).execute().getGroups();

      for (Group group : userGroups) {

        role = directory.members().get(group.getEmail(), email).execute().getRole();
        roles.add(role);

      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return roles;
  }

  @Override
  public void editEmployeeTeam(String email, String newTeam) {
    String domain = "@" + userService.getCurrentUser().getEmail().split("@")[1];
    String oldTeam = null;
    oldTeam = findTeam(email);
    Directory directory = directoryServiceFactory.create(userService.getCurrentUser().getEmail());
    try {
      Member member = new Member().setEmail(email);
      directory.members().delete(oldTeam + domain, email).execute();
      directory.members().insert(newTeam + domain, member).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Employee> findAllEmployees() {
    Directory directory = directoryServiceFactory.create(userService.getCurrentUser().getEmail());
    String domain = userService.getCurrentUser().getEmail().split("@")[1];
    List<User> users = null;
    try {
      users = directory.users().list().setDomain(domain).execute().getUsers();
    } catch (IOException e) {
      e.printStackTrace();
    }
    List<Employee> employees = new ArrayList<>();
    for (User user : users) {
      Employee employee = transformToDomain(user);
      employees.add(employee);
    }
    return employees;
  }

  @Override
  public String findTeam(String email) {
    Directory directory = directoryServiceFactory.create(userService.getCurrentUser().getEmail());
    String employeeTeam = null;
    try {
      List<Group> groupList = directory.groups().list().setUserKey(email).execute().getGroups();
      employeeTeam = groupList.get(0).getName();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      return "no team";
    }
    return employeeTeam;
  }

  private Employee transformToDomain(User user) {
    return new Employee(user.getPrimaryEmail(), " ", user.getName().getFullName());
  }


}
