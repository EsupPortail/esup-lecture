<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<link type="text/css" href="stylesheets/screen.css" rel="stylesheet"
	media="screen" title="Normal" />

<f:view>
	<t:div id="left">
	<h:outputText value="User connecté : "/>
	<h:outputText value="#{homeEditUserBean.user.id}" />
	<br/>	
	<br/>	
	<h:outputText value="Context :"/>
	<h:outputText value="#{homeEditUserBean.edit.contextName}" />
	<br/>	
	<br/>	
	<h:outputText value="Ses categories visible pour le user connecte :"/>	
	<t:dataList value="#{homeEditUserBean.edit.categories}" var="category"
		layout="unorderedList">
		<br/>	
		<br/>	
		<h:outputText value="#{category}" />
	</t:dataList>
	</t:div> 
	<h:outputText value="Categorie selectionnee :"/>	
	<h:outputText value="#{homeEditUserBean.edit.selectedCategory.profileId}" />

</f:view>


