<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="navModeDesk navClass"
	id='<portlet:namespace />listOfCat'>
	<ul class="nav nav-pills nav-stacked menuRubrique"
		id='<portlet:namespace />menuRubrique'>
		<li>
			<div class="row divLargeWith" onclick="AfficherTout('')">
				Toutes les actualités<span class="badge pull-right"><c:out
						value="${nombreArticleNonLu}"></c:out></span>
			</div>
		</li>
		<c:forEach items="${listCat}" var="cat">
			<c:forEach items="${listCat}" var="cat">
				<c:forEach items="${cat.sources}" var="src">
					<li><div class="row divLargeWith"
							onclick="filtrerParRubrique('${cat.id}','${src.id}','${src.name}','')">
							<c:out value="${src.name}"></c:out>
							<span class="badge pull-right"
								style="background-color:${src.color}"><c:out
									value="${src.unreadItemsNumber}"></c:out></span>
						</div></li>
				</c:forEach>
			</c:forEach>
		</c:forEach>
	</ul>
</div>
