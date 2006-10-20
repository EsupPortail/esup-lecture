<%@include file="_commons-include.jsp"%>
<e:page stringsVar="msgs" locale="#{ldapSearchController.locale}">
	<e:emptyMenu />

	<script type="text/javascript">
	var userSelected = false;
	function selectUser(linkId) {
		if (!userSelected) {
		  	userSelected = true;
			simulateLinkClick(linkId);
	  	}
	}
</script>

	<h:form id="ldapResultsForm">
		<e:section value="#{msgs['LDAP_RESULTS.TITLE']}" />
		<e:paragraph value="#{msgs['LDAP_RESULTS.TEXT.TOP']}" />

		<e:dataTable value="#{ldapSearchController.ldapUsers}" var="ldapUser"
			border="0" id="data" rowIndexVar="variable"
			rowOnClick="javascript:{selectUser('hiddenLink[#{variable}]');return false;}"
			cellspacing="0" cellpadding="0" width="100%">
			<t:column>
				<f:facet name="header">
					<e:text value="#{msgs['LDAP_RESULTS.HEADER.ID']}" />
				</f:facet>
				<e:text value="#{ldapUser.id}" />
				<t:commandLink id="hiddenLink" forceId="true"
					action="#{ldapSearchController.selectUser}" immediate="true">
					<t:updateActionListener value="#{ldapUser}"
						property="#{ldapSearchController.selectedUser}" />
				</t:commandLink>

			</t:column>
			<t:column>
				<f:facet name="header">
					<e:text value="#{msgs['LDAP_RESULTS.HEADER.ATTRIBUTES']}" />
				</f:facet>

				<t:dataList value="#{ldapUser.attributeNames}" var="keyName"
					rowCountVar="rowCountAttrs" rowIndexVar="rowIndexAttrs">

					<e:text value="#{keyName}=" />

					<t:dataList var="valueName" value="#{ldapUser.attributes[keyName]}"
						rowCountVar="rowCount" rowIndexVar="rowIndex">
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.OPEN_BRACE']}"
							rendered="#{(rowIndex==0) and (rowCount >1)}" />
						<e:bold value="#{valueName}" />
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.SEPARATOR']}"
							rendered="#{(rowIndex+1 < rowCount) and (rowCount >1)}" />
						<e:text
							value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.CLOSE_BRACE']}"
							rendered="#{(rowIndex+1==rowCount) and (rowCount >1)}" />
					</t:dataList>

				</t:dataList>
				

				
		</t:column>
		<t:column style="text-align: right;">
					<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}" id="columnBtn"
						action="#{ldapSearchController.selectUser}" immediate="true">
						<t:updateActionListener value="#{ldapUser}"
							property="#{ldapSearchController.selectedUser}" />
					</e:commandButton>
		</t:column>
		<f:facet name="footer">
					<t:htmlTag value="hr" />
				</f:facet>
		</e:dataTable>
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
			<e:commandButton action="back" id="idButtonBack"
				value="#{msgs['_.BUTTON.PREVIOUS']}" immediate="true" />
			<e:commandButton action="#{ldapSearchController.cancel}"
				id="idButtonCancel" value="#{msgs['_.BUTTON.CANCEL']}"
				immediate="true" />
		</e:panelGrid>
	</h:form>
	<script type="text/javascript">
highlightTableRows('ldapResultsForm:data');
hideDataTableButton('ldapResultsForm:data','columnBtn');
</script>
</e:page>
