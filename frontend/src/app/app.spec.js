describe( 'AppCtrl', function() {
  describe( 'isCurrentUrl', function() {
    var AppCtrl, $location, $scope;
    var $httpBackend, httpRequest, $rootScope;
    beforeEach( module( 'clouwayHr' ) );

    beforeEach( inject( function( $controller, _$location_, $rootScope ) {
      $location = _$location_;
      $scope = $rootScope.$new();
      AppCtrl = $controller( 'AppCtrl', { $location: $location, $scope: $scope });
    }));

    beforeEach(function () {

      inject(function ($injector) {
        $rootScope = $injector.get('$rootScope');
        $httpBackend = $injector.get('$httpBackend');
        httpRequest = $injector.get('httpRequest');
      });
    });

    it( 'should pass a dummy test1', inject( function() {
      expect( AppCtrl ).toBeTruthy();
    }));

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

    it('send put request', function () {

      var promise = httpRequest.put('/r/test/', {username: 'Test', password: 'unknown'});

      $httpBackend.expectPUT('/r/test/').respond(200, 'response123');

      expect($rootScope.loadingInProgress).toBe(true);

      promise.then(function (data) {
        expect(data).toBe('response123');
      });

      $httpBackend.flush();

      expect($rootScope.loadingInProgress).toBe(false);
    });

    it('send post request', function () {

      var promise = httpRequest.post('/r/test/', {username: 'Test', password: 'unknown'});

      $httpBackend.expectPOST('/r/test/').respond(200, 'response123');

      expect($rootScope.loadingInProgress).toBe(true);

      promise.then(function (data) {
        expect(data).toBe('response123');
      });

      $httpBackend.flush();

      expect($rootScope.loadingInProgress).toBe(false);
    });

  });
});
