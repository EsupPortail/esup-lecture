<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<!-- 
	<f:subview id="leftSubview">
 -->
	<!-- TREE -->
	<t:htmlTag value="div" id="left" forceId="true">
		<t:div styleClass="#{homeController.guestMode ? 'conteneurLogoGuest' : 'conteneurLogo' }">
		</t:div>
		<!-- Title -->
		<t:htmlTag value="span" styleClass="portlet-section-header">
			<h:outputText value="#{homeController.context.name}" />
		</t:htmlTag>
		<!-- Categories -->
		<t:htmlTag value="ul">
			<t:dataList value="#{homeController.context.categories}" var="cat"
				layout="simple">
				<t:htmlTag value="li"
					styleClass="#{cat.folded ? 'collapsed' : 'expanded'}">
					<h:commandButton action="#{homeController.toggleFoldedState}"
						image="/media/moins.gif" alt="#{msgs['colapseCategory']}"
						title="#{msgs['colapseCategory']}" rendered="#{!cat.folded}">
						<t:updateActionListener property="#{homeController.ualCategory}"
							value="#{cat}" />
					</h:commandButton>
					<h:commandButton action="#{homeController.toggleFoldedState}"
						image="/media/plus.gif" alt="#{msgs['expandCategory']}"
						title="#{msgs['expandCategory']}" rendered="#{cat.folded}">
						<t:updateActionListener property="#{homeController.ualCategory}"
							value="#{cat}" />
					</h:commandButton>
					<h:commandButton action="#{homeController.selectElement}"
						alt="#{cat.name}" title="#{cat.name}" value="#{cat.name}"
						styleClass="#{cat.id == homeController.ualCategory.id ? 'buttonStyle currentCategory' : 'buttonStyle otherCategory'}">
						<t:updateActionListener property="#{homeController.ualCategory}"
							value="#{cat}" />
						<t:updateActionListener property="#{homeController.ualSource}"
							value="#{null}" />
					</h:commandButton>
					<t:htmlTag value="ul" rendered="#{!cat.folded}">
						<!-- Sources -->
						<t:dataList value="#{cat.sources}" var="src" layout="simple">
							<t:htmlTag value="li"
								styleClass=" #{src.id == homeController.ualSource.id ? 'currentSource' : 'otherSource'}">
								<h:commandButton action="#{homeController.selectElement}"
									image="/media/puce.gif" alt="#{msgs['selectSource']}"
									title="#{msgs['selectSource']}">
									<t:updateActionListener
										property="#{homeController.ualCategory}" value="#{cat}" />
									<t:updateActionListener property="#{homeController.ualSource}"
										value="#{src}" />
								</h:commandButton>
								<h:commandButton action="#{homeController.selectElement}"
									alt="#{src.name}" title="#{src.name}" value="#{src.name}"
									styleClass="buttonStyle">
									<t:updateActionListener
										property="#{homeController.ualCategory}" value="#{cat}" />
									<t:updateActionListener property="#{homeController.ualSource}"
										value="#{src}" />
								</h:commandButton>
								<h:outputText value="(#{src.unreadItemsNumber})" styleClass="unreadItemsNumber"
									rendered="#{src.unreadItemsNumber > 0}"/>
							</t:htmlTag>
						</t:dataList>
					</t:htmlTag>
				</t:htmlTag>
			</t:dataList>
		</t:htmlTag>
	</t:htmlTag>
	<!-- Adjust Tree Size buttons 
		<t:htmlTag value="hr" /> -->

	<t:htmlTag value="div" id="menuLeft" forceId="true"
		rendered="#{!homeController.guestMode}">
		<t:htmlTag value="div" styleClass="menuButton">
			<t:htmlTag value="ul">
				<t:htmlTag value="li">
					<h:commandButton id="treeSmallerButton"
						actionListener="#{homeController.adjustTreeSize}"
						image="/media/retract.gif" alt="#{msgs['treeSmaller']}"
						title="#{msgs['treeSmaller']}" />
				</t:htmlTag>
				<t:htmlTag value="li">
					<h:commandButton id="treeLargerButton"
						actionListener="#{homeController.adjustTreeSize}"
						image="/media/extand.gif" alt="#{msgs['treeLarger']}"
						title="#{msgs['treeLarger']}" />
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>

		<t:div styleClass="menuBas">
			<t:div>
				<h:commandButton styleClass="buttonNoStyle" id="editButton"
					action="navigationEdit" value="#{msgs['edit']}"
					alt="#{msgs['edit']}" title="#{msgs['edit']}" />
			</t:div>
		</t:div>
	</t:htmlTag>

	<!-- 
	</f:subview>
 -->
</jsp:root>
