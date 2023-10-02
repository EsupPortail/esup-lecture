<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:message code="showUnreadNews" var="default_showUnreadNews" scope="session"></spring:message>
<spring:message code="${ctx_name}.showUnreadNews" var="showUnreadNews" text="${default_showUnreadNews}"></spring:message>
<c:set var="affichereye" value="true"></c:set>
<c:forEach items="${listCat}" var="cat">
  <c:if test="${contexte.userCanMarkRead=='false' || cat.userCanMarkRead=='false'}">
    <c:set var="affichereye" value="false"></c:set>
  </c:if>
</c:forEach>
<div id="${n}homeAccueilJspDiv1">
  <div class="round-top padding-botom-head">
    <div>
      <div class="row panel panelHome">
        <div class="col-sm-7 col-xs-7"
          onclick="lecture.${n}.AfficherTouteActualite()">
          <spring:message code="unreadNews" var="default_unreadNews" scope="session"></spring:message>
          <spring:message code="${ctx_name}.unreadNews" text="${default_unreadNews}"></spring:message>
          <c:choose>
             <c:when test="${contexte.nbrUnreadItem>0}">
                <span class="badge pull" style="background-color:red"><c:out value="${contexte.nbrUnreadItem}"></c:out></span>
             </c:when>
             <c:otherwise>
                <span class="badge pull" style="background-color:green"><c:out value="${contexte.nbrUnreadItem}"></c:out></span>
             </c:otherwise>
          </c:choose>
        </div>
        <c:if test="${contexte.lienVue!=null && contexte.lienVue!=''}">
          <div class="col-sm-5 col-xs-5">
            <a type="button" class="btn btn-default pull-right large-btn"
              href="${contexte.lienVue}"><spring:message
                code="seeAll" /></a>
          </div>
        </c:if>
        <c:if test="${contexte.userCanMarkRead=='true' && affichereye=='true'}">
            <div class="dropdown readNotRead ">
                <button class="btn btn-primary dropdown-toggle pull-right" type="button" data-toggle="dropdown">
                    <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
                </button>
                <portlet:actionURL var="submitFormURL">
                    <portlet:param name="action" value="filteredItem"/>
                </portlet:actionURL>
                <ul class="dropdown-menu pull-right">
                    <li class="checkbox afficherLuWith">
                        <label for="checkReadItem${n})" onclick="lecture['${n}'].jq('input#checkReadItem${n}').click()"> <c:out value="${showUnreadNews}"></c:out>&nbsp;</label>
                        <input id="checkReadItem${n}" class="checkReadItem" type="checkbox" ${contexte.itemDisplayMode=='UNREAD'? 'checked' : ''}
                        onchange="lecture.${n}.filterPublisherNotReadAccueil(this); lecture['${n}'].jq('#${n}rubSelectedDiv1 ').click();" id="${n}checkBoxNonLuAccueil">
                    </li>
                </ul>
            </div>
        </c:if>
      </div>
    </div>
  </div>
</div>
<div id="${n}homeAccueilJspDiv2">
    <div>
        <div class="panel-body scrollDivArticle ${n}" id="<portlet:namespace />zoneArticles">
            <c:set var="nbArticles" value="0" />
            <c:forEach items="${listeItemAcceuil}" var="article">
                <div id="contenuArti${contexte.modePublisher}${article.id}" class="itemOpacifiable  ${nbArticles==0 ? 'premierVisible' : ''} ${article.read ? 'dejaLue' : ''} ${article.rubriques != null ? 'modePublisher' : 'modeNoPublisher'} contenuArticle" ${contexte.itemDisplayMode=='UNREAD' ? 'hidden' : ''}>
                    <c:out value="${article.htmlContent}" escapeXml="false"></c:out>
                </div>
                <c:set var="nbArticles" value="${nbArticles+1}" />
            </c:forEach>
            <c:set var="nbArticles" value="0" />
            <c:forEach items="${listeItemAcceuilMasquerDejaLues}" var="article">
                <div id="contenuArti${contexte.modePublisher}${article.id}" class="itemOpacifiable  ${nbArticles==0 ? 'premierVisible' : ''} ${article.read ? 'dejaLue' : ''} ${article.rubriques != null ? 'modePublisher' : 'modeNoPublisher'} contenuArticle" ${contexte.itemDisplayMode=='UNREAD' ? '' : 'hidden'}>
                    <c:out value="${article.htmlContent}" escapeXml="false"></c:out>
                </div>
                <c:set var="nbArticles" value="${nbArticles+1}" />
            </c:forEach>
        </div>
    </div>
</div>