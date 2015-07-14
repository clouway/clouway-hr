/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
describe('EmployeeHome', function () {
  describe('isCurrentUrl', function () {
    var $httpBackend, HttpService, controller, scope, defer;

    beforeEach(module('clouwayHr.employeeHome'));

    beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $q) {
      scope = $rootScope.$new();
      defer = $q.defer();
      $httpBackend = _$httpBackend_;
      HttpService = {
        post: jasmine.createSpy().andReturn(defer.promise),
        get: jasmine.createSpy().andReturn(defer.promise),
        put: jasmine.createSpy().andReturn(defer.promise)
      };
      controller = $controller('EmployeeHomeCtrl', {$scope: scope, HttpService: HttpService});
    }));

    it("request vacation", function () {
      scope.fromDate = new Date();
      scope.toDate = new Date();
      scope.description = "description";

      var vacationData = {
        "fromDate": scope.fromDate.getTime(),
        "toDate": scope.toDate.getTime(),
        "description": "description"
      };

      defer.resolve({responseMessage: 'success'});
      scope.vacationRequest();

      expect(HttpService.post).toHaveBeenCalledWith('/r/employee/vacationRequest', vacationData);
      scope.$digest();

      expect(scope.responseMessage).toEqual('success');
    });

    it('get unHidden vacations', function () {
      var vacationResponse = {
        "dateFrom": 1,
        "dateTo": 2,
        "description": "description"
      };

      defer.resolve(vacationResponse);
      scope.getUnHiddenVacations();

      expect(HttpService.get).toHaveBeenCalledWith('/r/vacation/unhidden');
      scope.$digest();

      expect(scope.unhiddenVacations).toEqual(vacationResponse);
    });

    it('hide vacations', function () {
      scope.hide(2, 1);

      expect(HttpService.put).toHaveBeenCalledWith('/r/vacation/hide/2');
    });
  });
});
