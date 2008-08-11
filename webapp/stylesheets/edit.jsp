<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<e:page stringsVar="msgs" menuItem="welcome"
		locale="#{editController.locale}">
		<h:form id="edit">
			<t:htmlTag value="table" styleClass="portlet-table-body"
				style="width: 100%">
				<t:htmlTag value="tr">
					<t:htmlTag value="td" id="TDLeft" forceId="true"
						style="width: #{editController.treeSize}%">
						<jsp:include page="editLeft.jsp" />
					</t:htmlTag>
					<t:htmlTag value="td" id="TDRight" forceId="true"
						style="width: #{100 - editController.treeSize}%">
						<jsp:include page="editRight.jsp" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</h:form>
	</e:page>
</jsp:root>

