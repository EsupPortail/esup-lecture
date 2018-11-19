<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%--
	affiche la liste des rubrique sur le cote pour les ecrans large
 --%>
 <c:set var="nbCat"  value="0" />
 <c:set var="nbSrc" value="0" />
<div class="navModeDesk navClass" id='${n}listOfCat'>
	
  <ul 	class="nav nav-pills nav-stacked menuRubrique affix"
    	id='${n}menuRubrique'>
    <li>
      <div class="row divLargeWith rubriqueFiltre rubrique_all ${n} active"
      		onclick="lecture.${n}.filterByRubriqueClass('rubrique_all')">
          <c:out value="${ctxTextFilter}"></c:out>
          <c:if test="${contexte.userCanMarkRead=='true' && affichereye=='true'}">
              <span class="badge pull-right"><span data-idSrc="all"><c:out value="${nombreArticleNonLu}"></c:out></span></span>
          </c:if>
          <input type="hidden" class="srcId" value="toutRub"/>
          <input type="hidden" class="titleName" value="${ctxTextFilter}"/>
      </div>
    </li>
      <c:forEach items="${listCat}" var="cat">
      	<c:set var="nbCat" value="${nbCat+1}" />
     	<c:if var="notFromPublisher" test="${!cat.fromPublisher}" >
     		<%@include file="rubZoneNonPublisher.jsp"%>
     	</c:if>
     	<c:if test="${!notFromPublisher}" >
        <c:forEach items="${cat.sources}" var="src">
        	<c:set var="nbSrc" value="${nbSrc+1}" />
          <li><div class="row divLargeWith rubriqueFiltre rubrique_${src.uid} ${n}"
                   onclick="lecture.${n}.filterByRubriqueClass('rubrique_${src.uid}')">
              <c:out value="${src.name}"></c:out>
                  <span class="badge pull-right ${(contexte.userCanMarkRead=='true' && cat.userCanMarkRead=='true') ? '' : 'emptyTextCircle'}"
                    style="background-color:${src.color}">
                    <c:if test="${contexte.userCanMarkRead=='true' && cat.userCanMarkRead=='true'}">
                      <span data-idSrc="${src.id}"><c:out value="${src.unreadItemsNumber}"></c:out></span>
                    </c:if>
                  </span>
              <input type="hidden" class="srcId" value="${src.uid}"/>
              <input type="hidden" class="titleName" value="<c:out value="${src.name}" />"/>
            </div></li>
        </c:forEach>
        </c:if>
      </c:forEach>

  </ul>
</div>