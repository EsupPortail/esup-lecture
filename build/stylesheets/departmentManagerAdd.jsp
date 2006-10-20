<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}" authorized="#{departmentsController.currentUserCanAddDepartmentManager}" >
	<%@include file="_navigation.jsp"%>

	<h:form id="departmentManagerAddForm">
		<e:section value="#{msgs['DEPARTMENT_MANAGER_ADD.TITLE']}">
			<f:param value="#{sessionController.department.label}" />
		</e:section>
		<e:messages />

		<e:outputLabel for="ldapUid"
			value="#{msgs['DEPARTMENT_MANAGER_ADD.TEXT.PROMPT']}" />
		<e:inputText id="ldapUid" value="#{departmentsController.ldapUid}"
			required="true" />
		<e:message for="ldapUid" />
		<e:commandButton value="#{msgs['_.BUTTON.LDAP']}"
			action="ldapSearch" immediate="true">
			<t:updateActionListener value="#{departmentsController}"
				property="#{ldapSearchController.caller}" />
			<t:updateActionListener value="userSelectedToDepartmentManagerAdd"
				property="#{ldapSearchController.successResult}" />
			<t:updateActionListener value="cancelToDepartmentManagerAdd"
				property="#{ldapSearchController.cancelResult}" />
		</e:commandButton>
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:commandButton
				value="#{msgs['DEPARTMENT_MANAGER_ADD.BUTTON.ADD_DEPARTMENT_MANAGER']}"
				action="#{departmentsController.addDepartmentManager}" />
			<e:commandButton
				value="#{msgs['_.BUTTON.CANCEL']}"
				action="cancel" immediate="true" />
		</e:panelGrid>

	</h:form>
	<script type="text/javascript">
		document.getElementById("departmentManagerAddForm:ldapUid").focus();
	</script>
</e:page>
