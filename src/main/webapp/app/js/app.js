lecture = function(appName, appHomePath, resourceURL) {
    angular.module(appName, [])
        .controller('homeCtrl',function ($scope, $http) {

            //get context as JSON
            $http({method: 'GET', url: url(resourceURL, "getJSON")}).
                success(function (data) {
                    $scope.msgs = data.messages;
                    $scope.cats = data.context.categories;
                    $scope.contextName = data.context.name;
                    $scope.selectedCats = [$scope.cats[0]];
                    angular.forEach($scope.cats, function (cat, key) {
                        cat.selectedSrcs = cat.sources;
                    });
                });

            function url(pattern, id, p1, p2, p3, p4) {
                return pattern.
                    replace("@@id@@", id).
                    replace("__p1__", p1).
                    replace("__p2__", p2).
                    replace("__p3__", p3).
                    replace("__p4__", p4)
            }

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
                                }
                             });
                        }
                        else {
                            cat.selectedSrcs = cat.sources;
                        }
                    }
                    else {
                        cat.isSelected = false;
                    }
                });
            };

            $scope.toggle = function(catID) {
                angular.forEach($scope.cats, function (cat, key) {
                    if (cat.id == catID) {
                        cat.folded = !cat.folded;
                    }
                });
            };

            $scope.toggleItemReadState = function(cat, src, item) {
                $http({method: 'GET', url: url(resourceURL, "toggleItemReadState", cat.id, src.id, item.id, item.read)}).
                    success(function (data) {
                            (item.read ? src.unreadItemsNumber++ : src.unreadItemsNumber--);
                            item.read = !item.read;
                        });
                    };

            $scope.markAllItemsAsRead = function(flag) {
                angular.forEach($scope.selectedCats, function (cat, key) {
                    angular.forEach(cat.selectedSrcs, function (src, key) {
                        angular.forEach(src.items, function (item, key) {
                            if (item.read != flag) {
                                $scope.toggleItemReadState(cat, src, item);
                            }
                        })
                    })
                })
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

