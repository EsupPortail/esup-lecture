
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<%--
Partie supperieur de l'affichage des articles avec les boutons et dropdown de selection qui faut quand il faut.
 --%>
<div class="enteteLectureAll" >
	<!-- si au moin une catégorie a le tag userCanMarkRead à false on affiche pas la possibilité de marquer tous les artices àlu ou non lu -->
	<c:set var="affichereye" value="true"></c:set>
	<c:forEach items="${listCat}" var="cat">
		<c:if test="${cat.userCanMarkRead=='false'}">
			<c:set var="affichereye" value="false"></c:set>
		</c:if>
	</c:forEach>
	<c:if var="pageAccueil" test="${contexte.viewDef=='true'}" >
		<div class="pull-left" >	
			<label class="rubrique_Active ${n}"><c:out value="${contexte.name}"></c:out></label>
		</div>
	</c:if>
	<c:if test="${! pageAccueil}" >
		<div class="pull-left noMenuDropDown" >	
			<label class="rubrique_Active ${n}"><c:out value="${contexte.name}"></c:out></label>
		</div>
		<div 	class="pull-left withMenuDropDown" 
				data-toggle="modal"
      			data-target="#modalRubriqueList${n}">	
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
		<c:if
			test="${contexte.userCanMarckRead=='true'&&affichereye=='true'||contexte.treeVisible=='true'}">
			<div class="dropdown-toggle  pull-right" data-toggle="dropdown">
				<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
			</div>
			<ul class="dropdown-menu pull-right listOptionNonPublisher">
				<c:if
					test="${contexte.userCanMarckRead=='true'&&affichereye=='true'}">
					<input type="hidden" id="<portlet:namespace />listNonLu"
						value="val1" />
					<li onclick="lecture.${n}.filtrerNonLus('val2')">Afficher les articles Non
						lus</li>
					<li onclick="lecture.${n}.filtrerNonLus('val3')">Afficher les articles Non
						lus en premier</li>
					<li onclick="lecture.${n}.filtrerNonLus('val1')">Afficher tous les articles</li>
					<li onclick="lecture['${n}'].marquerToutItemLu(true)">Marquer tous les
						articles lus</li>
					<li onclick="lecture['${n}'].marquerToutItemLu(false)">Marquer tous les
						articles non lus</li>
				</c:if>
				<c:if test="${contexte.treeVisible=='true'}">
					<div class="checkbox afficherLuWith">
						<label> Cacher l'arbre des catégories&nbsp;</label>
							<input type="checkbox" id="<portlet:namespace />cacherListRubrique">
					</div>
				</c:if>
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
      <div class="modal-body modalPadding">
        <a href="#" class="list-group-item" onclick="lecture.${n}.filterByRubriqueClass('rubrique_all')"
          data-dismiss="modal"><c:out value="Toutes les actualités"></c:out>
          <span class="badge pull-right"><c:out
              value="${nombreArticleNonLu}"></c:out></span> </a>
        <c:forEach items="${listCat}" var="cat">
        	<c:set var="nbCat" value="${nbCat+1}" />
        	
        	<c:if test="${not empty cat.sources}">
        		<div  class="list-group-item rubriqueFiltre  ${n} cat_${nbCat}"
	              		onclick="lecture.${n}.filterByRubriqueClass('cat_${nbCat}', this)"
	              		data-dismiss="modal">
	              		<c:out value="${cat.name}"></c:out>
	              
	              <input type="hidden" class="titleName" value="${cat.name}"/>
	            </div>
        	
	          <c:forEach items="${cat.sources}" var="src">
	          	<c:set var="nbSrc" value="${nbSrc+1}" />
	            <div 	class="list-group-item rubriqueFiltre  ${n} src_${nbSrc}"
	             		onclick="lecture.${n}.filterByRubriqueClass('src_${nbSrc}', this)"
	            		data-dismiss="modal" >
	              	<c:out value="${src.name}"></c:out>
	              	<c:if test="${cat.userCanMarkRead=='true'}">
	              		<span class="badge pull-right" style="background-color:${src.color}">
	              			<c:out value="${src.unreadItemsNumber}"></c:out>
	              		</span>
	                </c:if>
	                <input type="hidden" class="titleName" value="${cat.name} > ${src.name}"/>
	            </div>
	          </c:forEach>
          </c:if>
        </c:forEach>
      </div>
    </div>
  </div>
</div>


