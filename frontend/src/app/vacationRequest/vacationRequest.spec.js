/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
describe('vacationRequest', function () {
  describe('isCurrentUrl', function () {
    var $httpBackend, httpRequest, controller, appConstants, scope, defer;

    beforeEach(module('hr.vacationRequest'));

    beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $q) {
      scope = $rootScope.$new();
      defer = $q.defer();
      $httpBackend = _$httpBackend_;
      httpRequest = {
        post: jasmine.createSpy().andReturn(defer.promise),
        get: jasmine.createSpy().andReturn(defer.promise)
      };

      var vacationRequest = {
        vacationRequest: '/r/employee/vacationRequest',
        unhiddenVacations: '/r/vacation/unhidden',
        hideVacation: '/r/vacation/hide'
      };

      appConstants = {
        getAppURL: function () {
          return vacationRequest;
        }
      };
      controller = $controller('VacationRequestCtrl', {
        $scope: scope,
        httpRequest: httpRequest,
        appConstants: appConstants
      });
    }));

    it('should pass', function () {
      expect(controller).toBeTruthy();
    });

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

      expect(httpRequest.post).toHaveBeenCalledWith(appConstants.getAppURL().vacationRequest, vacationData);
      scope.$digest();

      expect(scope.responseMessage).toEqual('success');
      expect(scope.isDisabled).toEqual(true);
      expect(scope.fromDate).toEqual(null);
      expect(scope.toDate).toEqual(null);
      expect(scope.description).toEqual(null);
    });

    it('get unHidden vacations', function () {
      var vacationResponse = {
        "dateFrom": 1,
        "dateTo": 2,
        "description": "description"
      };

      defer.resolve(vacationResponse);
      scope.getUnHiddenVacations();

      expect(httpRequest.get).toHaveBeenCalledWith(appConstants.getAppURL().unhiddenVacations);
      scope.$digest();

      expect(scope.unhiddenVacations).toEqual(vacationResponse);
    });

    it('hide vacations', function () {
      scope.hide(2, 1);

      expect(httpRequest.post).toHaveBeenCalledWith(appConstants.getAppURL().hideVacation + 2);
    });
  });
});
