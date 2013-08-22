var app = angular.module('lecture', []); 

app.controller('testCtrl', function ($scope, $http) {
	$scope.toto = "Dans le controleur !";

	$scope.getCats = function(url) {
		$http({method: 'GET', url: url}).
		success(function(data) {
			$scope.cats = data.context.categories;
			$scope.contextName = data.context.name;
		});
	};
});

