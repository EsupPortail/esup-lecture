<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<rs:aggregatedResources path="/resources.xml"/>

<div class="esup-lecture portlet-container" id="lecture-<portlet:namespace/>" ng-view>
</div>
<rs:aggregatedResources path="/resources-footer.xml"/>
<script type="text/javascript">
    angular.element(document).ready(function() {
        lecture("<portlet:namespace/>", "${resourcesPath}/app", "${dynamicResourcesPattern}");
        angular.bootstrap(angular.element(document.getElementById("lecture-<portlet:namespace/>")), ["<portlet:namespace/>"]);
    });
</script>
