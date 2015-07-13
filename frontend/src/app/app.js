angular.module('clouwayHr', [
  'templates-app',
  'templates-common',
  'ngBoilerplate.home',
  'ui.router'


])

        .config(function myAppConfig($stateProvider, $urlRouterProvider) {
          $urlRouterProvider.otherwise('/');
        })

        .config(function ($httpProvider) {
          $httpProvider.interceptors.push(function ($q, $location, $window) {
            return {
              'response': function (response) {
                return response;
              },
              'responseError': function (rejection) {
                if (rejection.status === 401) {
                  $window.location.href = '/home';
                }
                return $q.reject(rejection);
              }
            };
          });
        })

        .service('httpRequest', ['$http', '$q', '$rootScope', function ($http, $q, $rootScope) {

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

            $http({method: method, url: url, data: data})
                    .success(function (data) {
                      deferred.resolve(data);
                    })

                    .error(function (data) {
                      deferred.reject(data);
                    });

            return deferred.promise;
          };
        }])

        .run(function run() {
        })

        .controller('AppCtrl', function AppCtrl($scope, $location, httpRequest) {
          $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            if (angular.isDefined(toState.data.pageTitle)) {
              $scope.pageTitle = toState.data.pageTitle;
            }
          });

          $scope.currentUser="";
          httpRequest.get("/oauth/currentuser").then(function (data) {
            $scope.currentUser=data;
          });

        }
)

;