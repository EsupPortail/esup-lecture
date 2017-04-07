
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="row">
	<!-- si au moin une catégorie a le tag userCanMarkRead à false on affiche pas la possibilité de marquer tous les artices àlu ou non lu -->
	<c:set var="affichereye" value="true"></c:set>
	<c:forEach items="${listCat}" var="cat">
		<c:if test="${cat.userCanMarkRead=='false'}">
			<c:set var="affichereye" value="false"></c:set>
		</c:if>
	</c:forEach>
	<div class="col-xs-6 col-sm-6 " >
		<label class="rubrique_Active ${n}"><c:out value="${contexte.name}"></c:out></label>
	</div>
	<div class="col-xs-6 col-sm-6">
		<c:if test="${contexte.viewDef=='true'}">
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