<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators" locale="#{sessionController.locale}" authorized="#{administratorsController.currentUserCanDeleteAdmin}" >
	<%@include file="_navigation.jsp"%>

	<h:form >
		<e:section value="#{msgs['ADMINISTRATOR_DELETE.TITLE']}">
			<f:param
				value="#{administratorsController.userToDelete.displayName} (#{administratorsController.userToDelete.id})" />
		</e:section>

		<e:messages />

		<e:paragraph value="#{msgs['ADMINISTRATOR_DELETE.TEXT.TOP']}">
				<f:param
					value="#{administratorsController.userToDelete.displayName} (#{administratorsController.userToDelete.id})" />
		</e:paragraph>
		<e:commandButton
			value="#{msgs['_.BUTTON.CONFIRM']}"
			action="#{administratorsController.confirmDeleteAdmin}" />
		<e:commandButton value="#{msgs['_.BUTTON.CANCEL']}"
			action="cancel" />
	</h:form>
</e:page>
