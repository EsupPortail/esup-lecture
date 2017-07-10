<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:message code="showAllItems" var="default_ctxTextFilter" scope="session"></spring:message>
<spring:message code="${ctx_name}.showAllItems" var="ctxTextFilter" text="${default_ctxTextFilter}"></spring:message>
<spring:message code="showUnreadNews" var="default_showUnreadNews" scope="session"></spring:message>
<spring:message code="${ctx_name}.showUnreadNews" var="showUnreadNews" text="${default_showUnreadNews}"></spring:message>

<div id="${n}homeAutreJsp" class="row">
  <input type="hidden" id="${n}catSeletc" value='' /> <input
    type="hidden" id="${n}SrcSeletc" value='' />
    <input type="hidden"
    id="${n}rubSeletc" value='' />
    <c:if test="${contexte.treeVisible=='true'}">
      <div class="col-sm-3 .hidden-xs menuGauche">

      <c:if test="${contexte.modePublisher=='true'}">
        <%@include file="listeRubZonePublisher.jsp"%>
      </c:if>
      <c:if test="${contexte.modePublisher=='false'}">
        <%@include file="listeRubZoneNonPublisher.jsp"%>
      </c:if>
  </div>
  </c:if>
  <c:choose>
  <c:when test="${contexte.treeVisible=='true'}">
    <div class="col-sm-9 col-xs-12 divModeDesk" id="${n}divModeDesk">
  </c:when>
  <c:otherwise>
    <div class="col-xs-12 divModeDesk" id="${n}divModeDesk">
  </c:otherwise>
  </c:choose>
    <!--  div -->
      <div class="panel panel-default ${n} ${contexte.itemDisplayMode=='UNREAD' ? 'nonLueSeulement' : ''}">
        <div class="panel panel-heading largeHeadPanel ${contexte.viewDef ? 'panelHome' : '' }"
          id="${n}fixHead${contexte.modePublisher}">
          <c:if test="${contexte.modePublisher=='true'}">
            <%@include file="enteteArticlePublisher.jsp"%>
          </c:if>
          <c:if test="${contexte.modePublisher=='false'}">
            <%@include file="enteteArticleNonPublisher.jsp"%>
          </c:if>
        </div>
        <div class="panel-body scrollDivArticle" id="${n}zoneArticles">
          <%@include file="articleZoneFiltre.jsp"%>
        </div>
      </div>
    </div>
  <!--/div -->
</div>