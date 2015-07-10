var ang = angular.module('clouwayHr', [
  'templates-app',
  'templates-common',
  'clouwayHr.home',
  'clouwayHr.employeeHome',
  'clouwayHr.employerHome',
  'clouwayHr.employerHistory',
  'clouwayHr.employeeHistory',
  'ui.router'
]);

ang.config(function myAppConfig($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise('/home');
});

ang.run(function run() {
});

ang.service('HttpService', ['$http', '$q', '$rootScope', function ($http, $q, $rootScope) {
  var count = 0;

  this.get = function (url, data) {
    return this.send('GET', url, data);
  };

  this.post = function (url, data) {
    return this.send('POST', url, data);
  };

  this.put = function (url, data) {
    return this.send('PUT', url, data);
  };

  this.send = function (method, url, data) {
    var deferred = $q.defer();
    count++;

    $http({method: method, url: url, data: data})
            .success(function (data) {
              deferred.resolve(data);
              count--;

              if (count === 0) {
                $rootScope.loadingInProgress = false;
              }
            })

            .error(function (data) {
              deferred.reject(data);
              count--;

              if (count === 0) {
                $rootScope.loadingInProgress = false;
              }

            });

    $rootScope.loadingInProgress = true;

    return deferred.promise;
  };
}]);

ang.controller('AppCtrl', function AppCtrl($scope, $location) {
  $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
    if (angular.isDefined(toState.data.pageTitle)) {
      $scope.pageTitle = toState.data.pageTitle + ' | clouway-hr';
    }
  });
});
