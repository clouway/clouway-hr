/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */

describe('administrationController', function () {
  describe('administration', function () {
    var myApp, scope, controller, httpRequest, defer;

    beforeEach(module('hr.administration'));
    beforeEach(module('hr.core'));

    beforeEach(function () {
      inject(function ($injector) {
        scope = $injector.get('$rootScope');
        controller = $injector.get('$controller');
        defer = $injector.get("$q").defer();
        httpRequest = {
          get: jasmine.createSpy().andReturn(defer.promise),
          post: jasmine.createSpy().andReturn(defer.promise)
        };
        myApp = controller('administrationController', {$scope: scope, httpRequest: httpRequest});
      });
    });


    it('get one employee', function () {
      var employees = [{email: 'petko.ivanov@gmail.com', team: 'OSS', name: 'petko'}];
      defer.resolve(employees);
      expect(httpRequest.get).toHaveBeenCalledWith('/employee/getAll', '');
      scope.$digest();
      expect(scope.employees).toEqual(employees);
    });

    it('get two employees', function () {
      var employees = [{
        email: 'petko.ivanov@gmail.com',
        team: 'OSS',
        name: 'petko'
      }, {email: 'ivan.georgiev@gmail.com', team: 'Incubator', name: 'ivan'}];
      defer.resolve(employees);
      expect(httpRequest.get).toHaveBeenCalledWith('/employee/getAll', '');
      scope.$digest();
      expect(scope.employees).toEqual(employees);
    });

    it('edit employee team', function () {
      scope.editEmployee('ivan.georgiev@gmail.com', 'BSS', 'ivan');
      expect(httpRequest.post).toHaveBeenCalledWith('/employee/editTeam', {
        email: 'ivan.georgiev@gmail.com',
        team: 'BSS',
        name: 'ivan'
      });
      scope.$digest();
    });

    it('another edit employee team', function () {
      scope.editEmployee('petar.angelov@gmail.com', 'BSS', 'petar');
      expect(httpRequest.post).toHaveBeenCalledWith('/employee/editTeam', {
        email: 'petar.angelov@gmail.com',
        team: 'BSS',
        name: 'petar'
      });
      scope.$digest();
    });
  });
});