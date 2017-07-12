
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<%--
Partie supperieur de l'affichage des articles avec les boutons et dropdown de selection qui faut quand il faut.
 --%>
 <c:if var="treeVisible" test="${contexte.treeVisible=='true'}" ></c:if>
<c:if var="pageAccueil" test="${contexte.viewDef=='true'}" ></c:if>
<c:if var="withDropDown" test="${!pageAccueil && treeVisible}" ></c:if>	
 
 <c:if var="withDropDown" test="${!pageAccueil && treeVisible}" ></c:if>
<div class="enteteLectureAll "  }>
	
	<c:if test="${withDropDown}">
		<div class="pull-left noMenuDropDown " 
				>	
			<label class="rubrique_Active ${n}"><c:out value="${contexte.name}"></c:out></label>
		</div>
		<div data-spy="affix" data-offset-top="48">
		<div 	class="pull-left withMenuDropDown cursPoint"
				data-toggle="modal"
      			data-target="#modalRubriqueList${n}">	
			<label class="cursPoint rubrique_Active ${n}"><c:out value="${contexte.name}"></c:out></label>
			<span class="caret"></span>
		</div>
		</div>
	</c:if>
	<c:if test="${!withDropDown}" >
		<div class="pull-left" >	
			<label class="rubrique_Active ${n}"><c:out value="${contexte.name}"></c:out></label>
		</div>
	</c:if>
	
	<div class="pull-right">
		<c:if test="${pageAccueil}">
			<c:if test="${contexte.lienVue!=null && contexte.lienVue!=''}">
				<div>
					<a type="button" class="btn btn-default pull-right large-btn"
						href="${contexte.lienVue}">VOIR TOUT</a>
				</div>
			</c:if>
		</c:if>
		<c:if test="${contexte.userCanMarkRead=='true' && affichereye=='true'}">
			<button class="dropdown-toggle  pull-right" data-toggle="dropdown">
				<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
			</button>
			<ul class="dropdown-menu pull-right">
          		<li class="checkbox afficherLuWith">
          		             <label for="checkReadItem${n})" onclick="lecture['${n}'].jq('input#checkReadItem${n}').click()"> <c:out value="${showUnreadNews}"></c:out>&nbsp;
					</label>
					<input 	id = "checkReadItem${n}"
                		class="checkReadItem" 
                		type="checkbox"
                		${contexte.itemDisplayMode=='UNREAD'? 'checked' : ''}
                  		onchange="lecture.${n}.filterPublisherNotRead(this); lecture['${n}'].jq('#${n}rubSelectedDiv1 ').click();"
                  		id="${n}checkBoxNonLu">
          		</li>
  			</ul>
		</c:if>
	</div>
</div>
<c:set var="nbCat"  value="0" />
<c:set var="nbSrc" value="0" />

<div id="modalRubriqueList${n}" class="modal fade" role="dialog">
  <div class="modal-dialog modalMarge">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-body modalPadding withCategoriesAndSources">
        <a href="#" class="list-group-item" onclick="lecture.${n}.filterByRubriqueClass('rubrique_all')"
          data-dismiss="modal"><c:out value="${contexte.name}"></c:out>
          
          <c:if test="${contexte.userCanMarkRead=='true' && affichereye=='true'}" >
          <span class="badge pull-right"><c:out
              value="${nombreArticleNonLu}"></c:out></span> 
           </c:if>
           </a>
        <c:forEach items="${listCat}" var="cat">
        	<c:set var="nbCat" value="${nbCat+1}" />
        	
        	<c:if test="${not empty cat.sources}">
        		<div class="filtreInDropDown" >
        		<div  class="list-group-item rubriqueFiltre  ${n} cat_${nbCat}"
	              		onclick="lecture.${n}.filterByRubriqueClass('cat_${nbCat}')"
	              		data-dismiss="modal">
	              		<c:out value="${cat.name}"></c:out>
	         	</div>
	            
	            <div id="divCollapseInDropDown${nbCat}${n}"
	            		class="collapseInDropDown" 
	            		data-toggle="collapse"
						data-target="#ulInDropDown${nbCat}${n}" 
						aria-expanded="true"
						aria-controls="ulInDropDown${nbCat}${n}">
							<span class="glyphicon glyphicon-triangle-bottom "></span>
							<span class="glyphicon glyphicon-triangle-right "></span>
				</div>
				</div>
        		<ul id="ulInDropDown${nbCat}${n}" class="collapse in"  
        			 aria-expanded="true"
					aria-labelledby="divCollapseInDropDown${nbCat}${n}">
	          <c:forEach items="${cat.sources}" var="src">
	          	<c:set var="nbSrc" value="${nbSrc+1}" />
	          	<li>
	            <div 	class="list-group-item rubriqueFiltre  ${n} src_${nbSrc}"
	             		onclick="lecture.${n}.filterByRubriqueClass('src_${nbSrc}')"
	            		data-dismiss="modal" >
	              	<c:out value="${src.name}"></c:out>
					<c:if test="${contexte.userCanMarkRead=='true' && cat.userCanMarkRead=='true'}">
						<span class="badge pull-right"><c:out value="${src.unreadItemsNumber}"></c:out></span>
					</c:if>
				</div>
	            </li>
	          </c:forEach>
	          
	            </ul>
          </c:if>
        </c:forEach>
      </div>
    </div>
  </div>
</div>


