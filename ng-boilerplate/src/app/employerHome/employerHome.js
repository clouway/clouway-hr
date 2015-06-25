var ang = angular.module('ngBoilerplate.employerHome', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider) {
  $stateProvider.state('employerHome', {
    url: '/employer-home',
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

  HttpService.get('/rest/employer/vacation/type/pending').then(function (messsage) {
    console.log(messsage);
    $scope.responseMessage = messsage;
  }, function (reason) {
    $scope.errorMessage = "some error ocurred";

  });

  $scope.changeStatus = function (status, vacationId) {
    var url = '/rest/employer/vacation/' + vacationId + '/type/' + status;
    HttpService.put('/rest/employer/vacation/' + vacationId + '/type/' + status).then(function (data) {
      $scope.statusMessage = "success";
    });
  };


  $scope.dropdownDemoItems = [
    "The first choice!",
    "And another choice for you.",
    "but wait! A third!"
  ];
}])
;
