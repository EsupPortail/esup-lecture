lecture = function(appName, appHomePath, contextJsonUrl) {
    angular.module(appName, [])
        .controller('homeCtrl',function ($scope, $http) {

            //get context as JSON
            $http({method: 'GET', url: contextJsonUrl}).
                success(function (data) {
                    $scope.cats = data.context.categories;
                    $scope.contextName = data.context.name;
                    $scope.selectedCats = $scope.cats;
                });

            $scope.selectCat = function (catID) {
                angular.forEach($scope.cats, function (cat, key) {
                    if (cat.id == catID) {
                        cat.isSelected = true;
                        $scope.selectedCats = [cat];
                    }
                    else {
                        cat.isSelected = false;
                    }
                });
            };
        }).
        config(['$routeProvider', function ($routeProvider) {
            $routeProvider.
                when('/', {
                    controller: 'homeCtrl',
                    templateUrl: appHomePath + '/views/home.html'
                }).
                otherwise(
                {redirectTo: '/'}
            );
        }]);
};

