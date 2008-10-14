<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<e:page stringsVar="msgs">
		<e:emptyMenu />
		<!-- Hide/show -->
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:section value="#{msgs['EXCEPTION.TITLE']}" />
			<h:panelGroup>
				<t:panelGroup id="hideExceptionDetails"
					style="cursor: pointer;display:none;"
					onclick="hideExceptionDetails(); return false;" forceId="true">
					<e:bold value="#{msgs['EXCEPTION.TEXT.HIDE_DETAILS']} " />
					<t:graphicImage value="/media/hide.png"
						alt="#{msgs['EXCEPTION.TEXT.HIDE_DETAILS']}"
						title="#{msgs['EXCEPTION.TEXT.HIDE_DETAILS']}" />
				</t:panelGroup>
				<t:panelGroup id="showExceptionDetails"
					style="cursor: pointer;display:none;"
					onclick="showExceptionDetails(); return false;" forceId="true">
					<e:bold value="#{msgs['EXCEPTION.TEXT.SHOW_DETAILS']} " />
					<t:graphicImage value="/media/show.png"
						alt="#{msgs['EXCEPTION.TEXT.SHOW_DETAILS']}"
						title="#{msgs['EXCEPTION.TEXT.SHOW_DETAILS']}" />
				</t:panelGroup>
			</h:panelGroup>
		</e:panelGrid>
		<e:messages />
		<t:htmlTag value="hr" />
		<!-- exceptionForm -->
		<h:form id="exceptionForm">
			<h:panelGroup>
				<h:commandButton action="#{exceptionController.restart}"
					alt="#{msgs['EXCEPTION.BUTTON.RESTART']}"
					title="#{msgs['EXCEPTION.BUTTON.RESTART']}"
					value="#{msgs['EXCEPTION.BUTTON.RESTART']}"
					styleClass="elementButton">
				</h:commandButton>
				<h:commandButton action="#{exceptionController.restart}"
					image="/media/restart.png"
					alt="#{msgs['EXCEPTION.BUTTON.RESTART']}"
					title="#{msgs['EXCEPTION.BUTTON.RESTART']}">
				</h:commandButton>
			</h:panelGroup>
		</h:form>
		<!-- exceptionDetails -->
		<t:panelGroup id="exceptionDetails" rendered="#{printDetails}"
			forceId="true">
			<t:htmlTag value="br" />
			<e:panelGrid alternateColors="true" columns="2" width="100%"
				cellspacing="0" cellpadding="0">
				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>

				<e:outputLabel for="applicationName"
					value="#{msgs['EXCEPTION.INFORMATION.APPLICATION']}" />
				<e:text id="applicationName"
					value="#{exceptionController.applicationName}" />

				<e:outputLabel for="applicationVersion"
					value="#{msgs['EXCEPTION.INFORMATION.VERSION']}" />
				<e:text id="applicationVersion"
					value="#{exceptionController.applicationVersion}" />

				<e:outputLabel for="server"
					value="#{msgs['EXCEPTION.INFORMATION.SERVER']}" />
				<h:panelGroup id="server">
					<e:text value="#{exceptionController.server}"
						rendered="#{exceptionController.server != null}" />
					<e:italic value="#{msgs['EXCEPTION.INFORMATION.SERVER.UNKNOWN']}"
						rendered="#{exceptionController.server == null}" />
				</h:panelGroup>

				<e:outputLabel for="date"
					value="#{msgs['EXCEPTION.INFORMATION.DATE']}" />
				<e:text id="date" value="#{exceptionController.date}" />

				<e:outputLabel for="userId"
					value="#{msgs['EXCEPTION.INFORMATION.USER_ID']}" />
				<h:panelGroup id="userId">
					<e:text value="#{exceptionController.userId}"
						rendered="#{exceptionController.userId != null}" />
					<e:italic value="#{msgs['EXCEPTION.INFORMATION.USER_ID.UNKNOWN']}"
						rendered="#{exceptionController.userId == null}" />
				</h:panelGroup>

				<e:outputLabel for="portal"
					value="#{msgs['EXCEPTION.INFORMATION.PORTAL']}" />
				<h:panelGroup id="portal">
					<h:panelGroup rendered="#{exceptionController.portal != null}">
						<e:text value="#{exceptionController.portal}" />
						<e:text
							value=" #{msgs['EXCEPTION.INFORMATION.PORTAL.QUICK_START']}"
							rendered="#{exceptionController.quickStart}" />
					</h:panelGroup>
					<e:italic value="#{msgs['EXCEPTION.INFORMATION.PORTAL.UNKNOWN']}"
						rendered="#{exceptionController.portal == null}" />
				</h:panelGroup>

				<e:outputLabel for="client"
					value="#{msgs['EXCEPTION.INFORMATION.CLIENT']}" />
				<h:panelGroup id="client">
					<e:text value="#{exceptionController.client}"
						rendered="#{exceptionController.client != null}" />
					<e:italic value="#{msgs['EXCEPTION.INFORMATION.CLIENT.UNKNOWN']}"
						rendered="#{exceptionController.client == null}" />
				</h:panelGroup>

				<e:outputLabel for="queryString"
					value="#{msgs['EXCEPTION.INFORMATION.QUERY_STRING']}" />
				<h:panelGroup id="queryString">
					<e:text value="#{exceptionController.queryString}"
						rendered="#{exceptionController.queryString != null}" />
					<e:italic
						value="#{msgs['EXCEPTION.INFORMATION.QUERY_STRING.UNKNOWN']}"
						rendered="#{exceptionController.queryString == null}" />
				</h:panelGroup>

				<e:outputLabel for="userAgent"
					value="#{msgs['EXCEPTION.INFORMATION.USER_AGENT']}" />
				<h:panelGroup id="userAgent">
					<e:text value="#{exceptionController.userAgent}"
						rendered="#{exceptionController.userAgent != null}" />
					<e:italic
						value="#{msgs['EXCEPTION.INFORMATION.USER_AGENT.UNKNOWN']}"
						rendered="#{exceptionController.userAgent == null}" />
				</h:panelGroup>

				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:panelGrid>

			<e:subSection value="#{msgs['EXCEPTION.HEADER.EXCEPTION']}" />

			<e:panelGrid columns="2" width="100%" alternateColors="true"
				cellspacing="0" cellpadding="0">
				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>

				<e:outputLabel for="exceptionName"
					value="#{msgs['EXCEPTION.EXCEPTION.NAME']}" />
				<e:text id="exceptionName"
					value="#{exceptionController.exceptionName}" />

				<e:outputLabel for="exceptionMessage"
					value="#{msgs['EXCEPTION.EXCEPTION.MESSAGE']}" />
				<e:text id="exceptionMessage"
					value="#{exceptionController.exceptionMessage}" />

				<e:outputLabel for="exceptionShortStackTrace"
					value="#{msgs['EXCEPTION.EXCEPTION.SHORT_STACK_TRACE']}" />
				<e:dataTable id="exceptionShortStackTrace"
					value="#{exceptionController.exceptionShortStackTrace}"
					var="string" border="0" style="width: 100%">
					<t:column>
						<e:text value="#{string}" />
					</t:column>
				</e:dataTable>

				<e:outputLabel for="exceptionStackTrace"
					value="#{msgs['EXCEPTION.EXCEPTION.STACK_TRACE']}" />
				<e:dataTable id="exceptionStackTrace"
					value="#{exceptionController.exceptionStackTrace}" var="string"
					border="0" style="width: 100%">
					<t:column>
						<e:text value="#{string}" />
					</t:column>
				</e:dataTable>

				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:panelGrid>

			<e:subSection value="#{msgs['EXCEPTION.HEADER.REQUEST_ATTRIBUTES']}" />

			<e:dataTable id="requestAttributes"
				value="#{exceptionController.requestAttributes}" var="string"
				border="0" style="width: 100%"
				rendered="#{not empty exceptionController.requestAttributes}">

				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>
				<t:column>
					<e:text value="#{string}" />
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:text value="#{msgs['EXCEPTION.REQUEST_ATTRIBUTES.NONE']}"
				rendered="#{empty exceptionController.requestAttributes}" />

			<e:subSection value="#{msgs['EXCEPTION.HEADER.SESSION_ATTRIBUTES']}" />

			<e:dataTable id="sessionAttributes"
				value="#{exceptionController.sessionAttributes}" var="string"
				border="0" style="width: 100%"
				rendered="#{not empty exceptionController.sessionAttributes}">

				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>
				<t:column>
					<e:text value="#{string}" />
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:text value="#{msgs['EXCEPTION.SESSION_ATTRIBUTES.NONE']}"
				rendered="#{empty exceptionController.sessionAttributes}" />

			<e:subSection
				value="#{msgs['EXCEPTION.HEADER.GLOBAL_SESSION_ATTRIBUTES']}" />

			<e:dataTable id="globalSessionAttributes"
				value="#{exceptionController.globalSessionAttributes}" var="string"
				border="0" style="width: 100%"
				rendered="#{not empty exceptionController.globalSessionAttributes}">

				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>
				<t:column>
					<e:text value="#{string}" />
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:text value="#{msgs['EXCEPTION.GLOBAL_SESSION_ATTRIBUTES.NONE']}"
				rendered="#{empty exceptionController.globalSessionAttributes}" />

			<e:subSection value="#{msgs['EXCEPTION.HEADER.REQUEST_HEADERS']}" />

			<e:dataTable id="requestHeaders"
				value="#{exceptionController.requestHeaders}" var="string"
				border="0" style="width: 100%"
				rendered="#{not empty exceptionController.requestHeaders}">

				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>
				<t:column>
					<e:text value="#{string}" />
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:text value="#{msgs['EXCEPTION.REQUEST_HEADERS.NONE']}"
				rendered="#{empty exceptionController.requestHeaders}" />

			<e:subSection value="#{msgs['EXCEPTION.HEADER.REQUEST_PARAMETERS']}" />

			<e:dataTable id="requestParameters"
				value="#{exceptionController.requestParameters}" var="string"
				border="0" style="width: 100%"
				rendered="#{not empty exceptionController.requestParameters}">

				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>
				<t:column>
					<e:text value="#{string}" />
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:text value="#{msgs['EXCEPTION.REQUEST_PARAMETERS.NONE']}"
				rendered="#{empty exceptionController.requestParameters}" />

			<e:subSection value="#{msgs['EXCEPTION.HEADER.COOKIES']}" />

			<e:dataTable id="cookies" value="#{exceptionController.cookies}"
				var="string" border="0" style="width: 100%"
				rendered="#{not empty exceptionController.cookies}">

				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>
				<t:column>
					<e:text value="#{string}" />
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:text value="#{msgs['EXCEPTION.COOKIES.NONE']}"
				rendered="#{empty exceptionController.cookies}" />

			<e:subSection value="#{msgs['EXCEPTION.HEADER.SYSTEM_PROPERTIES']}" />

			<e:dataTable id="systemProperties"
				value="#{exceptionController.systemProperties}" var="string"
				border="0" style="width: 100%"
				rendered="#{not empty exceptionController.systemProperties}">

				<f:facet name="header">
					<t:htmlTag value="hr" />
				</f:facet>
				<t:column>
					<e:text value="#{string}" />
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:text value="#{msgs['EXCEPTION.SYSTEM_PROPERTIES.NONE']}"
				rendered="#{empty exceptionController.systemProperties}" />

			<e:paragraph rendered="#{exceptionController.recipientEmail != null}"
				value="#{msgs['EXCEPTION.TEXT.BOTTOM']}">
				<f:param value="#{exceptionController.recipientEmail}" />
			</e:paragraph>
		</t:panelGroup>
		<script>
	    <![CDATA[
	    function hideExceptionDetails() {
	        hideElement('hideExceptionDetails');
	        showElement('showExceptionDetails');
	        hideElement('exceptionDetails');
	    }
	    function showExceptionDetails() {
	        showElement('hideExceptionDetails');
	        hideElement('showExceptionDetails');
	        showElement('exceptionDetails');
	    }
	    hideExceptionDetails();
	    ]]>
        </script>
	</e:page>

</jsp:root>