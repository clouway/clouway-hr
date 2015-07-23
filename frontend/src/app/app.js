angular.module( 'hr.core', [
  'templates-app',
  'templates-common',
  'ui.router'
])
        .config(function myAppConfig($stateProvider, $urlRouterProvider) {
          $urlRouterProvider.otherwise('/');
        })

        .config(function ($httpProvider) {
          $httpProvider.interceptors.push(function ($q,$location,$window) {
            return {
              'response': function (response) {
                return response;
              },
              'responseError': function (rejection) {
                if (rejection.status === 401) {
                  $window.location.href = '/oauth/credential';
                }
                return $q.reject(rejection);
              }
            };
          });
        })

        .run(function run() {
        })

        .controller('AppCtrl', function AppCtrl($scope, httpRequest) {
          $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            if (angular.isDefined(toState.data.pageTitle)) {
              $scope.pageTitle = toState.data.pageTitle;
            }
          });
          httpRequest.get('/userservices/currentuser').then(function (data) {
            $scope.currentuser = data;
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

            $rootScope.loadingInProgress = true;

            return deferred.promise;
          };
        }]);