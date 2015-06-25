var ang = angular.module('ngBoilerplate', [
  'templates-app',
  'templates-common',
  'ngBoilerplate.home',
  'ngBoilerplate.about',
  'ngBoilerplate.employeeHome',
  'ngBoilerplate.employerHome',
  'ui.router'
]);

ang.config(function myAppConfig($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise('/home');
});

ang.run(function run() {
});

ang.factory("HttpService", ["$http", "$q", function ($http, $q) {
  return {
    get: function (url) {
      var result = $q.defer();
      $http.get(url).success(function ($data) {
        result.resolve($data);
      }).error(function ($data) {
        result.reject($data);
      });
      return result.promise;
    },

    post: function (url, d) {
      var result = $q.defer();

      $http.post(url, d)
              .success(function ($data) {
                console.log($data);
                result.resolve($data);
              })
              .error(function ($data) {
                result.reject($data);
              });
      return result.promise;
    },

    put: function (url) {
      var result = $q.defer();
      $http.put(url).success(function ($data) {
        result.resolve($data);
      }).error(function ($data) {
        result.reject($data);
      });

      return result.promise;
    }
  };
}]);

ang.controller('AppCtrl', function AppCtrl($scope, $location) {
  $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
    if (angular.isDefined(toState.data.pageTitle)) {
      $scope.pageTitle = toState.data.pageTitle + ' | ngBoilerplate';
    }
  });
});

