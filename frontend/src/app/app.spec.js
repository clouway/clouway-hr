describe( 'AppCtrl', function() {
  describe( 'httpRequest', function() {

    var $httpBackend, httpRequest, $rootScope;
    beforeEach( module( 'clouwayHr' ) );

    beforeEach(function () {

      inject(function ($injector) {
        $rootScope = $injector.get('$rootScope');
        $httpBackend = $injector.get('$httpBackend');
        httpRequest = $injector.get('httpRequest');
      });
    });

    it('send get request', function () {

      var promise = httpRequest.get('/r/get/');

      $httpBackend.expectGET('/r/get/').respond(200, "data");

      promise.then(function (data) {
        expect(data).toBe("data");
      });

      $httpBackend.flush();
    });

    it('send put request', function () {

      var promise = httpRequest.put('/r/put/', {name: 'Stefan', age: '20'});

      $httpBackend.expectPUT('/r/put/').respond(200, 'data');

      promise.then(function (data) {
        expect(data).toBe('data');
      });

      $httpBackend.flush();
    });

    it('send post request', function () {

      var promise = httpRequest.post('/r/post/', {name: 'Ivan', age: '50'});

      $httpBackend.expectPOST('/r/post/').respond(200, 'data');

      promise.then(function (data) {
        expect(data).toBe('data');
      });

      $httpBackend.flush();
    });

  });
});
