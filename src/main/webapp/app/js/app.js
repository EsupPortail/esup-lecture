lecture = function(appName, appHomePath, resourceURL) {
    var project = angular.module(appName, []);

    //config
    project.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
                when('/', {
            controller: 'homeCtrl',
            templateUrl: appHomePath + '/views/home.html'
        }).
                otherwise(
                {redirectTo: '/'}
        );
    }]);

    //Home Controller
    project.controller('homeCtrl', function($scope, $http) {
        var treeVisibleState;

        //get context as JSON
        $http({method: 'GET', url: url(resourceURL, "getJSON")}).
                success(function(data) {
            //i18n messages
            $scope.msgs = data.messages;
            //items display modes
            $scope.modes = [
                {id: 'all', value: $scope.msgs['all']},
                {id: 'notRead', value: $scope.msgs['notRead']},
                {id: 'unreadFirst', value: $scope.msgs['unreadFirst']}];
            //items not read displayed by default
            $scope.selectedMode = 'notRead';
            //categories
            $scope.cats = data.context.categories;
            //context name
            $scope.contextName = data.context.name;
            //first category selected by default
            $scope.selectedCats = [$scope.cats[0]];
            //all sources selected by default
            angular.forEach($scope.cats, function(cat, key) {
                cat.selectedSrcs = cat.sources;
            });
            //tree visible state
            treeVisibleState = data.context.treeVisibleState;
        });

        //select a category and eventually a source to restrict displayed content
        $scope.select = function(catID, srcID) {
            angular.forEach($scope.cats, function(cat, key) {
                if (cat.id === catID) {
                    cat.isSelected = true;
                    $scope.selectedCats = [cat];
                    if (srcID) {
                        angular.forEach(cat.sources, function(src, key) {
                            if (src.id === srcID) {
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
            angular.forEach($scope.cats, function(cat, key) {
                if (cat.id === catID) {
                    cat.folded = !cat.folded;
                }
            });
        };

        // mark as read or unread on source item
        $scope.toggleItemReadState = function(cat, src, item) {
            //call server to store information
            $http({method: 'GET', url: url(resourceURL, "toggleItemReadState", cat.id, src.id, item.id, !item.read)}).
                    success(function(data) {
                (item.read ? src.unreadItemsNumber++ : src.unreadItemsNumber--);
                item.read = !item.read;
            });
        };

        // mark as read or unread all displayed source items
        $scope.markAllItemsAsRead = function(flag) {
            angular.forEach($scope.selectedCats, function(cat, key) {
                angular.forEach(cat.selectedSrcs, function(src, key) {
                    angular.forEach(src.items, function(item, key) {
                        if (item.read !== flag) {
                            $scope.toggleItemReadState(cat, src, item);
                        }
                    });
                });
            });
        };

        // evaluate is hideTree button should be displayed
        $scope.hideTreeDisplayed = function() {
            if (treeVisibleState === "NEVERVISIBLE")
                return false;
            if (treeVisibleState === "VISIBLE")
                return true;
        };

        // evaluate is showTree button should be displayed
        $scope.showTreeDisplayed = function() {
            if (treeVisibleState === "NEVERVISIBLE")
                return false;
            if (treeVisibleState === "NOTVISIBLE")
                return true;
        };

        // evaluate is tree should be displayed
        $scope.treeDisplayed = function() {
            if (treeVisibleState === "NEVERVISIBLE")
                return false;
            if (treeVisibleState === "VISIBLE")
                return true;
        };

        // show the tree
        $scope.showTree = function() {
            treeVisibleState = "VISIBLE";
        };

        // hide the tree
        $scope.hideTree = function() {
            treeVisibleState = "NOTVISIBLE";
        };

    });
    
    //Mode Filter
    project.filter('modeFilter', function() {
        var modeFilter = function(input, selectedMode) {
            var ret = new Array();
            var i = 0;
            angular.forEach(input, function(item, key) {
                ret.push(item);
                i++;
                if (item.read && (selectedMode === "notRead" || selectedMode === "unreadFirst")) {
                    ret.splice(i - 1, 1);
                    i--;
                }
                ;
            });
            angular.forEach(input, function(item, key) {
                if (item.read && selectedMode === "unreadFirst") {
                    ret.push(item);
                }
                ;
            });
            return ret;
        };
        return modeFilter;
    });
    

};

lectureEdit = function(appName, appHomePath, resourceURL) {
    var project = angular.module(appName, []);

    //config
    project.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
                when('/', {
            controller: 'editCtrl',
            templateUrl: appHomePath + '/views/edit.html'
        }).
                otherwise(
                {redirectTo: '/'}
        );
    }]);

    project.controller('editCtrl', function ($scope, $http) {
        //get context as JSON
        $http({method: 'GET', url: url(resourceURL, "getEditJSON")}).
                success(function(data) {
            //i18n messages
            $scope.msgs = data.messages;
            //categories
            $scope.cats = data.context.categories;
            //context name
            $scope.contextName = data.context.name;
            //first category selected by default
            $scope.selectedCat = $scope.cats[0];
        });        
        
        $scope.select = function(cat) {
            $scope.selectedCat = cat;
        };
    });
        
};

// ************* utils *************

//forge a portlet resource url
function url(pattern, id, p1, p2, p3, p4) {
    return pattern.
            replace("@@id@@", id).
            replace("__p1__", p1).
            replace("__p2__", p2).
            replace("__p3__", p3).
            replace("__p4__", p4);
}
