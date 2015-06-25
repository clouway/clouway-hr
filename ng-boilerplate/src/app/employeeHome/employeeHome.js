var ang = angular.module('ngBoilerplate.employeeHome', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('employeeHome', {
    url: '/employee-home',
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
      "userId": 666,
      "fromDate": fromDate,
      "toDate": toDate,
      "description": description
    };

    HttpService.post('/rest/employee/vacation-request', vacationData)
            .then(function (message) {
              $scope.responseMessage = "success";
            }, function (reason) {
              $scope.responseMessage = "incorrect date";

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


  $scope.dropdownDemoItems = [
    "The first choice!",
    "And another choice for you.",
    "but wait! A third!"
  ];
}]);