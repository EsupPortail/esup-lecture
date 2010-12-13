<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:e="http://commons.esup-portail.org"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:jdt="http://www.jenia.org/jsf/dataTools">
	<ui:composition template="/stylesheets/template.jspx">
		<ui:define name="content">
			<e:form id="edit" showSubmitPopupText="false"
				showSubmitPopupImage="false">

				<t:div id="panels-layout" forceId="true">
					<t:div id="panelLeft-ui" styleClass="ui-layout-west" forceId="true"
						style="width: #{editController.treeSize}%">
						<jsp:include page="editLeft.jsp" />
					</t:div>
					<t:div id="panelRight-ui" styleClass="ui-layout-center"
						forceId="true" style="width: #{99-editController.treeSize}%">
						<jsp:include page="editRight.jsp" />
					</t:div>
				</t:div>

			</e:form>
		</ui:define>
	</ui:composition>
</jsp:root>

