<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="row">
	<div
		class="col-sm-12 col-xs-12 portlet-title round-top padding-botom-head">
		<div class="row">
			<div class="col-sm-5 col-xs-5"
				onclick="<portlet:namespace />AfficherTouteActualite()">
				<c:out value="Actualités non lues:"></c:out>
				<strong><c:out value="${contexte.nbrUnreadItem}"></c:out></strong>
			</div>
			<c:if test="${contexte.lienVue!=null && contexte.lienVue!=''}">
				<div class="col-sm-7 col-xs-7">
					<a type="button" class="btn btn-default pull-right large-btn"
						href="${contexte.lienVue}">VOIR TOUT</a>
				</div>
			</c:if>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-12 col-xs-12">
		<div class="panel-body col-sm-12 col-xs-12 scrollDivArticle"
			id="<portlet:namespace />zoneArticles">
			<c:forEach items="${listeItemAcceuil}" var="article">
				<c:if test="${article.read=='true'}">
					<input type="hidden" id="arti${article.id}"
						class="listeIdArti${contexte.modePublisher}"
						value="contenuArti${contexte.modePublisher}${article.id}" />
				</c:if>
				<div class="row"
					id="contenuArti${contexte.modePublisher}${article.id}">
					<c:out value="${article.htmlContent}" escapeXml="false"></c:out>
				</div>
			</c:forEach>
		</div>
	</div>
</div>