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
				<h:outputText value="#{editController.context.name} (EDIT)"/>
			</t:htmlTag>
			<!-- Categories -->
			<t:htmlTag value="ul">
				<t:dataList value="#{editController.context.categories}" var="cat" layout="simple">
					<t:htmlTag value="li"
						styleClass="#{cat.folded ? 'collapsed' : 'expanded' }">
						<h:commandButton action="#{editController.selectElement}"
							image="/media/moins.gif" alt="#{msgs['colapseCategory']}"
							title="#{msgs['colapseCategory']}" rendered="#{!cat.folded}">
							<t:updateActionListener property="#{editController.sourceId}" value="0" />
							<t:updateActionListener property="#{editController.categoryId}"
								value="#{cat.id}" />
						</h:commandButton>
						<h:commandButton action="#{editController.selectElement}"
							image="/media/plus.gif" alt="#{msgs['expandCategory']}"
							title="#{msgs['expandCategory']}" rendered="#{cat.folded}">
							<t:updateActionListener property="#{editController.sourceId}" value="0" />
							<t:updateActionListener property="#{editController.categoryId}"
								value="#{cat.id}" />
						</h:commandButton>
						<h:outputText value="#{cat.name}" />
						<t:htmlTag value="ul" rendered="#{!cat.folded}">
							<!-- Souces -->
							<t:dataList value="#{cat.sources}" var="src" layout="simple">
								<t:htmlTag value="li">
									<h:commandButton action="#{editController.selectElement}"
										image="/media/puce.gif" alt="#{msgs['selectSource']}" title="#{msgs['selectSource']}"
										rendered="#{!((editController.context.selectedCategory.id == cat.id) and (cat.selectedSource.id == src.id))}">
										<t:updateActionListener property="#{editController.sourceId}"
											value="#{src.id}" />
										<t:updateActionListener property="#{editController.categoryId}"
											value="#{cat.id}" />
									</h:commandButton>
									<h:graphicImage url="/media/puce.gif"
										alt="#{msgs['currentSource']}" title="#{msgs['currentSource']}"
										rendered="#{((editController.context.selectedCategory.id == cat.id) and (cat.selectedSource.id == src.id))}" />
									<!-- TODO
									<t:htmlTag value="span" styleClass="portlet-section-alternate"
										rendered="#{src.withUnread}">
									 -->
										<h:outputText value="#{src.name}" />
									<!-- 
									</t:htmlTag>
									 -->
									 <!-- TODO
									<h:outputText value="#{src.name}" rendered="#{!src.withUnread}" />
									  -->
								</t:htmlTag>
							</t:dataList>
						</t:htmlTag>
					</t:htmlTag>
				</t:dataList>
			</t:htmlTag>
		</t:htmlTag>
		<!-- Ajust Tree Size buttons -->
		<t:htmlTag value="hr" />
		<t:htmlTag value="div" id="menuLeft" forceId="true">
			<t:htmlTag value="div" styleClass="menuTitle">
				<h:commandButton id="homeButton"
					action="navigationHome"
					image="/media/go-home.png" alt="#{msgs['home']}"
					title="#{msgs['home']}" />
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuButton">
				<t:htmlTag value="ul">
					<t:htmlTag value="li">
						<h:commandButton id="treeSmallerButton"
							actionListener="#{editController.adjustTreeSize}"
							image="/media/retract.gif" alt="#{msgs['treeSmaller']}" title="#{msgs['treeSmaller']}"/>
					</t:htmlTag>
					<t:htmlTag value="li">
						<h:commandButton id="treeLargerButton"
							actionListener="#{editController.adjustTreeSize}"
							image="/media/extand.gif" alt="#{msgs['treeLarger']}" title="#{msgs['treeLarger']}"/>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</f:subview>
</jsp:root>
