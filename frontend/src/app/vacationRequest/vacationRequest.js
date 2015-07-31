/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
var ang = angular.module('hr.vacationRequest', [
  'ui.router',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('vacationRequest', {
    url: '/vacationRequest',
    views: {
      "main": {
        controller: 'VacationRequestCtrl',
        templateUrl: 'vacationRequest/vacationRequest.tpl.html'
      }
    },
    data: {pageTitle: 'Vacation Request'}
  });
});

ang.controller('VacationRequestCtrl', ['$scope', '$http', 'httpRequest', 'appConstants', function ($scope, $http, httpRequest, appConstants) {
  $scope.responseMessage = '';
  var appUrls = appConstants.getAppURL();
  $scope.vacationRequest = function vacationRequest() {
    var fromDate = $scope.fromDate.getTime();
    var toDate = $scope.toDate.getTime();
    var description = $scope.description;

    var vacationData = {
      "fromDate": fromDate,
      "toDate": toDate,
      "description": description
    };

    httpRequest.post(appUrls.vacationRequest, vacationData)
            .then(function (message) {
              $scope.responseMessage = "success";
              displayMessage($scope.responseMessage, 'success');
              $scope.isDisabled = true;
              $scope.clear();
            }, function (reason) {
              $scope.responseMessage = "incorrect date";
              displayMessage($scope.responseMessage, 'danger');
            });
  };

  $scope.getUnHiddenVacations = function () {
    httpRequest.get(appUrls.unhiddenVacations).then(function (data) {
      $scope.unhiddenVacations = data;
    });
  };

  $scope.hide = function (vacationId, vacationPosition) {
    var url = appUrls.hideVacation + vacationId;

    httpRequest.post(url).then(function () {
      $scope.unhiddenVacations.splice(vacationPosition, 1);
    });
  };

  var displayMessage = function (message, type) {
    $.bootstrapGrowl(message, {
      type: type,
      width: 'auto',
      align: 'left'
    });
  };

  //DATEPICKER

  $scope.clear = function () {
    $scope.fromDate = null;
    $scope.toDate = null;
    $scope.description = null;
  };

  // Disable weekend selection
  $scope.disabled = function (date, mode) {
    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
  };

  $scope.open = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.openTo = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.dateOptions = {
    formatYear: 'y',
    startingDay: 1
  };

  var tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  var afterTomorrow = new Date();
  afterTomorrow.setDate(tomorrow.getDate() + 2);
  //DATEPICKER
}]);
