<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanEditDepartmentProperties}">
	<%@include file="_navigation.jsp"%>

	<h:form id="departmentEditForm">
		<e:section value="#{msgs['DEPARTMENT_EDIT.TITLE']}">
			<f:param value="#{sessionController.department.label}" />
		</e:section>

		<e:messages />

		<e:panelGrid columns="2">
			<e:outputLabel for="id" value="#{msgs['DEPARTMENT_EDIT.TEXT.ID']}" />
			<e:text id="id" value="#{departmentsController.departmentToUpdate.id}" />
			<e:outputLabel for="label"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.LABEL']}" />
			<h:panelGroup>
				<e:inputText id="label"
					value="#{departmentsController.departmentToUpdate.label}" required="true"
					disabled="#{not departmentsController.currentUserCanEditDepartmentProperties}" />
				<e:message for="label" />
			</h:panelGroup>
			<e:outputLabel for="xlabel"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.XLABEL']}" />
			<h:panelGroup>
				<e:inputText id="xlabel"
					value="#{departmentsController.departmentToUpdate.xlabel}"
					required="true"
					disabled="#{not departmentsController.currentUserCanEditDepartmentProperties}" />
				<e:message for="xlabel" />
			</h:panelGroup>
			<e:outputLabel for="ldapFilter"
				value="#{msgs['DEPARTMENT_EDIT.TEXT.LDAP_FILTER']}" />
			<h:panelGroup>
				<e:inputText id="ldapFilter"
					value="#{departmentsController.departmentToUpdate.ldapFilter}"
				validator="#{departmentsController.validateFilter}"
					disabled="#{not departmentsController.currentUserCanEditDepartmentProperties}" />
				<e:message for="ldapFilter" />
				<e:text value="#{msgs['DEPARTMENT_EDIT.HELP.LDAP_FILTER']}" />
			</h:panelGroup>
		</e:panelGrid>

		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:commandButton action="#{departmentsController.updateDepartment}"
				rendered="#{departmentsController.currentUserCanEditDepartmentProperties}"
				value="#{msgs['_.BUTTON.UPDATE']}" />
			<e:commandButton action="cancel" value="#{msgs['_.BUTTON.CANCEL']}"
				immediate="true" />
		</e:panelGrid>
	</h:form>
	<script type="text/javascript">
		document.getElementById('departmentEditForm:label').focus();
	</script>
</e:page>
