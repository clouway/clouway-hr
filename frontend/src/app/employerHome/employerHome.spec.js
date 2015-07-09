/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
describe('EmployerHomeCtrl', function () {
  describe('isCurrentUrl', function () {
    var $httpBackend, HttpService, controller, scope, defer;

    beforeEach(module('clouwayHr.employerHome'));

    beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $q) {
      scope = $rootScope.$new();
      defer = $q.defer();
      $httpBackend = _$httpBackend_;
      HttpService = {
        get: jasmine.createSpy().andReturn(defer.promise),
        put: jasmine.createSpy().andReturn(defer.promise)
      };
      controller = $controller('EmployerHomeCtrl', {$scope: scope, HttpService: HttpService});
    }));

    it("get pending vacations", function () {
      var responseMessage = {
        responseMessage: "success"
      };
      defer.resolve(responseMessage);

      scope.getVacations('/r/employer/vacation/type/pending');
      expect(HttpService.get).toHaveBeenCalledWith('/r/employer/vacation/type/pending');
      scope.$digest();
      expect(scope.responseMessage).toEqual(responseMessage);
    });

    it("get pending vacations", function () {
      scope.changeStatus('reject', 1);

      expect(HttpService.put).toHaveBeenCalledWith('/r/vacation/1/type/reject');
    });
  });
})
;
