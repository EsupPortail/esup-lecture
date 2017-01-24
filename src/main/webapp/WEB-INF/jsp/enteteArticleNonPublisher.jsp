
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="row">
<!-- si au moin une catégorie a le tag userCanMarkRead à false on affiche pas la possibilité de marquer tous les artices àlu ou non lu -->
	<c:set var="affichereye" value="true"></c:set>
	<c:forEach items="${listCat}" var="cat"> 
		<c:if test="${cat.userCanMarkRead=='false'}">
			<c:set var="affichereye" value="false"></c:set>
		</c:if>
	</c:forEach>
	<div class="col-xs-12 col-sm-12">
		<div class="dropdown-toggle  pull-right" data-toggle="dropdown">
			<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
		</div>
		<ul class="dropdown-menu pull-right listOptionNonPublisher">		
		<c:if test="${contexte.userCanMarckRead=='true'&&affichereye=='true'}">
			<input type="hidden" id="<portlet:namespace />listNonLu" value="val1"/>
			<li onclick="filtrerNonLus('val2')">Afficher les articles Non lus</li>
			<li onclick="filtrerNonLus('val3')">Afficher les articles Non lus en premier</li>
			<li onclick="filtrerNonLus('val1')">Afficher tous les articles</li>
			<li onclick="marquerToutItemLu(true)">Marquer tous les articles lus</li>
			<li onclick="marquerToutItemLu(false)">Marquer tous les articles non lus</li>
		</c:if>
		<c:if test="${contexte.treeVisible=='true'}">
			<li id="<portlet:namespace />cacherListRubrique">Cacher l'arbre des catégories</li>
			<li id="<portlet:namespace />afficherListRubrique">Afficher l'arbre des catégories</li>
		</c:if>
		</ul>
	</div>
<%-- 	<c:set var="affichereye" value="false"></c:set> --%>
<%-- 	<c:forEach items="${listCat}" var="cat"> --%>
<%-- 		<c:if test="${cat.userCanMarkRead=='true'}"> --%>
<%-- 			<c:set var="affichereye" value="true"></c:set> --%>
<%-- 		</c:if> --%>
<%-- 	</c:forEach> --%>
<%-- 	<c:if test="${affichereye=='true'}"> --%>
<!-- 		<div class="col-xs-1 col-sm-1"> -->
<!-- 			<i class="fa fa-eye-slash fa-stack-1x" -->
<!-- 				onclick="marquerToutItemLu(true)" -->
<!-- 				data-toggle="tooltip" data-placement="top" -->
<!-- 				title="marquer tous comme lu"></i> -->
<!-- 		</div> -->
<%-- 	</c:if> --%>
<%-- 	<c:if test="${contexte.treeVisible=='true'}"> --%>
<!-- 		<div class="col-xs-1 col-sm-1"> -->
<!-- 			<i class="fa fa-arrows-alt fa-stack-1x mobileiconList" -->
<%-- 				id="<portlet:namespace />cacherListRubrique" data-toggle="tooltip" --%>
<!-- 				data-placement="top" title="cacher la liste des rubriques"></i> -->
<!-- 		</div> -->
<!-- 		<div class="col-xs-1 col-sm-1"> -->
<!-- 			<i class="fa fa-columns fa-stack-1x mobileiconList" -->
<%-- 				id="<portlet:namespace />afficherListRubrique" data-toggle="tooltip" --%>
<!-- 				data-placement="top" title="afficher la liste des rubriques"></i> -->
<!-- 		</div> -->
<%-- 	</c:if> --%>
<%-- 	<c:if test="${affichereye=='true'}"> --%>
<!-- 		<div class="col-xs-1 col-sm-1"> -->
<!-- 			<i class="fa fa-eye fa-stack-1x" -->
<!-- 				onclick="marquerToutItemLu(false)" -->
<!-- 				data-toggle="tooltip" data-placement="top" -->
<!-- 				title="marquer tous comme non Lu"></i> -->
<!-- 		</div> -->
<!-- 		<div class="col-xs-3 col-sm-3"> -->
<%-- 			<select id="<portlet:namespace />listNonLu" --%>
<!-- 				onchange="filtrerNonLus()"> -->
<!-- 				<option value="val1">Tous</option> -->
<!-- 				<option value="val2">Non lus</option> -->
<!-- 				<option value="val3">Non lus en premier</option> -->
<!-- 			</select> -->
<!-- 		</div> -->
<%-- 	</c:if> --%>
</div>