<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="files" locale="#{sessionController.locale}"
	authorized="#{filesController.pageAuthorized}">
	<%@include file="_navigation.jsp"%>

	<e:section value="#{msgs['FILES.TITLE']}" />

	<e:messages />

	<h:form id="filesForm" rendered="#{not empty filesController.files}">
		<e:dataTable id="data" rowIndexVar="variable"
			value="#{filesController.files}" var="file" border="0"
			style="width:100%" cellspacing="0" cellpadding="0">
			<f:facet name="header">
				<t:htmlTag value="hr" />
			</f:facet>

			<t:column>
				<f:facet name="header">
					<e:text value="#{msgs['FILES.TEXT.FILES.HEADER.NAME']}" />
				</f:facet>
				<e:text value="#{file.name}" />
			</t:column>

			<t:column>
				<f:facet name="header">
					<e:text value="#{msgs['FILES.TEXT.FILES.HEADER.TYPE']}" />
				</f:facet>
				<e:text value="#{file.type}" />
			</t:column>

			<t:column>
				<f:facet name="header">
					<e:text value="#{msgs['FILES.TEXT.FILES.HEADER.SIZE']}" />
				</f:facet>
				<e:text value="#{file.size}" />
			</t:column>

			<t:column>
				<e:commandButton value="#{msgs['FILES.BUTTON.DOWNLOAD']}"
					action="#{filesController.downloadFile}" >
					<t:updateActionListener value="#{file.name}"
						property="#{filesController.filenameToDownload}" />
				</e:commandButton>
			</t:column>

			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
	</h:form>
	<e:paragraph value="#{msgs['FILES.TEXT.FILES.NO_FILE']}"
		rendered="#{empty filesController.files}" >
		<f:param value="#{filesController.directory}" />
	</e:paragraph>
</e:page>
