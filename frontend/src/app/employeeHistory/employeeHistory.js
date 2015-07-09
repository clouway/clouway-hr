var ang = angular.module('clouwayHr.employeeHistory', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('employeeHistory', {
    url: '/employeeHistory',
    views: {
      "main": {
        controller: 'EmployeeHistoryCtrl',
        templateUrl: 'employeeHistory/employeeHistory.tpl.html'
      }
    },
    data: {pageTitle: 'Home'}
  });
});

ang.controller('EmployeeHistoryCtrl', ['$scope', '$http', 'HttpService', function ($scope, $http, HttpService) {
  $scope.currentTime = new Date().getTime();
  $scope.responseMessage = '';
  $scope.getVacations = function () {
    HttpService.get('/r/history/vacations').then(function (messsage) {
      $scope.responseMessage = messsage;
    }, function (reason) {
      $scope.errorMessage = "some error ocurred";
    });
  };
}]);
