<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="app"><portlet:namespace/></c:set>
<script type="text/javascript">appName = "${app}";url = "<portlet:resourceURL id="ajaxCall"/>";</script>
<script type="text/javascript" src="${resourcesPath}/app/js/app.js"></script>
<div id="main" ng-app="${app}" ng-controller="testCtrl" class="container-fluid">
    <div class="span4">
        {{context.Name}}
        <ul ng-repeat="cat in cats">
            <li ng-click="selectCat(cat.id)" class="btn-link">{{cat.name}}</li>
            <ul ng-repeat="source in cat.sources">
                <li>{{source.name}}</li>
            </ul>
        </ul>
    </div>
    <div class="span8">
        <ul ng-repeat="cat in selectedCats">
            <li>{{ cat.name }}</li>
            <ul ng-repeat="source in cat.sources">
                <li>{{ source.name }}</li>
                <ul ng-repeat="item in source.items">
                    <li><p ng-bind-html-unsafe="item.htmlContent"></p></li>
                </ul>
            </ul>
        </ul>
    </div>
</div>
