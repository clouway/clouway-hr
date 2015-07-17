angular.module( 'hr.core', [
  'templates-app',
  'templates-common',
  'ui.router'
])
        .config(function myAppConfig($stateProvider, $urlRouterProvider) {
          $urlRouterProvider.otherwise('/');
        })

        .run(function run() {
        })

        .controller('AppCtrl', function AppCtrl($scope, httpRequest) {
          $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            if (angular.isDefined(toState.data.pageTitle)) {
              $scope.pageTitle = toState.data.pageTitle;
            }
          });
          httpRequest.get('/oauth/currentuser').then(function (data) {
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