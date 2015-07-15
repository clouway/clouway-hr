describe('http request service', function () {
  var $rootScope, AppCtrl;

  beforeEach(module('clouway-hr'));

  beforeEach(function () {

    inject(function ($injector, $controller) {
      $rootScope = $injector.get('$rootScope');
      AppCtrl = $controller('AppCtrl', {$scope: $rootScope});
    });
  });

  it('should pass a dummy test', function () {
    expect(AppCtrl).toBeTruthy();
  });
});