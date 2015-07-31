/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
var ang = angular.module('hr.processingVacations', [
  'ui.router',
  //'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('processingVacations', {
    url: '/processingVacations',
    views: {
      "main": {
        controller: 'ProcessingVacationsCtrl',
        templateUrl: 'employer/processingVacations/processingVacations.tpl.html'
      }
    },
    data: {pageTitle: 'Home'}
  });
});

ang.controller('ProcessingVacationsCtrl', ['$scope', '$http', 'httpRequest', 'appConstants', function ($scope, $http, httpRequest, appConstants) {

  $scope.responseMessage = '';
  var appUrls = appConstants.getAppURL();
  $scope.getVacations = function () {
    httpRequest.get(appUrls.pendingVacations).then(function (messsage) {
      $scope.responseMessage = messsage;
    }, function (reason) {
      $scope.errorMessage = "some error ocurred";
    });
  };


  $scope.changeStatus = function (status, vacationId) {
    var url = '/r/vacation/' + vacationId + '/type/' + status;

    httpRequest.post(url).then(function (data) {
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
