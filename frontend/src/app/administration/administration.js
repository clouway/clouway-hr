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
  $scope.employees = '';
  $scope.searchedName = '';

  httpRequest.get('/hr/employer/getAll', '').then(function (data) {
    $scope.employees = data;
  });

  $scope.addEmployee = function () {
    httpRequest.post('/hr/employer/addEmployee', {
      email: $scope.employeeEmail,
      team: $scope.employeeTeam,
      name: $scope.employeeName
    })
            .then(function () {
              $scope.employees.unshift({
                email: $scope.employeeEmail,
                team: $scope.employeeTeam,
                name: $scope.employeeName
              });
              $scope.employeeEmail = '';
              $scope.employeeTeam = '';
              $scope.employeeName = '';
            });
  };

  $scope.editEmployee = function (email, newTeam, newName, position) {
    httpRequest.post('/hr/employer/editEmployee', {
      email: email,
      team: newTeam,
      name: newName
    })
            .then(function () {
              $scope.employees[position] = {
                email: email,
                team: newTeam,
                name: newName
              };
            });
  };

  $scope.sendToModal = function (email, team, name, position) {
    $scope.modalName = name;
    $scope.modalTeam = team;
    $scope.modalEmail = email;
    $scope.modalPosition = position;
  };

  $scope.refreshTeams = function () {
    httpRequest.get('/hr/employer/refreshTeams', '').then(function (data) {
      $scope.employees = data;
    });
  };
}]);