<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server"%>
<rs:aggregatedResources path="/resourcesLecture.xml" />
<portlet:resourceURL id='toggleItemReadState' var="toggleItemReadState"></portlet:resourceURL>
<portlet:resourceURL id='toggleAllItemReadState'
	var="toggleAllItemReadState"></portlet:resourceURL>
<portlet:resourceURL id='FilteredItem' var="FilteredItem"></portlet:resourceURL>
<c:set var="portletNameSpace"><portlet:namespace/></c:set>
<script type="text/javascript">
	(function($, namespace, portletId, urlMarkRead, urlMarkAllRead, urlFiltrItem) {
		lecture.init($, namespace, portletId, urlMarkRead, urlMarkAllRead, urlFiltrItem);
	})(up.jQuery, '#lecture-${portletNameSpace}', '${portletNameSpace}', '${toggleItemReadState}', '${toggleAllItemReadState}', '${FilteredItem}');
</script>
<div class="esup-lecture portlet-container"
	id="lecture-<portlet:namespace/>">
	<div class="container-fluid">
		<span class="spinner"></span>
		<c:if
			test="${contexte.viewDef=='true'&& contexte.modePublisher=='true'}">
			<%@include file="homeAccueil.jsp"%>
		</c:if>
		<c:if
			test="${contexte.viewDef=='false'||contexte.modePublisher=='false'}">
			<%@include file="homeAutre.jsp"%>
		</c:if>
	</div>
</div>
