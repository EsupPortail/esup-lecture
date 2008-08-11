<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<f:subview id="leftSubview">
		<!-- TREE -->
		<t:htmlTag value="div" id="left" forceId="true">
			<!-- Title -->
			<t:htmlTag value="p" styleClass="portlet-section-header">
				<h:outputText value="#{editController.context.name} (EDIT)" />
			</t:htmlTag>
			<!-- Categories -->
			<t:htmlTag value="ul">
				<t:dataList value="#{editController.context.categories}" var="cat"
					layout="simple">
					<t:htmlTag value="li" styleClass="edit">
						<h:commandButton
							action="#{editController.toogleCategorySubcribtion}"
							image="/media/subscribe.png" alt="#{msgs['subscribeCategory']}"
							title="#{msgs['subscribeCategory']}"
							rendered="#{cat.notSubscribed}" styleClass="valign">
							<t:updateActionListener property="#{editController.ualCategory}"
								value="#{cat}" />
						</h:commandButton>
						<h:commandButton
							action="#{editController.toogleCategorySubcribtion}"
							image="/media/unsubscribe.png"
							alt="#{msgs['unsubscribeCategory']}"
							title="#{msgs['unsubscribeCategory']}"
							rendered="#{cat.subscribed}" styleClass="valign">
							<t:updateActionListener property="#{editController.ualCategory}"
								value="#{cat}" />
						</h:commandButton>
						<h:graphicImage value="/media/forced.png"
							alt="#{msgs['forcedCategory']}" title="#{msgs['forcedCategory']}"
							rendered="#{cat.obliged}"  styleClass="valign"/>
						<h:commandButton action="#{editController.selectElement}"
							alt="#{cat.name}" title="#{cat.name}" value="#{cat.name}"
							styleClass="elementButton">
							<t:updateActionListener property="#{editController.ualCategory}"
								value="#{cat}" />
						</h:commandButton>
					</t:htmlTag>
				</t:dataList>
			</t:htmlTag>
		</t:htmlTag>
		<!-- Ajust Tree Size buttons -->
		<t:htmlTag value="hr" />
		<t:htmlTag value="div" id="menuLeft" forceId="true">
			<t:htmlTag value="div" styleClass="menuTitle">
				<h:commandButton id="homeButton" action="navigationHome"
					image="/media/go-home.png" alt="#{msgs['home']}"
					title="#{msgs['home']}" />
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuButton">
				<t:htmlTag value="ul">
					<t:htmlTag value="li">
						<h:commandButton id="treeSmallerButton"
							actionListener="#{editController.adjustTreeSize}"
							image="/media/retract.gif" alt="#{msgs['treeSmaller']}"
							title="#{msgs['treeSmaller']}" />
					</t:htmlTag>
					<t:htmlTag value="li">
						<h:commandButton id="treeLargerButton"
							actionListener="#{editController.adjustTreeSize}"
							image="/media/extand.gif" alt="#{msgs['treeLarger']}"
							title="#{msgs['treeLarger']}" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</f:subview>
</jsp:root>
