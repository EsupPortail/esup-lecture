<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<e:page stringsVar="msgs" menuItem="welcome"
		locale="#{editController.locale}">
		<t:stylesheet path="/media/thickbox.css" media="screen"/>
		<t:stylesheet path="/media/js-layout.css" media="screen"/>
		<e:form id="edit" showSubmitPopupText="false" showSubmitPopupImage="false">

			<t:div id="panels-layout" forceId="true">
				<t:div id="panelLeft-ui" styleClass="ui-layout-west" forceId="true" 
				 	style="width: #{editController.treeSize}%">
					<jsp:include page="editLeft.jsp" />
				</t:div>
				<t:div id="panelRight-ui" styleClass="ui-layout-center"  forceId="true" 
					style="width: #{99-editController.treeSize}%">
					<jsp:include page="editRight.jsp" />
				</t:div>
			</t:div>

		</e:form>
	</e:page>
</jsp:root>

