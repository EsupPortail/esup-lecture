<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="things" locale="#{sessionController.locale}"
	authorized="#{thingsController.pageAuthorized}">
	<%@include file="_navigation.jsp"%>
	<script type="text/javascript">
	var thingSelected = false;
	function selectThing(linkId) {
		if (!thingSelected) {
		  	thingSelected = true;
			simulateLinkClick(linkId);
	  	}
	  	return false;
	}
	</script>

	<h:form id="thingsHeaderForm">

		<h:panelGroup>
			<e:section value="#{msgs['THINGS.TITLE']}"
				rendered="#{sessionController.department != null}">
				<f:param value="#{sessionController.department.label}" />
			</e:section>
			<e:section value="#{msgs['THINGS.TITLE_NO_DEPARTMENT']}"
				rendered="#{sessionController.department == null}" />
		</h:panelGroup>

		<e:messages />

		<e:outputLabel for="department"
			value="#{msgs['THINGS.TEXT.DEPARTMENT_SELECTION.PROMPT']}" />
		<e:selectOneMenu id="department" value="#{sessionController.department}"
			onchange="submit();" converter="#{departmentConverter}">
			<f:selectItems value="#{thingsController.departmentItems}" />
		</e:selectOneMenu>
		<e:message for="department" />
		<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}" id="changeButton" />
		<script type="text/javascript">
document.getElementById("thingsHeaderForm:changeButton").style.visibility = 'hidden';
document.getElementById("thingsHeaderForm:changeButton").style.width = 0;
</script>
		<e:commandButton
			rendered="#{thingsController.currentUserCanAddThing}"
			value="#{msgs['THINGS.BUTTON.ADD_THING']}"
			action="#{thingsController.addThing}" immediate="true" />
		<e:commandButton action="navigationDepartmentView"
			rendered="#{sessionController.department != null}"
			value="#{msgs['THINGS.BUTTON.VIEW_DEPARTMENT']}"/>

	</h:form>

	<h:panelGroup rendered="#{sessionController.department != null}"
		id="thingsPanelGroup">
		<h:form id="thingsForm">
			<h:panelGroup rendered="#{not empty thingsController.things}">
				<e:dataTable id="data" rowIndexVar="variable"
					value="#{thingsController.things}" var="thing" border="0"
					style="width:100%" cellspacing="0" cellpadding="0">
					<f:facet name="header">
						<t:htmlTag value="hr" />
					</f:facet>

					<t:column
						onclick="javascript:{return selectThing('hiddenLink[#{variable}]');}">
						<f:facet name="header">
							<e:text
								value="#{msgs['THINGS.TEXT.THINGS.HEADER.VALUE']}" />
						</f:facet>
						<e:text value="#{thing.value}" />
						<t:commandLink id="hiddenLink" forceId="true" action="editThing">
							<t:updateActionListener value="#{thing}"
								property="#{thingsController.thingToUpdate}" />
						</t:commandLink>
					</t:column>

					<t:column
						onclick="javascript:{return selectThing('hiddenLink[#{variable}]');}">
						<f:facet name="header">
							<e:text
								value="#{msgs['THINGS.TEXT.THINGS.HEADER.DATE']}" />
						</f:facet>
						<e:text value="#{thing.printableDate}" />
					</t:column>

					<t:column
						onclick="javascript:{return selectThing('hiddenLink[#{variable}]');}">
						<e:commandButton value="#{msgs['_.BUTTON.VIEW_EDIT']}"
							id="editButton" action="editThing">
							<t:updateActionListener value="#{thing}"
								property="#{thingsController.thingToUpdate}" />
						</e:commandButton>
					</t:column>

					<t:column>
						<e:commandButton value="^^" action="#{thingsController.moveFirst}"
							rendered="#{variable != 0}">
							<t:updateActionListener value="#{thing}"
								property="#{thingsController.thingToUpdate}" />
						</e:commandButton>
					</t:column>

					<t:column>
						<e:commandButton value="^" action="#{thingsController.moveUp}"
							rendered="#{variable != 0}">
							<t:updateActionListener value="#{thing}"
								property="#{thingsController.thingToUpdate}" />
						</e:commandButton>
					</t:column>

					<t:column>
						<e:commandButton value="v" action="#{thingsController.moveDown}"
							rendered="#{variable != thingsController.thingsNumber - 1}">
							<t:updateActionListener value="#{thing}"
								property="#{thingsController.thingToUpdate}" />
						</e:commandButton>
					</t:column>

					<t:column>
						<e:commandButton value="vv" action="#{thingsController.moveLast}"
							rendered="#{variable != thingsController.thingsNumber - 1}">
							<t:updateActionListener value="#{thing}"
								property="#{thingsController.thingToUpdate}" />
						</e:commandButton>
					</t:column>

					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
				</e:dataTable>
				<e:paragraph value="#{msgs['THINGS.TEXT.THINGS.NOTE']}" />
			</h:panelGroup>
			<e:paragraph
				value="#{msgs['THINGS.TEXT.THINGS.NO_THING']}"
				rendered="#{empty thingsController.things}" />
		</h:form>
	</h:panelGroup>
	<script type="text/javascript">
		highlightTableRows("thingsForm:data");
		hideDataTableButton("thingsForm:data","editButton");
</script>
</e:page>
