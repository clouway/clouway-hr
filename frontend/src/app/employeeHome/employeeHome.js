var ang = angular.module('clouwayHr.employeeHome', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('employeeHome', {
    url: '/employeeHome',
    views: {
      "main": {
        controller: 'EmployeeHomeCtrl',
        templateUrl: 'employeeHome/employeeHome.tpl.html'
      }
    },
    data: {pageTitle: 'Home'}
  });
});


ang.controller('EmployeeHomeCtrl', ['$scope', '$http', 'HttpService', function ($scope, $http, HttpService) {
  $scope.responseMessage = '';
  $scope.vacationRequest = function vacationRequest() {

    var fromDate = $scope.fromDate.getTime();
    var toDate = $scope.toDate.getTime();
    var description = $scope.description;

    var vacationData = {
      "fromDate": fromDate,
      "toDate": toDate,
      "description": description
    };

    HttpService.post('/r/employee/vacationRequest', vacationData)
            .then(function (message) {
              $scope.responseMessage = "success";
              displayMessage($scope.responseMessage, 'success');
              $scope.getUnHiddenVacations();
            }, function (reason) {
              $scope.responseMessage = "incorrect date";
              displayMessage($scope.responseMessage, 'danger');
            });
  };

  $scope.getUnHiddenVacations = function () {
    HttpService.get('/r/vacation/unhidden').then(function (data) {
      $scope.unhiddenVacations = data;
    });
  };

  $scope.hide = function (vacationId, vacationPosition) {
    var url = "/r/vacation/hide/" + vacationId;

    HttpService.put(url).then(function () {
      $scope.unhiddenVacations.splice(vacationPosition, 1);
    });
  };

  var displayMessage = function (message, type) {
    $.bootstrapGrowl($scope.responseMessage, {
      type: type,
      width: 'auto',
      align: 'left'
    });
  };

  //DATEPICKER

  $scope.clear = function () {
    $scope.fromDate = null;
    $scope.toDate = null;
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
