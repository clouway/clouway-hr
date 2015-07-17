/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
describe('processingVacations', function () {
  describe('isCurrentUrl', function () {
    var $httpBackend, httpRequest, controller, appConstants, scope, defer;

    beforeEach(module('hr.processingVacations'));

    beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $q) {
      scope = $rootScope.$new();
      defer = $q.defer();
      $httpBackend = _$httpBackend_;

      httpRequest = {
        get: jasmine.createSpy().andReturn(defer.promise),
        post: jasmine.createSpy().andReturn(defer.promise)
      };

      var pending = {
        pendingVacations: "/r/employer/vacation/type/pending"
      };

      appConstants = {
        getAppURL: function () {
          return pending;
        }
      };

      controller = $controller('ProcessingVacationsCtrl', {
        $scope: scope,
        httpRequest: httpRequest,
        appConstants: appConstants
      });
    }));

    it("get pending vacations", function () {
      var responseMessage = {
        responseMessage: "success"
      };
      defer.resolve(responseMessage);
      appConstants.getAppURL();
      scope.getVacations(appConstants.getAppURL());
      expect(httpRequest.get).toHaveBeenCalledWith(appConstants.getAppURL().pendingVacations);
      scope.$digest();
      expect(scope.responseMessage).toEqual(responseMessage);
    });

    it("get pending vacations", function () {
      scope.changeStatus('reject', 1);

      expect(httpRequest.post).toHaveBeenCalledWith('/r/vacation/1/type/reject');
    });
  });
})
;
