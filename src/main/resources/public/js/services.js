'use strict';

var samServices = angular.module('samServices', ['ngResource']);

samServices.factory('StatusResourceService', ['$resource', function($resource) {
	return {
		status: $resource('/status', {}, {
			query: {method: 'GET',  params: {}, isArray: true}
		}),
	};
}]);
