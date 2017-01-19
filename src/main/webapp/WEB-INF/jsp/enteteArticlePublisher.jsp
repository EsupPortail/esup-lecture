
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="row">
	<input type="hidden" id="<portlet:namespace />listNonLu" value="" />
	<div class="col-xs-6 col-sm-6" id="<portlet:namespace />rubSelectedDiv">
		<label>Toutes les actualités</label>
	</div>
	<div class="col-xs-6 col-sm-6 dropdown">
		<div class="dropdown-toggle  pull-right" data-toggle="dropdown">
			<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
		</div>
		<div class="dropdown-menu pull-right">
			<div class="checkbox">
				<label> Afficher uniquement les actualités non lues<input
					type="checkbox" id="<portlet:namespace />checkBoxNonLu"
					onchange="<portlet:namespace />filtrerPublisherNonLus()">
				</label>
			</div>
		</div>
	</div>
	<div class='row menuRubDropDown'>
		<c:forEach items="${listCat}" var="cat">
			<div class="col-xs-12 dropdown">
				<button class="dropdown-toggle btnRubriqueDropDown" type="button"
					data-toggle="dropdown">
					<span class="caret pull-right"></span>
				</button>
				<div class="dropdown-menu menuListRubrique">
					<c:forEach items="${cat.sources}" var="src">
						<div class="row ligneRubriqueMenu"
							onclick="<portlet:namespace />filtrerParRubrique('${cat.id}','${src.id}')">
							<c:out value="${src.name}"></c:out>
							<span class="badge pull-right"
								style="background-color:${src.color}"><c:out
									value="${src.unreadItemsNumber}"></c:out></span>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
