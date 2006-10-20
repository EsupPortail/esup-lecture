<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}" authorized="#{departmentsController.currentUserCanViewDepartment}" >
	<%@include file="_navigation.jsp"%>

	<h:form id="departmentManagerEditForm">
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%"
			cellspacing="0" cellpadding="0">
			<e:section value="#{msgs['DEPARTMENT_MANAGER_EDIT.TITLE']}">
				<f:param
					value="#{departmentsController.departmentManagerToUpdate.user.displayName} (#{departmentsController.departmentManagerToUpdate.user.id})" />
				<f:param value="#{sessionController.department.label}" />
			</e:section>

			<e:commandButton
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.BUTTON.DELETE']}"
				action="deleteDepartmentManager"
				rendered="#{departmentsController.currentUserCanDeleteDepartmentManager}" />
		</e:panelGrid>

		<e:messages />

		<h:panelGrid columns="2">
			<e:selectBooleanCheckbox id="managerManagers"
				value="#{departmentsController.departmentManagerToUpdate.manageManagers}"
				disabled="#{not departmentsController.currentUserCanEditDepartmentManager}" />
			<e:outputLabel for="managerManagers"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_MANAGERS']}" />
			<e:selectBooleanCheckbox id="manageDepartment"
				value="#{departmentsController.departmentManagerToUpdate.manageDepartment}"
				disabled="#{not departmentsController.currentUserCanEditDepartmentManager}" />
			<e:outputLabel for="manageDepartment"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_DEPARTMENT']}" />
			<e:selectBooleanCheckbox id="manageThings"
				value="#{departmentsController.departmentManagerToUpdate.manageThings}"
				disabled="#{not departmentsController.currentUserCanEditDepartmentManager}" />
			<e:outputLabel for="manageThings"
				value="#{msgs['DEPARTMENT_MANAGER_EDIT.TEXT.MANAGE_THINGS']}" />
		</h:panelGrid>
		<h:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:commandButton
				value="#{msgs['_.BUTTON.UPDATE']}"
				rendered="#{departmentsController.currentUserCanEditDepartmentManager}"
				action="#{departmentsController.updateDepartmentManager}" />
			<e:commandButton
				value="#{msgs['_.BUTTON.CANCEL']}"
				action="cancel" />
		</h:panelGrid>
	</h:form>
</e:page>
