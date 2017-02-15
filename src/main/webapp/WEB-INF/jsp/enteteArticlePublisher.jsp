
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<input type="hidden" id="<portlet:namespace />listNonLu" value="" />
<div class="row enteteEcranLarge">
	<div class="col-xs-7 col-sm-7"
		id="<portlet:namespace />rubSelectedDiv1">
		<label>Toutes les actualités</label>
	</div>
	<div class='col-xs-5 col-sm-5'>
		<div class='row'>
			<!-- 			<div class="menuRubDropDown col-xs-8 col-sm-9"> -->
			<!-- 				<div class="btnRubriqueDropDown" data-toggle="collapse" -->
			<%-- 					data-target="#<portlet:namespace />rub"> --%>
			<!-- 					<span class="caret pull-right"></span> -->
			<!-- 				</div> -->
			<%-- 				<div class="collapse menuListRubrique" id="<portlet:namespace />rub"> --%>
			<!-- 					<div class="row ligneRubriqueMenu ligneRubriqueMenuPub" -->
			<!-- 						onclick="AfficherTout()"> -->
			<%-- 						Toutes les actualités<span class="badge pull-right"><c:out --%>
			<%-- 								value="${nombreArticleNonLu}"></c:out></span> --%>
			<!-- 					</div> -->
			<%-- 					<c:forEach items="${listCat}" var="cat"> --%>
			<%-- 						<c:forEach items="${cat.sources}" var="src"> --%>
			<!-- 							<div class="row ligneRubriqueMenu ligneRubriqueMenuPub" -->
			<%-- 								onclick="filtrerParRubrique('${cat.id}','${src.id}','${src.name}')"> --%>
			<%-- 								<c:out value="${src.name}"></c:out> --%>
			<!-- 								<span class="badge pull-right" -->
			<%-- 									style="background-color:${src.color}"><c:out --%>
			<%-- 										value="${src.unreadItemsNumber}"></c:out></span> --%>
			<!-- 							</div> -->
			<%-- 						</c:forEach> --%>
			<%-- 					</c:forEach> --%>
			<!-- 				</div> -->
			<!-- 			</div> -->




			<div class="dropdown col-xs-4 col-sm-2 pull-right">
				<div class="dropdown-toggle  pull-right" data-toggle="dropdown">
					<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
				</div>
				<div class="dropdown-menu pull-right">
					<div class="checkbox afficherLuWith">
						<label> Afficher uniquement les actualités non lues&nbsp;</label>
						<c:if test="${contexte.itemDisplayMode=='UNREAD'}">
							<input type="checkbox" id="<portlet:namespace />checkBoxNonLu"
								onchange="filtrerPublisherNonLus()" checked="checked">
						</c:if>
						<c:if test="${contexte.itemDisplayMode=='ALL'}">
							<input type="checkbox" id="<portlet:namespace />checkBoxNonLu"
								onchange="filtrerPublisherNonLus()">
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row menuRubDropDown" id="mainmenurow<portlet:namespace />">
	<div id="mainmenu<portlet:namespace />" class="col-xs-10 col-sm-11">
		<div id="<portlet:namespace />rubSelectedDiv2" data-toggle="modal"
			data-target="#myModal<portlet:namespace />">
			<label>Toutes les actualités</label> <span class="caret pull-right margeCarret"></span>
		</div>
	</div>
	<div class="dropdown pull-right col-xs-2 col-sm-1">
		<div class="dropdown-toggle  pull-right" data-toggle="dropdown">
			<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
		</div>
		<div class="dropdown-menu pull-right">
			<div class="checkbox afficherLuWith">
				<label> Afficher uniquement les actualités non lues&nbsp;</label>
				<c:if test="${contexte.itemDisplayMode=='UNREAD'}">
					<input type="checkbox" id="<portlet:namespace />checkBoxNonLu2"
						onchange="filtrerPublisherNonLusMobile()" checked="checked">
				</c:if>
				<c:if test="${contexte.itemDisplayMode=='ALL'}">
					<input type="checkbox" id="<portlet:namespace />checkBoxNonLu2"
						onchange="filtrerPublisherNonLusMobile()">
				</c:if>
			</div>
		</div>
	</div>

</div>
<div id="myModal<portlet:namespace />" class="modal fade" role="dialog">
	<div class="modal-dialog modalMarge">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-body modalPadding">
				<a href="#" class="list-group-item" onclick="AfficherTout('')" data-dismiss="modal"><c:out
						value="Toutes les actualités"></c:out> <span
					class="badge pull-right"><c:out
							value="${nombreArticleNonLu}"></c:out></span> </a>
				<c:forEach items="${listCat}" var="cat">
					<c:forEach items="${cat.sources}" var="src">
						<a href="#" class="list-group-item"
							onclick="filtrerParRubrique('${cat.id}','${src.id}','${src.name}','')" data-dismiss="modal"><c:out
								value="${src.name}"></c:out><span class="badge pull-right"
							style="background-color:${src.color}"><c:out
									value="${src.unreadItemsNumber}"></c:out></span></a>
					</c:forEach>
				</c:forEach>
			</div>
		</div>
	</div>
</div>

