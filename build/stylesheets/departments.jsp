<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.pageAuthorized}">
	<%@include file="_navigation.jsp"%>

	<script type="text/javascript">
	var departmentSelected = false;
	function selectDepartment(linkId) {
		if (!departmentSelected) {
		  	departmentSelected = true;
			simulateLinkClick(linkId);
	  	}
	}
</script>

	<h:form id="departmentsForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0">
			<e:section value="#{msgs['DEPARTMENTS.TITLE']}" />
			<e:commandButton action="addDepartment"
				rendered="#{departmentsController.currentUserCanAddDepartment}"
				value="#{msgs['DEPARTMENTS.BUTTON.ADD_DEPARTMENT']}" />
		</e:panelGrid>

		<e:messages />

		<e:dataTable id="data" value="#{departmentsController.departments}"
			var="department" rowIndexVar="variable" border="0"
			style="width: 100%" cellspacing="0" cellpadding="0"
			rowId="#{department.id}"
			rowOnClick="javascript:{selectDepartment('hiddenLink[#{variable}]');return false;}"
			rendered="#{not empty departmentsController.departments}">
			<f:facet name="header">
				<t:htmlTag value="hr" />
			</f:facet>
			<h:column>
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENTS.TEXT.HEADER.LABEL']}" />
				</f:facet>
				<t:commandLink id="hiddenLink" forceId="true"
					action="viewDepartement" immediate="true">
					<e:text value="#{department.label}" />
					<t:updateActionListener value="#{department}"
						property="#{sessionController.department}" />
				</t:commandLink>
			</h:column>
			<h:column>
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENTS.TEXT.HEADER.XLABEL']}" />
				</f:facet>
				<e:text value="#{department.xlabel}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<e:text value="#{msgs['DEPARTMENTS.TEXT.HEADER.LDAP_FILTER']}" />
				</f:facet>
				<e:text value="#{department.ldapFilter}" />
			</h:column>
			<t:column style="text-align: right;">
				<e:commandButton value="#{msgs['_.BUTTON.VIEW_EDIT']}" id="columnBtn"
					action="viewDepartement" immediate="true">
					<t:updateActionListener value="#{department}"
						property="#{sessionController.department}" />
				</e:commandButton>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
		<e:text value="#{msgs['DEPARTMENTS.TEXT.NO_DEPARTMENT']}"
			rendered="#{empty departmentsController.departments}" />
	</h:form>
	<script type="text/javascript">
highlightTableRows("departmentsForm:data");
hideDataTableButton("departmentsForm:data","columnBtn");
</script>
</e:page>
