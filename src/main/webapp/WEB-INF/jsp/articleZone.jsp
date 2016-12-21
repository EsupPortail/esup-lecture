<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:forEach items="${listCat}" var="cat">
	<c:forEach items="${cat.sources}" var="src">
		<c:forEach items="${src.items}" var="article">
			<div class='row'>
				<div class='col-md-11'>
					<c:out value="${article.htmlContent}" escapeXml="false"></c:out>
				</div>
				<div class='col-md-1 articleDiv' id="eye${article.id}">
					<c:if test="${isRead=='true'}">
						<i class="fa fa-eye-slash fa-stack-1x"
							onclick="<portlet:namespace />marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read})"></i>
					</c:if>
					<c:if test="${isRead=='false'}">
						<i class="fa fa-eye fa-stack-1x"
							onclick="<portlet:namespace />marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read})"></i>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</c:forEach>
</c:forEach>
