<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<nav class="col-xs-12 col-sm-3" id='<portlet:namespace />listOfCat'>
	<ul class="nav nav-pills nav-stacked">
		<li onclick="AfficherTout()"><strong><c:out
					value="${contexte.name}"></c:out></strong></li>
		<c:forEach items="${listCat}" var="cat">
		<c:set var="idcat" value="${fn:replace(cat.id,' ', '')}"></c:set>
			<c:set var="idcat" value="${fn:replace(idcat,':', '')}"></c:set>
			<li><c:if test="${not empty cat.sources}">
					<div class="cursPoint"
						onclick="filtrerParCategorie('${cat.id}')">
						<c:out value="${cat.name}"></c:out>
					</div>
					<div data-toggle="collapse" data-target="#liThem${idcat}">
						<span class="caret pull-right"></span>
					</div>
					<ul class="collapse" id="liThem${idcat}">
						<c:forEach items="${cat.sources}" var="src">
							<li><div class="row ligneRubriqueMenu cursPoint"
									onclick="filtrerParRubrique('${cat.id}','${src.id}','')">
									<c:out value="${src.name}"></c:out>
									<span class="badge pull-right"><c:out
											value="${src.unreadItemsNumber}"></c:out></span>
								</div></li>
						</c:forEach>
					</ul>
				</c:if></li>
		</c:forEach>
	</ul>
</nav>
