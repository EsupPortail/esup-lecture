
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="row enteteEcranLarge">
<!--   "col-xs-7 col-sm-7" -->
  <div class="pull-left"
 
    id="${n}rubSelectedDiv1">
    <label class="rubrique_Active ${n}">Toutes les actualités</label>
  </div>

        <div class="dropdown-toggle  pull-right" data-toggle="dropdown">
          <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
        </div>
        <portlet:actionURL var="submitFormURL">
          <portlet:param name="action" value="filteredItem"/>
        </portlet:actionURL>
        <div class="dropdown-menu pull-right">
          <div class="checkbox afficherLuWith">

            <form id="formReadMode<portlet:namespace />"
              action="${submitFormURL}" method="post">
              <input type="hidden" name="p3"
                id="<portlet:namespace />listNonLu" value="" /> <input
                name="idContexte" type="hidden" value="${contexte.id}">
              <c:if test="${contexte.itemDisplayMode=='UNREAD'}">
                <label> <spring:message code="showUnreadNews" />&nbsp;
                </label>
                <input class="checkReadItem" type="checkbox"
                  id="<portlet:namespace />checkBoxNonLu" checked="checked">
                <!--                  onchange="filtrerPublisherNonLus()" -->

              </c:if>

              <c:if test="${contexte.itemDisplayMode=='ALL'}">
                <label> <spring:message code="showUnreadNews" />&nbsp;
                </label>
                <input class="checkReadItem" type="checkbox"
                  onchange="filtrerPublisherNonLus()"
                  id="<portlet:namespace />checkBoxNonLu">
                <!--                 onchange="filtrerPublisherNonLus()" -->
              </c:if>
            </form>
          </div>
  </div>
</div>
<div class="row menuRubDropDown" id="mainmenurow<portlet:namespace />">
  <div id="mainmenu<portlet:namespace />" class="col-xs-10 col-sm-11">
    <div id="<portlet:namespace />rubSelectedDiv2" data-toggle="modal"
      data-target="#myModal<portlet:namespace />">
      <label class="rubrique_Active ${n}"> Toutes les actualités</label> <span class="caret margeCarret"></span>
    </div>
  </div>
  <div class="dropdown pull-right col-xs-2 col-sm-1">
    <div class="dropdown-toggle  pull-right" data-toggle="dropdown">
      <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
    </div>
    <div class="dropdown-menu pull-right">
      <div class="checkbox afficherLuWith">
        <form id="formReadMode<portlet:namespace /> "
          action="${submitFormURL}" method="post">
          <input type="hidden" name="p3" id="<portlet:namespace />listNonLu"
            value="" /> <input name="idContexte" type="hidden"
            value="contexte.id"> <label> Afficher uniquement
            les actualités non lues&nbsp;</label>
          <c:if test="${contexte.itemDisplayMode=='UNREAD'}">
            <input type="checkbox" id="<portlet:namespace />checkBoxNonLu2"
               checked="checked">
<!-- 							 onchange="filtrerPublisherNonLusMobile()" -->
          </c:if>
          <c:if test="${contexte.itemDisplayMode=='ALL'}">
            <input type="checkbox" id="<portlet:namespace />checkBoxNonLu2"
              >
<!-- 							onchange="filtrerPublisherNonLusMobile()" -->
          </c:if>
        </form>
      </div>
    </div>
  </div>

</div>
<div id="myModal${n}" class="modal fade" role="dialog">
  <div class="modal-dialog modalMarge">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-body modalPadding">
        <a href="#" class="list-group-item" onclick="filterByRubriqueClass('rubrique_all','${n}')"
          data-dismiss="modal"><c:out value="Toutes les actualités"></c:out>
          <span class="badge pull-right"><c:out
              value="${nombreArticleNonLu}"></c:out></span> </a>
        <c:forEach items="${listCat}" var="cat">
          <c:forEach items="${cat.sources}" var="src">
            <a href="#" class="list-group-item"
              onclick="filterByRubriqueClass('rubrique_${src.uid}','${n}')"
              data-dismiss="modal"><c:out value="${src.name}"></c:out><span
              class="badge pull-right" style="background-color:${src.color}"><c:out
                  value="${src.unreadItemsNumber}"></c:out></span></a>
          </c:forEach>
        </c:forEach>
      </div>
    </div>
  </div>
</div>
<!-- onclick="filtrerParRubrique('${cat.id}','${src.id}','${src.name}','')" -->
