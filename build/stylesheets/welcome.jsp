<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="welcome" locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>
	<h:form id="welcomeForm">
		<e:section value="#{msgs['WELCOME.TITLE']}" />
		<e:paragraph value="#{msgs['WELCOME.TEXT.TOP']}" />

		<h:panelGroup rendered="#{sessionController.currentUser != null}">
			<h:panelGroup rendered="#{not empty welcomeController.printableThingSets}">
				<e:subSection value="#{msgs['WELCOME.THINGS.TEXT']}" />
				<e:dataTable value="#{welcomeController.printableThingSets}" var="printableThingSet"
					border="0" style="width:100%" cellspacing="0" cellpadding="0" alternateColors="false">
					<f:facet name="header">
						<t:htmlTag value="hr" />
					</f:facet>
					<t:column>
						<e:dataTable value="#{printableThingSet.things}" var="thing"
							border="0" style="width:100%" cellspacing="0" cellpadding="0" alternateColors="true"
							rendered="#{not empty printableThingSet.things}">
							<f:facet name="header">
								<e:text value="#{msgs['WELCOME.THINGS.HEADER']}" >
									<f:param value="#{printableThingSet.department.label}" />
								</e:text>
							</f:facet>
							<t:column>
								<e:text value="#{thing.value}" />
							</t:column>
						</e:dataTable>
						<e:text value="#{msgs['WELCOME.THINGS.NONE']}" rendered="#{empty printableThingSet.things}">
							<f:param value="#{printableThingSet.department.label}" />
						</e:text>
					</t:column>
					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
				</e:dataTable>
			</h:panelGroup>
			<e:paragraph rendered="#{empty welcomeController.printableThingSets}" value="#{msgs['WELCOME.TEXT.NO_DEPARTMENT']}" />
		</h:panelGroup>
		<h:panelGroup rendered="#{sessionController.currentUser == null}">
			<e:paragraph value="#{msgs['WELCOME.TEXT.UNAUTHENTICATED']}" />
			<e:panelGrid columns="2" >
				<e:outputLabel id="localeLabel" for="locale" 
					value="#{msgs['PREFERENCES.TEXT.LANGUAGE']}" />
				<h:panelGroup>
					<e:selectOneMenu id="locale" onchange="submit();"
						value="#{preferencesController.locale}" converter="#{localeConverter}" >
						<f:selectItems value="#{preferencesController.localeItems}" />
					</e:selectOneMenu>
					<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}" id="localeChangeButton" />
				</h:panelGroup>
			</e:panelGrid>
		</h:panelGroup>
	</h:form>
<script type="text/javascript">	
	document.getElementById("welcomeForm:localeChangeButton").style.visibility = 'hidden';		
	document.getElementById("welcomeForm:localeChangeButton").style.width = 0;
</script>
</e:page>
