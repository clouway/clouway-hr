/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */

var myApp = angular.module('hr.administration', [
  'ui.router',
  'ui.bootstrap',
  'angular-loading-bar'
])
        .config(function config($stateProvider) {
          $stateProvider.state('administration', {
            url: '/administration',
            views: {
              "main": {
                controller: 'administrationController',
                templateUrl: 'administration/administration.tpl.html'
              }
            },
            data: {pageTitle: 'Administration'}
          });
        });

myApp.constant('ENDPOINT', (function() {
  var endpoint = '/employee/';
  return {
    GET_ALL: endpoint + 'getAll',
    EDIT_TEAM: endpoint + 'editTeam',
    GET_TEAM: endpoint + 'getTeam'
  };
})());
myApp.controller('administrationController', ['$scope', 'httpRequest', 'ENDPOINT', function ($scope, httpRequest, ENDPOINT) {
  $scope.employees = '';

  httpRequest.get(ENDPOINT.GET_ALL, '').then(function (data) {
    $scope.employees = data;
  });

  $scope.editEmployee = function (email, newTeam, name) {
    httpRequest.post(ENDPOINT.EDIT_TEAM, {
      email: email,
      team: newTeam,
      name: name
    });
  };

  $scope.employeeInformation = function (email, name) {
    $scope.employeeTeam = '';
    $scope.employeeName = name;
    $scope.employeeEmail = email;
    $scope.getEmployeeTeam();
  };

  $scope.getEmployeeTeam = function () {
    httpRequest.post(ENDPOINT.GET_TEAM, {email: $scope.employeeEmail}).then(function (data) {
      $scope.employeeTeam = data;
    });
  };
}]);