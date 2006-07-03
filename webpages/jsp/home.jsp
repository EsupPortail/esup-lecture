<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
 
<f:view>
	<h:dataTable value="#{homeBean.categories}" var="cat">
		<h:column>
			<h:outputText value="#{cat.name}" />
		</h:column>
	</h:dataTable>
</f:view>
