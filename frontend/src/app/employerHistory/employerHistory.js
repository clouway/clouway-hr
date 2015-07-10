var ang = angular.module('clouwayHr.employerHistory', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('employerHistory', {
    url: '/employerHistory',
    views: {
      "main": {
        controller: 'EmployerHistoryCtrl',
        templateUrl: 'employerHistory/employerHistory.tpl.html'

      }
    },
    data: {pageTitle: 'History'}
  });
});

ang.controller('EmployerHistoryCtrl', ['$scope', '$http', 'HttpService', function ($scope, $http, HttpService) {

  $scope.currentTime = new Date().getTime();
  $scope.responseMessage = '';
  $scope.getVacations = function () {
    HttpService.get('/r/history/vacations').then(function (messsage) {
      $scope.responseMessage = messsage;
    }, function (reason) {
      $scope.errorMessage = "some error ocurred";
    });
  };

  $scope.changeStatus = function (status, vacationId) {
    var url = '/r/vacation/' + vacationId + '/type/' + status;
    HttpService.put(url).then(function (data) {
      $scope.getVacations();
      displayMessage("success", "success");
    });
  };

  var displayMessage = function (message, type) {
    $.bootstrapGrowl(message, {
      type: type,
      width: 'auto',
      align: 'left'
    });
  };
}]);
