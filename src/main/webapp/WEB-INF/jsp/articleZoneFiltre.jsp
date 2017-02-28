<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${contexte.itemDisplayMode=='ALL'}">
  <c:forEach items="${listCat}" var="cat">
    <c:forEach items="${cat.sources}" var="src">
      <c:forEach items="${src.items}" var="article">

        <c:set var="idSources" value="${fn:replace(src.id,' ', '')}"></c:set>
        <c:set var="idSources" value="${fn:replace(idSources,':', '')}"></c:set>
        <div class="itemShowFilter">
        <input type="hidden" class="itemShowFilterIsRead" value="${article.read}"/>
          <div class='row'>
            <div class='col-xs-10 col-sm-11'
              class="contenuArti${contexte.modePublisher}${article.id}"
              id="contenuArti${contexte.modePublisher}${article.id}${idSources}">
              <c:out value="${article.htmlContent}" escapeXml="false"></c:out>
            </div>
            <input type="hidden" class="eye${article.id}"
              value="${article.id}${idSources}" />
            <c:if test="${cat.userCanMarkRead=='true'}">
              <div class='col-xs-2 col-sm-1 articleDiv'
                id="eye${article.id}${idSources}">
                <c:if test="${article.read=='true'}">
                  <input type="hidden" id="arti${article.id}${idSources}"
                    class="listeIdArti${contexte.modePublisher}"
                    value="contenuArti${contexte.modePublisher}${article.id}${idSources}" />
                  <i class="fa fa-eye-slash fa-stack-1x"
                    onclick="marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read},${contexte.modePublisher})"></i>
                </c:if>
                <c:if test="${article.read=='false'}">
                  <i class="fa fa-eye fa-stack-1x"
                    onclick="marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read},${contexte.modePublisher})"></i>
                </c:if>
              </div>
            </c:if>
          </div>
          <c:if test="${not empty article.rubriques}">
            <div class='row'>
              <div class="col-xs-3 col-sm-2"></div>
              <div class="col-xs-8 col-sm-8 paddingLeft">
                <c:forEach items="${article.rubriques}" var="rub">
                <input type="hidden" class="uidDesRubriquesItem" value="${rub.uid}"/>
                  <span class="badge cursPoint"
                    style="background-color:${rub.couleur}"
                    onclick="filtrerParRubrique('${cat.id}','','${rub.nom}')"><c:out
                      value="${rub.nom}"></c:out></span>
                </c:forEach>
              </div>
            </div>
          </c:if>
          </div>
      </c:forEach>
    </c:forEach>
  </c:forEach>
</c:if>
<c:if test="${contexte.itemDisplayMode=='UNREAD'}">
  <c:forEach items="${listCat}" var="cat">
    <c:forEach items="${cat.sources}" var="src">
      <c:forEach items="${src.items}" var="article">
         <div class="itemShowFilter">
         <input type="hidden" class="itemShowFilterIsRead" value="${article.read}"/>
        <c:if test="${article.read=='false'}">
          <c:set var="idSources" value="${fn:replace(src.id,' ', '')}"></c:set>
          <c:set var="idSources" value="${fn:replace(idSources,':', '')}"></c:set>
          <div class='row'>
            <div class='col-xs-11 col-sm-11'
              class="contenuArti${contexte.modePublisher}${article.id}"
              id="contenuArti${contexte.modePublisher}${article.id}${idSources}">
              <c:out value="${article.htmlContent}" escapeXml="false"></c:out>
            </div>
            <input type="hidden" class="eye${article.id}"
              value="${article.id}${idSources}" />
            <c:if test="${cat.userCanMarkRead=='true'}">
              <div class='ol-xs-1 col-sm-1 articleDiv'
                id="eye${article.id}${idSources}">
                <c:if test="${article.read=='true'}">
                  <input type="hidden" id="arti${article.id}${idSources}"
                    class="listeIdArti${contexte.modePublisher}"
                    value="contenuArti${contexte.modePublisher}${article.id}${idSources}" />
                  <i class="fa fa-eye-slash fa-stack-1x"
                    onclick="marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read},${contexte.modePublisher})"></i>
                </c:if>
                <c:if test="${article.read=='false'}">
                  <i class="fa fa-eye fa-stack-1x"
                    onclick="marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read},${contexte.modePublisher})"></i>
                </c:if>
              </div>
            </c:if>
          </div>
          <c:if test="${not empty article.rubriques}">
            <div class='row esup_lecture_margin_bottom rubriqueArticle'>
              <div class="col-xs-4 col-sm-2"></div>
              <div class="col-xs-8 col-sm-8">
                <c:forEach items="${article.rubriques}" var="rub">
                  <input type="hidden" class="uidDesRubriquesItem" value="${rub.uid}"/>
                  <span class="badge cursPoint rubriqueFiltre"
                    style="background-color:${rub.couleur}"
                    ><c:out
                      value="${rub.nom}"></c:out><input type="hidden" class="srcName" value="${rub.nom}"/><input type="hidden" class="srcId" value="${rub.uid}"/></span>
<%--                       onclick="filtrerParRubrique('${cat.id}','','${rub.nom}')" --%>
                </c:forEach>
              </div>
            </div>
          </c:if>
        </c:if>
        </div>
      </c:forEach>
    </c:forEach>
  </c:forEach>
</c:if>
