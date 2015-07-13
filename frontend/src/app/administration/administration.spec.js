/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */

describe('administrationController', function () {
  describe('administration', function () {
    var myApp, scope, controller, httpRequest, defer;

    beforeEach(module('administration'));
    beforeEach(module('clouway-hr'));

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
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
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
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      expect(scope.employees).toEqual(employees);
    });

    it('add employee', function () {
      defer.resolve([{email: 'petko.ivanov@gmail.com', team: 'OSS', name: 'petko'}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      prepareScopeFieldsToAddEmployee('georgi.ivanov@gmail.com', 'OSS', 'Georgi');
      scope.addEmployee();
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/addEmployee', {
        email: 'georgi.ivanov@gmail.com',
        team: 'OSS',
        name: 'Georgi'
      });
      scope.$digest();
      expectScopeFieldsToBeEmpty();
      expect(scope.employees).toEqual([{
        email: 'georgi.ivanov@gmail.com',
        team: 'OSS',
        name: 'Georgi'
      }, {email: 'petko.ivanov@gmail.com', team: 'OSS', name: 'petko'}]);
    });

    it('add another employee', function () {
      defer.resolve([{email: 'petko.ivanov@gmail.com', team: 'OSS', name: 'petko'}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      prepareScopeFieldsToAddEmployee('petar.georgiev@gmail.com', 'BSS', 'petar');
      scope.addEmployee();
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/addEmployee', {
        email: 'petar.georgiev@gmail.com',
        team: 'BSS',
        name: 'petar'
      });
      scope.$digest();
      expectScopeFieldsToBeEmpty();
      expect(scope.employees).toEqual([{
        email: 'petar.georgiev@gmail.com',
        team: 'BSS',
        name: 'petar'
      }, {email: 'petko.ivanov@gmail.com', team: 'OSS', name: 'petko'}]);
    });

    it('edit employee team', function () {
      defer.resolve([{email: 'petko.ivanov@gmail.com', team: 'OSS', name: 'petko'}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      scope.editEmployee('ivan.georgiev@gmail.com', 'OSS', 'ivan', 0);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/editEmployee', {
        email: 'ivan.georgiev@gmail.com',
        team: 'OSS',
        name: 'ivan'
      });
      scope.$digest();
      expect(scope.employees).toEqual([{
        email: 'ivan.georgiev@gmail.com',
        team: 'OSS',
        name: 'ivan'
      }]);
    });

    it('another edit employee team', function () {
      defer.resolve([{email: 'ivan.georgiev@gmail.com', team: 'BSS', name: 'ivan'},
        {email: 'petar.angelov@gmail.com', team: 'Incubator', name: 'petar'}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      scope.editEmployee('petar.angelov@gmail.com', 'BSS', 'petar', 1);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/editEmployee', {
        email: 'petar.angelov@gmail.com',
        team: 'BSS',
        name: 'petar'
      });
      scope.$digest();
      expect(scope.employees).toEqual([{
        email: 'ivan.georgiev@gmail.com',
        team: 'BSS',
        name: 'ivan'
      }, {email: 'petar.angelov@gmail.com', team: 'BSS', name: 'petar'}]);
    });

    var expectScopeFieldsToBeEmpty = function () {
      expect(scope.employeeEmail).toBe('');
      expect(scope.employeeTeam).toBe('');
      expect(scope.employeeName).toBe('');
    };

    var prepareScopeFieldsToAddEmployee = function (email, team, name) {
      scope.employeeEmail = email;
      scope.employeeTeam = team;
      scope.employeeName = name;
    };
  });
});