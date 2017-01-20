
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="row">
	<c:set var="affichereye" value="false"></c:set>
	<c:forEach items="${listCat}" var="cat">
		<c:if test="${cat.userCanMarkRead=='true'}">
			<c:set var="affichereye" value="true"></c:set>
		</c:if>
	</c:forEach>
	<c:if test="${affichereye=='false'}">
		<div class="col-xs-1 col-sm-1">
			<i class="fa fa-eye-slash fa-stack-1x"
				onclick="marquerToutItemLu(true)"
				data-toggle="tooltip" data-placement="top"
				title="marquer tous comme lu"></i>
		</div>
	</c:if>
	<c:if test="${contexte.treeVisible=='true'}">
		<div class="col-xs-1 col-sm-1">
			<i class="fa fa-arrows-alt fa-stack-1x mobileiconList"
				id="<portlet:namespace />cacherListRubrique" data-toggle="tooltip"
				data-placement="top" title="cacher la liste des rubriques"></i>
		</div>
		<div class="col-xs-1 col-sm-1">
			<i class="fa fa-columns fa-stack-1x mobileiconList"
				id="<portlet:namespace />afficherListRubrique" data-toggle="tooltip"
				data-placement="top" title="afficher la liste des rubriques"></i>
		</div>
	</c:if>
	<c:if test="${affichereye=='false'}">
		<div class="col-xs-1 col-sm-1">
			<i class="fa fa-eye fa-stack-1x"
				onclick="marquerToutItemLu(false)"
				data-toggle="tooltip" data-placement="top"
				title="marquer tous comme non Lu"></i>
		</div>
		<div class="col-xs-3 col-sm-3">
			<select id="<portlet:namespace />listNonLu"
				onchange="filtrerNonLus()">
				<option value="val1">Tous</option>
				<option value="val2">Non lus</option>
				<option value="val3">Non lus en premier</option>
			</select>
		</div>
	</c:if>
</div>