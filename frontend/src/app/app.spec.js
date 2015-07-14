describe('AppCtrl', function () {
  describe('isCurrentUrl', function () {
    var $httpBackend, httpRequest, $rootScope;

    beforeEach(module('clouwayHr'));

    beforeEach(function () {

      inject(function ($injector) {
        $rootScope = $injector.get('$rootScope');
        $httpBackend = $injector.get('$httpBackend');
        httpRequest = $injector.get('HttpService');
      });
    });

    it('send get request', function () {
      var promise = httpRequest.get('/r/test/', {username: 'Test', password: 'unknown'});

      $httpBackend.expectGET('/r/test/').respond(200, 'response123');
      expect($rootScope.loadingInProgress).toBe(true);

      promise.then(function (data) {
        expect(data).toBe('response123');
      });

      $httpBackend.flush();

      expect($rootScope.loadingInProgress).toBe(false);
    });

    it('hides loading bar when all pending responses are received ', function () {

      var p1 = httpRequest.get('/r/test/', {username: 'Test', password: 'unknown'});
      var p2 = httpRequest.get('/r/another/', {username: 'Test', password: 'unknown'});

      $httpBackend.expectGET('/r/test/').respond(200, 'response123');

      expect($rootScope.loadingInProgress).toBe(true);

      p1.then(function (data) {
        expect(data).toBe('response123');
      });

      $httpBackend.expectGET('/r/another/').respond(200, 'response123');

      p2.then(function (data) {
        expect(data).toBe('response123');
      });

      $httpBackend.flush();

      expect($rootScope.loadingInProgress).toBe(false);
    });

    it('hides loading bar when request fails', function () {
      var promise = httpRequest.get('/r/test2/');

      $httpBackend.expectGET('/r/test2/').respond(404, 'error404');

      expect($rootScope.loadingInProgress).toBe(true);

      promise.then(null, function (data) {
        expect(data).toBe('error404');
      });

      $httpBackend.flush();

      expect($rootScope.loadingInProgress).toBe(false);
    });
  });
});
