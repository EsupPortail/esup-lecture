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
	<h:outputText value="#{homeContextUserBean.context.name}" />
	<br/>	
	<br/>	
	<h:outputText value="#{homeContextUserBean.context.description}" />
	<br/>	
	<br/>	
	<h:outputText value="#{homeContextUserBean.context.test}" />
	<br/>	
	<br/>	
	<h:outputText value="Ses categories :"/>	
	<t:dataList value="#{homeContextUserBean.context.categories}" var="category"
		layout="unorderedList">
		<h:outputText value="#{category.name}" />
	</t:dataList>
	</t:div> 
</f:view>


