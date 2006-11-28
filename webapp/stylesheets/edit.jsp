<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<link rel="stylesheet"
		href="http://localhost:8080/esup-lecture/media/lecture.css"
		media="screen" />
	<e:page stringsVar="msgs" menuItem="welcome" locale="#{editController.locale}">
		<h:form id="edit">
			<!-- ********* editRight and editLeft fisrt for just one jsp:include/page ********* -->
			<t:buffer into="#{editRight}">
				<jsp:include page="editRight.jsp" />
			</t:buffer>
			<t:buffer into="#{editLeft}">
				<jsp:include page="editLeft.jsp" />
			</t:buffer>
			<!-- ********* With Tree View ********* -->
			<t:buffer into="#{withTree}">
				<t:htmlTag value="table" styleClass="portlet-table-body" style="width: 100%">
					<t:htmlTag value="tr">
						<t:htmlTag value="td" id="TDLeft" forceId="true"
							style="width: #{editController.treeSize}%">
							<h:outputText value="#{editLeft}" escape="false"/>
						</t:htmlTag>
						<t:htmlTag value="td" id="TDRight" forceId="true"
							style="width: #{100 - editController.treeSize}%">
							<h:outputText value="#{editRight}" escape="false"/>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:buffer>
			<!-- ********* Rendering ********* -->
			<h:outputText id="left" value="#{withTree}" escape="false"/>
			<h:outputText id="right" value="#{withoutTree}" escape="false"/>
		</h:form>
	</e:page>
</jsp:root>

