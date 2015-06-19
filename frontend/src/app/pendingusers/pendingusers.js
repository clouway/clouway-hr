angular.module('clouwayHr.pendingusers', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
])

        .config(function config($stateProvider) {
          $stateProvider.state('pendingusers', {
            url: '/pendingusers',
            views: {
              "main": {
                controller: 'PendingUsersCtrl',
                templateUrl: 'pendingusers/pendingusers.tpl.html'
              }
            },
            data: {pageTitle: 'Pending users?'}
          });
        })

        .controller('PendingUsersCtrl', function PendingUsersCtrl($scope, httpRequest) {

          $scope.hint = "hint";
          $scope.datalists = [];

          httpRequest.get('r/getpendingusers').then(function (data) {
            $scope.datalists = data;
          });

          $scope.approve = function (email) {

            httpRequest.put("r/approve/"+email).then(function (data) {
                      for (var i = 0; i < $scope.datalists.length; i++) {
                        if ($scope.datalists[i].email == email) {
                          $scope.datalists.splice(i, 1);
                          break;
                        }
                      }
                    }
            );
          };

          $scope.reject = function (email) {
            httpRequest.put("r/reject/"+email).then(function (data) {
                      for (var i = 0; i < $scope.datalists.length; i++) {
                        if ($scope.datalists[i].email == email) {
                          $scope.datalists.splice(i, 1);
                          break;
                        }
                      }
                    }
            );
          };

          $scope.showInModal = function (name, email, picture) {
            $scope.modalName = name;
            $scope.modalEmail = email;
            $scope.modalPicture = picture;
            $('#myModal').modal('show');
          };

        })
;
