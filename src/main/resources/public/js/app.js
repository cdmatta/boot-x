var samApp = angular.module('samApp', ['samServices']);

samApp.controller('StatusResourceCtrl', [ '$scope', 'StatusResourceService', '$interval', function($scope, StatusResourceService, $interval) {
	$scope.sortType = 'recentFailureCount';
	$scope.sortReverse = true;
	$scope.searchString = '';

	StatusResourceService.status.query().$promise.then(function (result) {
		$scope.webpages = result;
	});

	$interval(function() {
		StatusResourceService.status.query().$promise.then(function (result) {
			$scope.webpages = result;
		});
	}, 10 * 1000);
}]);

samApp.factory('StatusResourceService', ['$resource', function($resource) {
	return {
		status: $resource('/status', {}, {
			query: {method: 'GET',  params: {}, isArray: true}
		}),
	};
}]);

