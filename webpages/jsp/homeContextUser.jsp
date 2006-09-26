<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<link type="text/css" href="stylesheets/screen.css" rel="stylesheet"
	media="screen" title="Normal" />

<f:view>
	<t:div id="left">
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
	<h:outputText value="Ses categories :"/>	
	<t:dataList value="#{homeContextUserBean.context.categories}" var="category"
		layout="unorderedList">
		<br/>	
		<h:outputText value="#{category.name}" />
		<br/>	
		<br/>
		<h:outputText value="#{category.description}" />
		<br/>	
		<br/>
		<h:outputText value="Ses sources :"/>	
		<t:dataList value="#{category.sources}" var="source"
			layout="unorderedList">
			<br/>	
			<h:outputText value="#{source.name}" />
			<br/>	
			<br/>
			<!-- Que pour les tests -->
			<h:outputText value="        ITEMXPATH           :" />
			<h:outputText value="#{source.itemXPath}" />
			<h:outputText value="         XSLT         :" />
			<h:outputText value="#{source.xslt}" />
			<h:outputText value="         CONTENT      :" />
			<h:outputText value="#{source.content}" />
		</t:dataList>
	</t:dataList>
	</t:div> 
	<h:outputText value="Element selectionne : "/>	
	<br/>	
	<br/>
	<h:outputText value="#{homeContextUserBean.context.selectedBean.name}"/>	
	<br/>	
	<h:outputText value="#{homeContextUserBean.context.selectedBean.content}"/>	
	<br/>	
</f:view>


