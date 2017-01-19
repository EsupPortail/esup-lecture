<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <c:if test="${contexte.modePublisher=='true'}"> --%>
<!-- 	<div class='row menuRubDropDown'> -->
<%-- 		<c:forEach items="${listCat}" var="cat"> --%>
<!-- 			<div class="col-xs-12 dropdown"> -->
<!-- 				<button class="dropdown-toggle btnRubriqueDropDown" type="button" -->
<!-- 					data-toggle="dropdown"> -->
<!-- 					<span class="caret pull-right"></span> -->
<!-- 				</button> -->
<!-- 				<div class="dropdown-menu menuListRubrique"> -->
<%-- 					<c:forEach items="${cat.sources}" var="src"> --%>
<!-- 						<div class="row ligneRubriqueMenu" -->
<%-- 							onclick="<portlet:namespace />filtrerParRubrique('${cat.id}','${src.id}')"> --%>
<%-- 							<c:out value="${src.name}"></c:out> --%>
<!-- 							<span class="badge pull-right" -->
<%-- 								style="background-color:${src.color}"><c:out --%>
<%-- 									value="${src.unreadItemsNumber}"></c:out></span> --%>
<!-- 						</div> -->
<%-- 					</c:forEach> --%>
<!-- 				</div> -->
<!-- 			</div> -->
<%-- 		</c:forEach> --%>
<!-- 	</div> -->
<%-- </c:if> --%>
<div class="row">
	<input type="hidden" id="<portlet:namespace />catSeletc" value='' /> <input
		type="hidden" id="<portlet:namespace />SrcSeletc" value='' /> <input
		type="hidden" id="<portlet:namespace />treeVisible"
		value="${contexte.treeVisible}" />
	<c:if test="${contexte.treeVisible=='true'}">
		<c:if test="${contexte.modePublisher=='true'}">
			<%@include file="listeRubZonePublisher.jsp"%>
		</c:if>
		<c:if test="${contexte.modePublisher=='false'}">
			<%@include file="listeRubZoneNonPublisher.jsp"%>
		</c:if>
	</c:if>
	<div class="col-xs-12 col-sm-9 divModeDesk"
		id="<portlet:namespace />divModeDesk">
		<div class="row">
			<div class="panel panel-default">
				<div class="panel panel-heading">
					<c:if test="${contexte.modePublisher=='true'}">
						<%@include file="enteteArticlePublisher.jsp"%>
					</c:if>
					<c:if test="${contexte.modePublisher=='false'}">
						<%@include file="enteteArticleNonPublisher.jsp"%>
					</c:if>
				</div>
				<div class="panel-body scrollDivArticle" id="<portlet:namespace />zoneArticles">
					<%@include file="articleZoneFiltre.jsp"%>
				</div>
			</div>
		</div>
	</div>
</div>