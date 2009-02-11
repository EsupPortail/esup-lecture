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
	<e:page stringsVar="msgs" menuItem="welcome" locale="#{homeController.locale}">
		<t:stylesheet path="/media/thickbox.css" media="screen"/>
		<t:stylesheet path="/media/js-layout.css" media="screen"/>

<script type="text/javascript"><!--

/*
JHomeController.getContextKey(
		function(data) { 
			alert(data);
		}
);

JHomeController.getDummyText(
		function(data) { 
			alert(data);
		}
);

JHomeController.isTreeVisible(
		function(data) { 
			alert(data);
		}

);
*/

--></script>
		<h:form id="home">

			<!-- ********* Rendering ********* -->
			<t:div id="twoPanels" forceId="true" 
				rendered="#{homeController.treeVisible and !homeController.guestMode}">
				<t:div id="panelLeft" forceId="true" style="width: #{homeController.treeSize}%">
					<jsp:include page="homeLeft.jsp" />
				</t:div>
				<t:div id="panelRight" forceId="true"  style="width: #{99-homeController.treeSize}%">
					<jsp:include page="homeRight.jsp" />
				</t:div>
			</t:div>
			<t:div id="onePanel" forceId="true" 
				rendered="#{!homeController.treeVisible or homeController.guestMode}">
				<t:div id="panelRight" forceId="true" >
					<jsp:include page="homeRight.jsp" />
				</t:div>
			</t:div>

		</h:form>
		<script type="text/javascript">
		document.getElementById("home:itemDisplayModeButton").style.display = "none";
        </script>
	</e:page>
</jsp:root>

