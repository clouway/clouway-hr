describe('http request service', function () {
  var $httpBackend, httpRequest, $rootScope;

  beforeEach(module('hr.core'));

  beforeEach(function () {

    inject(function ($injector) {
      $rootScope = $injector.get('$rootScope');
      $httpBackend = $injector.get('$httpBackend');
      httpRequest = $injector.get('httpRequest');
    });
  });

  it('sends get request', function () {

    var promise = httpRequest.get('/test/', {date: '22.05.2015'});

    $httpBackend.expectGET('/test/').respond(200, 'response123');

    promise.then(function (data) {
      expect(data).toBe('response123');
    });

    $httpBackend.flush();
  });


  it('sends post request', function () {
    var promise = httpRequest.post('/test/', {date: '10.12.2013'});

    $httpBackend.expectPOST('/test/').respond(200, 'response');

    promise.then(function (data) {
      expect(data).toBe('response');
    });

    $httpBackend.flush();
  });

  it('sends put request', function () {
    var promise = httpRequest.put('/test/', {date: '30.04.2000'});

    $httpBackend.expectPUT('/test/').respond(200, 'response12345');

    promise.then(function (data) {
      expect(data).toBe('response12345');
    });

    $httpBackend.flush();
  });
});
