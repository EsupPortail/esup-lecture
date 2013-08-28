<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="app"><portlet:namespace/></c:set>
<script type="text/javascript">appName = "${app}";url = "<portlet:resourceURL id="ajaxCall"/>"; appHomePath = "${resourcesPath}/app";</script>
<script type="text/javascript" src="${resourcesPath}/app/js/app.js"></script>
<div id="main" ng-app="${app}" ng-view class="container-fluid">
</div>
