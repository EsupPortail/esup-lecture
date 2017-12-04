<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- ce template n'est pas utilisé pour publisher -->
<c:forEach items="${listCat}" var="cat">
	<c:forEach items="${cat.sources}" var="src">
		<c:forEach items="${src.items}" var="article">
				<div class='row'>
					<div class='col-xs-11 col-sm-11'>
						<c:out value="${article.htmlContent}" escapeXml="false"></c:out>
					</div>
					<div class='col-xs-1 col-sm-1 articleDiv' id="eye${article.id}">
						<c:if test="${isRead=='true'}">
							<i class="fa fa-eye-slash fa-stack-1x"
								onclick="lecture['${n}'].marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read},false)"></i>
						</c:if>
						<c:if test="${isRead=='false'}">
							<i class="fa fa-eye fa-stack-1x"
								onclick="lecture['${n}'].marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read},false)"></i>
						</c:if>
					</div>
				</div>
		</c:forEach>
	</c:forEach>
</c:forEach>
