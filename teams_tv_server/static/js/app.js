(function(angular) {
  'use strict';
var myApp = angular.module('COETVApp', []);

myApp.controller('COETVAppController', ['$scope', function($scope) {
    $scope.message = "Hello, All";
}]);
})(window.angular);