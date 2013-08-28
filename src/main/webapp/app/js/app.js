var app = angular.module(appName, []);

app.controller('homeCtrl', function ($scope, $http) {

    //get context as JSON
    $http({method: 'GET', url: url}).
        success(function (data) {
            $scope.cats = data.context.categories;
            $scope.contextName = data.context.name;
            $scope.selectedCats = $scope.cats;
        });

    $scope.selectCat = function(catID) {
        angular.forEach($scope.cats, function(cat, key) {
            if (cat.id == catID) {
                cat.isSelected = true;
                $scope.selectedCats = [cat];
            }
            else {
                cat.isSelected = false;
            }
        });
    }
});

app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/', {
            controller: 'homeCtrl',
            templateUrl: appHomePath + '/views/home.html'
        }).
        otherwise(
            {redirectTo : '/'}
        )
}]);

