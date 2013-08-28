lecture = function(appName, appHomePath, contextJsonUrl) {
    angular.module(appName, [])
        .controller('homeCtrl',function ($scope, $http) {

            //get context as JSON
            $http({method: 'GET', url: contextJsonUrl}).
                success(function (data) {
                    $scope.cats = data.context.categories;
                    $scope.contextName = data.context.name;
                    $scope.selectedCats = $scope.cats;
                    angular.forEach($scope.cats, function (cat, key) {
                        cat.selectedSrcs = cat.sources;
                    });
                });

            $scope.select = function(catID, srcID) {
                angular.forEach($scope.cats, function (cat, key) {
                    if (cat.id == catID) {
                        cat.isSelected = true;
                        $scope.selectedCats = [cat];
                        if (srcID) {
                            angular.forEach(cat.sources, function (src, key) {
                                if (src.id == srcID) {
                                    src.isSelected = true;
                                    cat.selectedSrcs = [src];
                                };
                             });
                        }
                        else {
                            cat.selectedSrcs = cat.sources;
                        };
                    }
                    else {
                        cat.isSelected = false;
                    };
                });
            };

            $scope.toggle = function(catID) {
                angular.forEach($scope.cats, function (cat, key) {
                    if (cat.id == catID) {
                        cat.folded = !cat.folded;
                    }
                });
            }

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

