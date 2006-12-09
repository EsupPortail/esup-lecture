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
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<e:page stringsVar="msgs" menuItem="welcome"
		locale="#{homeController.locale}">
		<t:stylesheet path="/media/lecture.css" media="screen"/>
		<h:form id="home">
			<!-- ********* Rendering ********* -->
			<h:outputText id="left" escape="false"
				rendered="#{homeController.treeVisible}">
				<t:htmlTag value="table" styleClass="portlet-table-body"
					style="width: 100%">
					<t:htmlTag value="tr">
						<t:htmlTag value="td" id="TDLeft" forceId="true"
							style="width: #{homeController.treeSize}%">
							<jsp:include page="homeLeft.jsp" />
						</t:htmlTag>
						<t:htmlTag value="td" id="TDRight" forceId="true"
							style="width: #{100 - homeController.treeSize}%">
							<jsp:include page="homeRight.jsp" />
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</h:outputText>
			<h:outputText id="right" escape="false"
				rendered="#{!homeController.treeVisible}">
				<jsp:include page="homeRight.jsp" />
			</h:outputText>
		</h:form>
	</e:page>
</jsp:root>

