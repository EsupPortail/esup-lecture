lecture = function(appName, appHomePath, resourceURL) {
    angular.module(appName, [])
        .controller('homeCtrl',function ($scope, $http) {

            var treeVisibleState;

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
                    treeVisibleState = data.context.treeVisibleState;
                });

            //forge a portlet resource url
            function url(pattern, id, p1, p2, p3, p4) {
                return pattern.
                    replace("@@id@@", id).
                    replace("__p1__", p1).
                    replace("__p2__", p2).
                    replace("__p3__", p3).
                    replace("__p4__", p4)
            }

            //select a category and eventually a source to restrict displayed content
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

            // fold | unfold a category
            $scope.toggle = function(catID) {
                angular.forEach($scope.cats, function (cat, key) {
                    if (cat.id == catID) {
                        cat.folded = !cat.folded;
                    }
                });
            };

            // mark as read or unread on source item
            $scope.toggleItemReadState = function(cat, src, item) {
                //call server to store information
                $http({method: 'GET', url: url(resourceURL, "toggleItemReadState", cat.id, src.id, item.id, !item.read)}).
                    success(function (data) {
                            (item.read ? src.unreadItemsNumber++ : src.unreadItemsNumber--);
                            item.read = !item.read;
                        });
                    };

            // mark as read or unread all displayed source items
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
            };

            // evaluate is hideTree button should be displayed
            $scope.hideTreeDisplayed = function() {
                if (treeVisibleState == "NEVERVISIBLE") return false;
                if (treeVisibleState == "VISIBLE") return true;
            };

            // evaluate is showTree button should be displayed
            $scope.showTreeDisplayed = function() {
                if (treeVisibleState == "NEVERVISIBLE") return false;
                if (treeVisibleState == "NOTVISIBLE") return true;
            };

            // evaluate is tree should be displayed
            $scope.treeDisplayed = function() {
                if (treeVisibleState == "NEVERVISIBLE") return false;
                if (treeVisibleState == "VISIBLE") return true;
            };

            // show the tree
            $scope.showTree = function() {
                treeVisibleState = "VISIBLE";
            };

            // hide the tree
            $scope.hideTree = function() {
                treeVisibleState = "NOTVISIBLE";
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

