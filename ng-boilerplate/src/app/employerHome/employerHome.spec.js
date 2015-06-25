/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
describe('EmployerHomeCtrl', function () {
  describe('isCurrentUrl', function () {
    var EmployerHomeCtrl, $location, $scope;

    beforeEach(module('ngBoilerplate'));

    beforeEach(inject(function ($controller, _$location_, $rootScope) {
      $location = _$location_;
      $scope = $rootScope.$new();
      EmployerHomeCtrl = $controller('AppCtrl', {$location: $location, $scope: $scope});
    }));

    it('should pass a dummy test', inject(function () {
      expect(EmployerHomeCtrl).toBeTruthy();
    }));


  });
});
