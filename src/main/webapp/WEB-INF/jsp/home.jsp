<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="app"><portlet:namespace/></c:set>
<script type="text/javascript">appName = "${app}";</script>
<script type="text/javascript" src="${resourcesPath}/media/js/app.js"></script>
<div id="main" class="portlet-section-body esup-lecture" ng-app="${app}">
	<div ng-controller="testCtrl">
		<a href="<portlet:resourceURL id="ajaxCall"/>">JSON</a><br>
		<hr>
		<button ng-click="getCats('<portlet:resourceURL id="ajaxCall"/>')">GET cat</button>
		{{contextName}}
		<ul ng-repeat="cat in cats">
			<li>{{cat.name}}</li>
			<ul ng-repeat="source in cat.sources">
				<li>{{source.name}}</li>
				<ul ng-repeat="item in source.items">
					<li>{{item.id}}</li>
					<li><p ng-bind-html-unsafe="item.htmlContent" /></li>					
				</ul>
			</ul>
		</ul>	
		<hr>
		<input ng-model="test"> --> D{{test}}F
		<p>{{toto}}</p>
	</div>
</div>
