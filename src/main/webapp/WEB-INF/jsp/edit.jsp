<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<rs:aggregatedResources path="/resources.xml"/>

<div class="esup-lecture portlet-container">
    <div id="lecture-<portlet:namespace/>" ng-view></div>
    <portlet:actionURL var="actionURL">
        <portlet:param name="action" value="back"/>
    </portlet:actionURL>
    <spring:message var="libBack" code="back" />
    <form:form method="post" action="${actionURL}">
        <div class="row">
            <div class="col-xs-2 col-xs-offset-5">
                <input type="submit" class="btn btn-primary btn-block" />
            </div>
        </div>
    </form:form>
</div>

