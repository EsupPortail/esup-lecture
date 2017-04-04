
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class=" enteteLectureAll">
<!--   "col-xs-7 col-sm-7" -->
  <div class="pull-left noMenuDropDown" id="${n}rubSelectedDiv1">
    <label class="rubrique_Active ${n}">Toutes les actualités</label>
  </div>
  <div id="${n}rubSelectedDiv2" class="pull-left withMenuDropDown" data-toggle="modal"
      data-target="#myModal<portlet:namespace />">
      <label class="rubrique_Active ${n}"> Toutes les actualités</label> <span class="caret margeCarret"></span>
   </div>
	<div class="dropdown readNotRead">	
	<!--		
        <div class="dropdown-toggle  pull-right" data-toggle="dropdown">
          <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
        </div>
    --> 
    	<button  class="btn btn-primary dropdown-toggle pull-right" type="button" data-toggle="dropdown"> 
    		<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
    	</button>
    	 <portlet:actionURL var="submitFormURL">
          <portlet:param name="action" value="filteredItem"/>
        </portlet:actionURL>
        <ul class="dropdown-menu pull-right">
          <li class="checkbox afficherLuWith">
<!--  
            <form id="formReadMode${n}" action="${submitFormURL}" method="post">
              <input 	type="hidden" 
              			name="p3"
                		id="${n}listNonLu" 
                		value="" /> 
              <input	name="idContexte" 
              			type="hidden" 
              			value="${contexte.id}" />
    -->        
                <label for="checkReadItem${n})" onclick="lecture.jq('input#checkReadItem${n}').click()"> <spring:message code="showUnreadNews" />&nbsp;
                </label>
                <input 	id = "checkReadItem${n}"
                		class="checkReadItem" 
                		type="checkbox"
                		value="${contexte.itemDisplayMode=='UNREAD'}"
                  		onchange="filterPublisherNotRead('${n}', this); lecture.jq('#${n}rubSelectedDiv1 ').click();"
                  id="${n}checkBoxNonLu">
                <!--                 onchange="filtrerPublisherNonLus()" -->
       <!--       
            </form>
         --> 
          </li>
  </ul>
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
