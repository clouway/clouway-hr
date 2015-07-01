var ang = angular.module('clouwayHr.employerHome', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('employerHome', {
    url: '/employerHome',
    views: {
      "main": {
        controller: 'EmployerHomeCtrl',
        templateUrl: 'employerHome/employerHome.tpl.html'
      }
    },
    data: {pageTitle: 'Home'}
  });
});

ang.controller('EmployerHomeCtrl', ['$scope', '$http', 'HttpService', function ($scope, $http, HttpService) {
  $scope.responseMessage = '';

  $scope.getPendingVacations = function () {
    HttpService.get('/r/employer/vacation/type/pending').then(function (messsage) {
      $scope.responseMessage = messsage;
    }, function (reason) {
      $scope.errorMessage = "some error ocurred";
    });
  };

  $scope.changeStatus = function (status, vacationId) {
    var url = '/r/employer/vacation/' + vacationId + '/type/' + status;
    HttpService.put(url).then(function (data) {
      $scope.getPendingVacations();
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
