<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<link type="text/css" href="stylesheets/screen.css" rel="stylesheet"
	media="screen" title="Normal" />

<f:view>
	<t:div id="left">
		<t:dataList value="#{homeBean.categories}" var="cat"
			layout="unorderedList">
			<h:outputText value="#{cat.name}" />
		</t:dataList>
	</t:div>
</f:view>
