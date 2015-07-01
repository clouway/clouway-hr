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
        post: jasmine.createSpy().andReturn(defer.promise)
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

      expect(HttpService.post).toHaveBeenCalledWith('/rest/employee/vacation-request', vacationData);
      scope.$digest();

      expect(scope.responseMessage).toEqual('success');
    });

  });
});
