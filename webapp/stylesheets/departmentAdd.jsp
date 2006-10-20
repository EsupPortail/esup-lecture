<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanAddDepartment}">
	<%@include file="_navigation.jsp"%>

	<h:form id="departmentAddForm">
		<e:section value="#{msgs['DEPARTMENT_ADD.TITLE']}" />

		<e:messages />

		<e:panelGrid id="departmentAddPanel" columns="2">
			<e:outputLabel for="label"
				value="#{msgs['DEPARTMENT_ADD.TEXT.LABEL']}" />
			<h:panelGroup>
				<e:inputText id="label"
					value="#{departmentsController.departmentToAdd.label}" required="true" />
				<e:message for="label" />
			</h:panelGroup>
			<e:outputLabel for="xlabel"
				value="#{msgs['DEPARTMENT_ADD.TEXT.XLABEL']}" />
			<h:panelGroup>
				<e:inputText id="xlabel"
					value="#{departmentsController.departmentToAdd.xlabel}" required="true" />
				<e:message for="xlabel" />
			</h:panelGroup>
			<e:outputLabel for="ldapFilter" value="#{msgs['DEPARTMENT_ADD.TEXT.LDAP_FILTER']}" />
			<h:panelGroup>
				<e:inputText id="ldapFilter"
					value="#{departmentsController.departmentToAdd.xlabel}" />
				<e:message for="ldapFilter" />
				<e:text value="#{msgs['DEPARTMENT_ADD.HELP.LDAP_FILTER']}" />
			</h:panelGroup>
			<h:panelGroup>
				<e:commandButton
					value="#{msgs['DEPARTMENT_ADD.BUTTON.ADD_DEPARTMENT']}"
					action="#{departmentsController.addDepartment}" />
				<e:commandButton value="#{msgs['_.BUTTON.CANCEL']}" action="cancel"
					immediate="true" />
			</h:panelGroup>
		</e:panelGrid>
	</h:form>
	<script type="text/javascript">
	document.getElementById("departmentAddForm:label").focus();
</script>
</e:page>
