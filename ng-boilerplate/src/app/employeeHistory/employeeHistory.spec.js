/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
describe('EmployeeHistoryCtrl', function () {
  describe('isCurrentUrl', function () {
    var $httpBackend, HttpService, controller, scope, defer;


    beforeEach(module('clouwayHr.employeeHistory'));

    beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $q) {
      scope = $rootScope.$new();
      defer = $q.defer();
      $httpBackend = _$httpBackend_;
      HttpService = {
        get: jasmine.createSpy().andReturn(defer.promise)
      };
      controller = $controller('EmployeeHistoryCtrl', {$scope: scope, HttpService: HttpService});
    }));

    it("get vacation history ", function () {
      var responseMessage = {
        "vacationId": 4996180836614144,
        "status": "accept",
        "dateFrom": 1438549200000,
        "dateTo": 1439240400000,
        "description": "some description"
      };

      defer.resolve(responseMessage);

      scope.getVacations('/r/history/vacations');
      expect(HttpService.get).toHaveBeenCalledWith('/r/history/vacations');
      scope.$digest();
      expect(scope.responseMessage).toEqual(responseMessage);
    });
  });
});
