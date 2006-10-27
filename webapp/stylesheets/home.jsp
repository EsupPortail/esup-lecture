<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- 
TODO : 
- gestion des actions dans un bean
=== en cours mais passer tous les bouton en commandbutton
 -->
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
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<link rel="stylesheet"
		href="http://localhost:8080/esup-lecture/stylesheets/lecture.css"
		media="screen" />
	<e:page stringsVar="msgs" menuItem="welcome" locale="#{homeController.locale}">
		<h:outputText value="#{msgs['HOME.TEST']}" />
		<t:htmlTag value="h1">TEST</t:htmlTag>
	</e:page>
</jsp:root>

