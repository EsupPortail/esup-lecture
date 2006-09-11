<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<link type="text/css" href="stylesheets/screen.css" rel="stylesheet"
	media="screen" title="Normal" />

<f:view>
	<t:div id="left">
	<h:outputText value="Test du canal lecture : " />
	<br/>	
	<br/>	
	<h:outputText value="User connecté : "/>
	<h:outputText value="#{homeContextUserBean.user.id}" />
	<br/>	
	<br/>	
	<h:outputText value="Context :"/>
	<h:outputText value="#homeContextUserBean.user.context.name" />
	<br/>	
	<br/>	
	<h:outputText value="#homeContextUserBean.user.context.description" />
	</t:div> 
</f:view>


	