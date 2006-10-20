<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}" authorized="#{departmentsController.currentUserCanViewDepartment}" >
	<%@include file="_navigation.jsp"%>

	<script type="text/javascript">
	var managerSelected = false;
	function selectManager(linkId) {
		if (!managerSelected) {
		  	managerSelected = true;
			simulateLinkClick(linkId);
	  	}
	}
</script>

	<h:form id="departmentViewForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0" id="topPanelGrid">
			<e:section value="#{msgs['DEPARTMENT_VIEW.TITLE']}">
				<f:param value="#{sessionController.department.label}" />
			</e:section>
			<e:commandButton action="back"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.BACK']}" />
		</e:panelGrid>

		<e:messages />

		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0">
			<h:column>
				<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.MANAGERS']}" />
			</h:column>
			<e:commandButton
				rendered="#{departmentsController.currentUserCanAddDepartmentManager}"
				value="#{msgs['DEPARTMENT_VIEW.BUTTON.ADD_MANAGER']}"
				action="addManager" immediate="true" />
		</e:panelGrid>

		<h:panelGroup
			rendered="#{not empty departmentsController.departmentManagers}">
			<e:dataTable id="data" rowIndexVar="variable" width="100%"
				rowOnClick="javascript:{selectManager('hiddenLink[#{variable}]');return false;}"
				value="#{departmentsController.departmentManagers}"
				var="departmentManager" border="0" cellspacing="0" cellpadding="0">
				<f:facet name="header">
					<h:panelGroup>
						<t:htmlTag value="hr" />
					</h:panelGroup>
				</f:facet>
				<t:column>
					<e:text
						value="#{departmentManager.user.displayName} (#{departmentManager.user.id})" />
					<t:commandLink id="hiddenLink" forceId="true"
						action="editDepartmentManager" immediate="true">
						<t:updateActionListener value="#{departmentManager}"
							property="#{departmentsController.departmentManagerToUpdate}" />
					</t:commandLink>
				</t:column>
				<t:column style="text-align: right;">
					<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}" id="columnBtn"
						action="editDepartmentManager">
						<t:updateActionListener value="#{departmentManager}"
							property="#{departmentsController.departmentManagerToUpdate}" />
					</e:commandButton>
				</t:column>
				<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
			</e:dataTable>
			<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.NOTE']}" />
		</h:panelGroup>
		<e:paragraph value="#{msgs['DEPARTMENT_VIEW.TEXT.MANAGERS.NONE']}"
			rendered="#{empty departmentsController.departmentManagers}" />

		<e:subSection value="#{msgs['DEPARTMENT_VIEW.HEADER.PROPERTIES']}" />

		<e:panelGrid columns="2">
			<e:outputLabel for="id"
				value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.ID']}" />
			<e:text value=" #{sessionController.department.id}" id="id" />
			<e:outputLabel for="label"
				value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.LABEL']}" />
			<e:text value=" #{sessionController.department.label}" id="label" />
			<e:outputLabel for="xlabel"
				value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.XLABEL']}" />
			<e:text value=" #{sessionController.department.xlabel}" id="xlabel" />
			<e:outputLabel for="ldapFilter"
				value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.LDAP_FILTER']}" />
			<h:panelGroup id="ldapFilter">
				<e:text value="#{sessionController.department.ldapFilter}"
					rendered="#{sessionController.department.ldapFilter != null}" />
				<e:italic value="#{msgs['DEPARTMENT_VIEW.TEXT.PROPERTIES.NO_LDAP_FILTER']}"
					rendered="#{sessionController.department.ldapFilter == null}" />
			</h:panelGroup>
		</e:panelGrid>
		<e:commandButton
			rendered="#{departmentsController.currentUserCanEditDepartmentProperties}"
			value="#{msgs['DEPARTMENT_VIEW.BUTTON.EDIT_PROPERTIES']}"
			action="editProperties" immediate="true">
			<t:updateActionListener value="#{sessionController.department}"
				property="#{departmentsController.departmentToUpdate}" />
		</e:commandButton>

		<e:commandButton value="#{msgs['DEPARTMENT_VIEW.BUTTON.DELETE_DEPARTMENT']}"
			rendered="#{departmentsController.currentUserCanDeleteDepartment}"
			action="deleteDepartment" immediate="true"/>
	</h:form>
	<script type="text/javascript">
highlightTableRows("departmentViewForm:data");
hideDataTableButton("departmentViewForm:data","columnBtn");
</script>
</e:page>
