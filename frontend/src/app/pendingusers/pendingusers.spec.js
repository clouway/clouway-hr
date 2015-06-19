describe('PENDING USERS TEST SUITE', function () {

  var myApp, scope, httpBackend, controller, http, httpRequest, defer;

  beforeEach(module('clouwayHr'));
  beforeEach(module('clouwayHr.pendingusers'));

  beforeEach(function () {
    inject(function ($injector) {
      httpBackend = $injector.get('$httpBackend');
      scope = $injector.get('$rootScope');
      controller = $injector.get('$controller');
      http = $injector.get('$http');
      defer = $injector.get("$q").defer();

      httpRequest = {
        get: jasmine.createSpy().andReturn(defer.promise),
        post: jasmine.createSpy().andReturn(defer.promise),
        put: jasmine.createSpy().andReturn(defer.promise)
      };
      myApp = controller('PendingUsersCtrl', {$scope: scope, httpRequest: httpRequest});
    });
  });

  it('=====SHOULD PASS IF SETUP IS OK=====', function () {
    expect(scope.hint).toEqual('hint');
    expect(httpRequest.get).toHaveBeenCalledWith("r/getpendingusers");
  });

  it('=====SHOULD PASS IF RETURN PERSON=====', function () {
    var person = {'name': 'pepo'};
    defer.resolve(person);
    expect(httpRequest.get).toHaveBeenCalledWith("r/getpendingusers");
    scope.$digest();
    expect(scope.datalists).toEqual({'name': 'pepo'});
  });

  it('=====SHOULD PASS IF approve() works=====', function () {

    scope.approve("email");
    defer.resolve([{'email': 'pepo'}, {'email': 'email'}]);
    scope.$digest();

    expect(httpRequest.put).toHaveBeenCalledWith("r/approve/email");
    expect(scope.datalists).toEqual([{'email': 'pepo'}]);
  });


  it('=====SHOULD PASS IF reject() WORKS=====', function () {
    scope.reject("email");
    defer.resolve([{'email': 'pepo'}, {'email': 'email'}]);
    scope.$digest();

    expect(httpRequest.put).toHaveBeenCalledWith("r/reject/email");
    expect(scope.datalists).toEqual([{'email': 'pepo'}]);
  });

});