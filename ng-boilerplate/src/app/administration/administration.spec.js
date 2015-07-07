/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */

describe('administrationController', function () {
  describe('administration', function () {
    var myApp, scope, controller, httpRequest, defer;

    beforeEach(module('administration'));
    beforeEach(module('ngBoilerplate'));

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
      var employees = [{email: 'petko.ivanov@gmail.com', team:  'OSS', name: 'petko', isAdmin: 'false'}];
      defer.resolve(employees);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      expect(scope.employees).toEqual(employees);
    });

    it('get two employees', function () {
      var employees = [{email: 'petko.ivanov@gmail.com', team:  'OSS', name: 'petko', isAdmin: 'false'}, {email: 'ivan.georgiev@gmail.com', team:  'Incubator', name: 'ivan', isAdmin: 'false'}];
      defer.resolve(employees);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      expect(scope.employees).toEqual(employees);
    });

    it( 'add employee as user', function() {
      defer.resolve([{email: 'petko.ivanov@gmail.com', team:  'OSS', name: 'petko', isAdmin: 'false'}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      prepareScopeFieldsToAddEmployee('georgi.ivanov@gmail.com', 'OSS', 'Georgi', 'false');
      scope.addEmployee();
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/addEmployee', {email: 'georgi.ivanov@gmail.com', team: 'OSS', name: 'Georgi', isAdmin: 'false'});
      scope.$digest();
      expectScopeFieldsToBeEmpty();
      expect(scope.employees).toEqual([ { email : 'georgi.ivanov@gmail.com', team : 'OSS', name : 'Georgi', isAdmin : 'false' }, { email : 'petko.ivanov@gmail.com', team : 'OSS', name : 'petko', isAdmin : 'false' } ]);
    });

    it( 'add employee as administrator', function() {
      defer.resolve([{email: 'petko.ivanov@gmail.com', team:  'OSS', name: 'petko', isAdmin: 'false'}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      prepareScopeFieldsToAddEmployee('petar.georgiev@gmail.com', 'BSS', 'petar', 'true');
      scope.addEmployee();
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/addEmployee', {email: 'petar.georgiev@gmail.com', team: 'BSS', name: 'petar', isAdmin: 'true'});
      scope.$digest();
      expectScopeFieldsToBeEmpty();
      expect(scope.employees).toEqual([{email: 'petar.georgiev@gmail.com', team:  'BSS', name: 'petar', isAdmin: 'true'}, {email: 'petko.ivanov@gmail.com', team:  'OSS', name: 'petko', isAdmin: 'false'}]);
    });

    it( 'edit employee team', function() {
      scope.modalEmployeeRole = 'false';
      defer.resolve([{email: 'petko.ivanov@gmail.com', team:  'OSS', name: 'petko', isAdmin: scope.modalEmployeeRole}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      scope.editEmployee('ivan.georgiev@gmail.com', 'OSS', 'ivan', 0);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/editEmployee', {email: 'ivan.georgiev@gmail.com', team: 'OSS', name: 'ivan', isAdmin: scope.modalEmployeeRole});
      scope.$digest();
      expect(scope.employees).toEqual([{email: 'ivan.georgiev@gmail.com', team:  'OSS', name: 'ivan', isAdmin: scope.modalEmployeeRole}]);
    });

    it('another edit employee team', function () {
      scope.modalEmployeeRole = 'false';
      defer.resolve([{email: 'ivan.georgiev@gmail.com', team:  'BSS', name: 'ivan', isAdmin: scope.modalEmployeeRole},
        {email: 'petar.angelov@gmail.com', team: 'Incubator', name: 'petar', isAdmin: scope.modalEmployeeRole}]);
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.$digest();
      scope.editEmployee('petar.angelov@gmail.com', 'BSS', 'petar', 1);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/editEmployee', {email: 'petar.angelov@gmail.com', team: 'BSS', name: 'petar', isAdmin: scope.modalEmployeeRole});
      scope.$digest();
      expect(scope.employees).toEqual([{ email : 'ivan.georgiev@gmail.com', team : 'BSS', name : 'ivan', isAdmin: scope.modalEmployeeRole }, { email : 'petar.angelov@gmail.com', team : 'BSS', name : 'petar', isAdmin: scope.modalEmployeeRole }]);
    });

    it ('edit employeers name and team at the same time', function () {
      scope.modalEmployeeRole = 'false';
      defer.resolve([{email: 'georgi.georgiev@gmail.com', team:  'shsgh', name: 'sghdfbh', isAdmin: scope.modalEmployeeRole}]);
      scope.$digest();
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.editEmployee('georgi.georgiev@gmail.com', 'BSS', 'georgi', 0);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/editEmployee', {email: 'georgi.georgiev@gmail.com', team: 'BSS', name: 'georgi', isAdmin: scope.modalEmployeeRole});
      scope.$digest();
      expect(scope.employees).toEqual([{email: 'georgi.georgiev@gmail.com', team:  'BSS', name: 'georgi', isAdmin: scope.modalEmployeeRole}]);
    });

    it('edit employee role', function (){
      scope.modalEmployeeRole = 'false';
      defer.resolve([{email: 'georgi.georgiev@gmail.com', team:  'shsgh', name: 'sghdfbh', isAdmin: scope.modalEmployeeRole}]);
      scope.$digest();
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.editEmployee('georgi.georgiev@gmail.com', 'BSS', 'georgi', 0);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/editEmployee', {email: 'georgi.georgiev@gmail.com', team: 'BSS', name: 'georgi', isAdmin: scope.modalEmployeeRole});
      scope.modalEmployeeRole = 'true';
      scope.$digest();
      expect(scope.employees).toEqual([{email: 'georgi.georgiev@gmail.com', team:  'BSS', name: 'georgi', isAdmin: scope.modalEmployeeRole}]);
    });

    it('delete employee', function () {
      defer.resolve([{email: 'qnko.ivanov@gmail.com', team:  'WSS', name: 'qnko'}]);
      scope.$digest();
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');
      scope.deleteEmployee('qnko.ivanov@gmail.com', 0);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/deleteEmployee', {email:'qnko.ivanov@gmail.com'});
      scope.$digest();
      expect(scope.employees).toEqual([]);
    });

    it('another delete employee', function () {
      defer.resolve([{email: 'qnko.ivanov@gmail.com', team:  'WSS', name: 'qnko'},
        {email: 'petkan.georgiev@gmail.com', team:  'Incubator', name: 'petkan'}]);
      scope.$digest();
      expect(httpRequest.get).toHaveBeenCalledWith('/hr/employer/getAll', '');

      scope.deleteEmployee('petkan.georgiev@gmail.com', 1);
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/deleteEmployee', {email:'petkan.georgiev@gmail.com'});
      scope.$digest();
      expect(scope.employees).toEqual([{email: 'qnko.ivanov@gmail.com', team:  'WSS', name: 'qnko'}]);
    });

    it('search for employee', function () {
      scope.searchedName = 'georgi';
      scope.searchEmployee();
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/searchEmployee', {name: scope.searchedName});
      defer.resolve([{email: 'georgi.petrov@gmail.com', team:  'OSS', name: scope.searchedName}]);
      scope.$digest();
      expect(scope.employees).toEqual([{email: 'georgi.petrov@gmail.com', team:  'OSS', name: scope.searchedName}]);
    });

    it('search for employees with same names', function () {
      scope.searchedName = 'dimitar';
      scope.searchEmployee();
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/searchEmployee', {name: scope.searchedName});
      defer.resolve([{email: 'dimitar.ivanov@gmail.com', team:  'WSS', name: scope.searchedName},
        {email: 'dimitar.dimitrov@gmail.com', team:  'Incubator', name: scope.searchedName}]);
      scope.$digest();
      expect(scope.employees).toEqual([{email: 'dimitar.ivanov@gmail.com', team:  'WSS', name: 'dimitar'},
        {email: 'dimitar.dimitrov@gmail.com', team:  'Incubator', name: scope.searchedName}]);
    });

    it('search by first letters', function () {
      scope.searchedName = 'pet';
      scope.searchEmployee();
      expect(httpRequest.post).toHaveBeenCalledWith('/hr/employer/searchEmployee', {name: scope.searchedName});
      defer.resolve([{email: 'petar.ivanov@gmail.com', team:  'WSS', name: 'petar'},
        {email: 'petko.dimitrov@gmail.com', team:  'Incubator', name: 'petko'}]);
      scope.$digest();
      expect(scope.employees).toEqual([{email: 'petar.ivanov@gmail.com', team:  'WSS', name: 'petar'},
        {email: 'petko.dimitrov@gmail.com', team:  'Incubator', name: 'petko'}]);
    });

    var expectScopeFieldsToBeEmpty = function () {
      expect(scope.employeeEmail).toBe('');
      expect(scope.employeeTeam).toBe('');
      expect(scope.employeeName).toBe('');
      expect(scope.employeeRole).toBe('false');
    };

    var prepareScopeFieldsToAddEmployee = function (email, team, name, role){
      scope.employeeEmail = email;
      scope.employeeTeam = team;
      scope.employeeName = name;
      scope.employeeRole = role;
    };
  });
});