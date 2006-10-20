<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators" locale="#{sessionController.locale}"
	authorized="#{administratorsController.currentUserCanAddAdmin}">
	<%@include file="_navigation.jsp"%>
	<h:form id="administratorAddForm">

		<e:section value="#{msgs['ADMINISTRATOR_ADD.TITLE']}" />

		<e:messages />

		<e:outputLabel id="x" for="ldapUid"
			value="#{msgs['ADMINISTRATOR_ADD.TEXT.PROMPT']}" />
		<e:inputText id="ldapUid" value="#{administratorsController.ldapUid}"
			required="true" />
		<e:message id="y" for="ldapUid" />

		<e:commandButton value="#{msgs['_.BUTTON.LDAP']}" action="ldapSearch"
			immediate="true">
			<t:updateActionListener value="#{administratorsController}"
				property="#{ldapSearchController.caller}" />
			<t:updateActionListener value="userSelectedToAdministratorAdd"
				property="#{ldapSearchController.successResult}" />
			<t:updateActionListener value="cancelToAdministratorAdd"
				property="#{ldapSearchController.cancelResult}" />
		</e:commandButton>
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:commandButton
				value="#{msgs['ADMINISTRATOR_ADD.BUTTON.ADD_ADMIN']}"
				action="#{administratorsController.addAdmin}" />
			<e:commandButton value="#{msgs['_.BUTTON.CANCEL']}" action="cancel"
				immediate="true" />
		</e:panelGrid>
	</h:form>
	<script type="text/javascript">
		document.getElementById("administratorAddForm:ldapUid").focus();
	</script>
</e:page>
