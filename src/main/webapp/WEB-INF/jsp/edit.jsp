<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script type="text/javascript">
	angular.element(document).ready(function() {
			lecture("<portlet:namespace/>", "${resourcesPath}/app", "${dynamicResourcesPattern}");
			angular.bootstrap(angular.element(document.getElementById("lecture-<portlet:namespace/>")), ["<portlet:namespace/>"]);
		});
</script>
<div class="esup-lecture" id="lecture-<portlet:namespace/>" ng-view class="container-fluid">
    EDIT!!!!!
</div>
