<?xml version="1.0" encoding="UTF-8" ?>
<!-- 
CSS Class :
Portlet :
portlet-table-body: table body
portlet-section-header: header
portlet-section-alternate: With unread element
portlet-section-selected: for selected element

Lecture specific:
collapsed: collapsed tree element
expended: expended tree element
menuTitle: text in menu bar
menuButton: buttons in menu bar
unreadArticle: unread article
readArticle: read article
toggleButton: read/unread toggle button
 -->
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<link rel="stylesheet"
		href="http://localhost:8080/esup-lecture/media/lecture.css"
		media="screen" />
	<e:page stringsVar="msgs" menuItem="welcome" locale="#{homeController.locale}">
		<h:form id="home">
			<!-- ********* homeRight and homeLeft fisrt for just one jsp:include/page ********* -->
			<t:buffer into="#{homeRight}">
				<jsp:include page="homeRight.jsp" />
			</t:buffer>
			<t:buffer into="#{homeLeft}">
				<jsp:include page="homeLeft.jsp" />
			</t:buffer>
			<!-- ********* With Tree View ********* -->
			<t:buffer into="#{withTree}">
				<t:htmlTag value="table" styleClass="portlet-table-body" style="width: 100%">
					<t:htmlTag value="tr">
						<t:htmlTag value="td" id="TDLeft" forceId="true"
							style="width: #{homeController.treeSize}%">
							<h:outputText value="#{homeLeft}" escape="false"/>
						</t:htmlTag>
						<t:htmlTag value="td" id="TDRight" forceId="true"
							style="width: #{100 - homeController.treeSize}%">
							<h:outputText value="#{homeRight}" escape="false"/>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:buffer>
			<!-- ********* Without Tree View ********* -->
			<t:buffer into="#{withoutTree}">
				<h:outputText value="#{homeRight}" escape="false"/>
			</t:buffer>
			<!-- ********* Rendering ********* -->
			<h:outputText id="left" value="#{withTree}" escape="false"
				rendered="#{homeController.treeVisible}" />
			<h:outputText id="right" value="#{withoutTree}" escape="false"
				rendered="#{!homeController.treeVisible}" />
		</h:form>
	</e:page>
</jsp:root>

