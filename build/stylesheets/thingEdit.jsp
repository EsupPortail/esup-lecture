<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="things" locale="#{sessionController.locale}"
	authorized="#{thingsController.currentUserCanViewThings}">
	<%@include file="_navigation.jsp"%>
	<e:section value="#{msgs['THING_EDIT.TITLE']}" />

	<e:messages />

	<h:form id="thingEditForm">
		<e:panelGrid columns="2">
			<e:outputLabel for="value"
				value="#{msgs['THING_EDIT.TEXT.VALUE']}" />
			<h:panelGroup>
				<e:inputText id="value"
					value="#{thingsController.thingToUpdate.value}" 
					disabled="#{not thingsController.currentUserCanEditThings}"/>
				<e:message for="value" />
			</h:panelGroup>
		</e:panelGrid>

		<e:commandButton value="#{msgs['_.BUTTON.UPDATE']}"
			action="#{thingsController.updateThing}"
			rendered="#{thingsController.currentUserCanEditThings}" />
		<e:commandButton value="#{msgs['THING_EDIT.BUTTON.DELETE']}"
			immediate="true" action="deleteThing"
			rendered="#{thingsController.currentUserCanDeleteThings}" />
		<e:commandButton immediate="true" value="#{msgs['_.BUTTON.CANCEL']}"
			action="cancel" />
	</h:form>
	<script type="text/javascript">
		document.getElementById("thingEditForm:value").focus();
	</script>
</e:page>
