<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<link type="text/css" href="stylesheets/screen.css" rel="stylesheet"
	media="screen" title="Normal" />

<f:view>
	<t:div id="left">
	<h:outputText value="Test du canal lecture : " />
	<h:outputText value="User connecté : "/>
	<h:outputText value="#{homeContextUserBean.user.id}" />
	<!--<h:outputText value="Context courant :#homeContextUserBean.contextName" />-->
	
	</t:div>
</f:view>


	