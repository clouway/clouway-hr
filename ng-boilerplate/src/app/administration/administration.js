/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */

var myApp = angular.module('administration', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
])
        .config(function config($stateProvider) {
          $stateProvider.state('administration', {
            url: '/hr/administration',
            views: {
              "main": {
                controller: 'administrationController',
                templateUrl: 'administration/administration.tpl.html'
              }
            },
            data: {pageTitle: 'Administration'}
          });
        });
myApp.controller('administrationController', ['$scope', 'httpRequest', function ($scope, httpRequest) {
  $scope.employeeEmail = '';
  $scope.employeeTeam = '';
  $scope.employeeName = '';
  $scope.employeeRole = 'false';
  //$scope.employees = '';
  $scope.employees = [{email: 'asd', team: 'asd', name: 'asd', isAdmin: 'true'}];
  $scope.searchedName = '';
  $scope.modalEmployeeRole = 'User';

    httpRequest.get('/hr/employer/getAll', '').then(function (data) {
      $scope.employees = data;
    });

  $scope.addEmployee = function () {
    httpRequest.post('/hr/employer/addEmployee', {email: $scope.employeeEmail, team: $scope.employeeTeam, name: $scope.employeeName, isAdmin: $scope.employeeRole})
            .then(function () {
                          $scope.employees.unshift({
                            email: $scope.employeeEmail,
                            team: $scope.employeeTeam,
                            name: $scope.employeeName,
                            isAdmin: $scope.employeeRole
                          });
                          $scope.employeeEmail = '';
                          $scope.employeeTeam = '';
                          $scope.employeeName = '';
                          $scope.employeeRole = 'false';
            });
  };

  $scope.editEmployee = function (email, newTeam, newName, position) {
    httpRequest.post('/hr/employer/editEmployee', {email: email, team: newTeam, name: newName, isAdmin: $scope.modalEmployeeRole})
            .then(function () {
              $scope.employees[position] = {
                email: email,
                team: newTeam,
                name: newName,
                isAdmin: $scope.modalEmployeeRole
              };
            });
  };

  $scope.deleteEmployee = function (emailToDelete, position) {
    httpRequest.post('/hr/employer/deleteEmployee', {email: emailToDelete})
            .then(function () {
              $scope.employees.splice(position, 1);
            });
  };

  $scope.searchEmployee = function () {
    httpRequest.post('/hr/employer/searchEmployee', {name: $scope.searchedName})
            .then(function (data) {
              $scope.employees = data;
            });
  };

  $scope.sendToModal = function (email, team, name, isAdmin, position) {
    $scope.modalName = name;
    $scope.modalTeam = team;
    $scope.modalEmail = email;
    $scope.modalEmployeeRole = isAdmin.toString();
    $scope.modalPosition = position;
  };
}]);