<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<link type="text/css" href="stylesheets/screen.css" rel="stylesheet"
	media="screen" title="Normal" />

<f:view>
	<t:div id="left">
	<h:outputText value="Test du canal lecture : " />
		<t:dataList value="#{homeContextBean.contextWebs}" var="context"
			layout="unorderedList">
			<h:outputText value="#{context.name}" />
			<h:outputText value="  #{context.description}" />
			<h:outputText value="Ses catégories :" />
			<t:dataList value="#{context.categoryWebs}" var="cat" layout="unorderedList">
				<h:outputText value="    #{cat.name}" />
			</t:dataList>	
		</t:dataList>
	</t:div>
</f:view>


	