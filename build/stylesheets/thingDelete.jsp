<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="things" locale="#{sessionController.locale}"
	authorized="#{thingsController.currentUserCanDeleteThings}">
	<%@include file="_navigation.jsp"%>

	<h:form>
		<e:section value="#{msgs['THING_DELETE.TITLE']}"
			rendered="#{thingsController.thingToUpdate.value != null}">
			<f:param value="#{thingsController.thingToUpdate.value}" />
		</e:section>
		<e:section value="#{msgs['THING_DELETE.TITLE_NO_VALUE']}"
			rendered="#{thingsController.thingToUpdate.value == null}" />
		<e:paragraph value="#{msgs['THING_DELETE.TEXT.TOP']}" />
		<e:commandButton value="#{msgs['_.BUTTON.CONFIRM']}"
			action="#{thingsController.confimDeleteThing}" />
		<e:commandButton immediate="true" value="#{msgs['_.BUTTON.CANCEL']}"
			action="cancel" />
	</h:form>
</e:page>
