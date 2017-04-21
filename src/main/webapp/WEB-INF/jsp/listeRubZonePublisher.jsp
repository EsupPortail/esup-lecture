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
<div class="navModeDesk navClass" id='${n}listOfCat' >
	
  <ul 	class="nav nav-pills nav-stacked menuRubrique affix"
    	id='${n}menuRubrique'>
    <li>
      <div 	class="row divLargeWith rubriqueFiltre rubrique_all  ${n} active" 
      		onclick="lecture.${n}.filterByRubriqueClass('rubrique_all')">
        Toutes les actualités<span class="badge pull-right"><c:out
            value="${nombreArticleNonLu}"></c:out>
            <input type="hidden" class="srcId" value="toutRub"/>
            <input type="hidden" class="titleName" value="Toutes les actualités"/></span>
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
          <li><div 	class="row divLargeWith rubriqueFiltre rubrique_${src.uid} ${n}"
          			onclick="lecture.${n}.filterByRubriqueClass('rubrique_${src.uid}'); true"
          			>
             
              <c:out value="${src.name}"></c:out>
              <span class="badge pull-right"
                style="background-color:${src.color}"><c:out
                  value="${src.unreadItemsNumber}"></c:out>
                  <input type="hidden" class="srcId" value="${src.uid}"/>
                  <input type="hidden" class="titleName" value="${src.name}"/></span>
            </div></li>
        </c:forEach>
        </c:if>
      </c:forEach>

  </ul>
</div>
<%--  onclick="filtrerParRubrique('${cat.id}','${src.id}','${src.name}','')" --%>
<!-- onclick="AfficherTout('')" 

          			

-->