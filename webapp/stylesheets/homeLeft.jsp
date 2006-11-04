<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<f:subview id="leftSubview">
		<!-- TREE -->
		<t:htmlTag value="div" id="left" forceId="true">
			<!-- Title -->
			<t:htmlTag value="p" styleClass="portlet-section-header">
				<h:outputText value="#{homeController.context.name}"/>
			</t:htmlTag>
			<!-- Categories -->
			<t:htmlTag value="ul">
				<t:dataList value="#{homeController.context.categories}" var="cat" layout="simple">
					<t:htmlTag value="li"
						styleClass="#{cat.folded ? 'collapsed' : 'expanded' }">
						<h:commandButton action="#{homeController.selectElement}"
							image="/media/moins.gif" alt="#{msgs['colapseCategory']}"
							title="#{msgs['colapseCategory']}" rendered="#{!cat.folded}">
							<t:updateActionListener property="#{homeController.sourceID}" value="0" />
							<t:updateActionListener property="#{homeController.categoryID}"
								value="#{cat.id}" />
						</h:commandButton>
						<h:commandButton action="#{homeController.selectElement}"
							image="/media/plus.gif" alt="#{msgs['expandCategory']}"
							title="#{msgs['expandCategory']}" rendered="#{cat.folded}">
							<t:updateActionListener property="#{homeController.sourceID}" value="0" />
							<t:updateActionListener property="#{homeController.categoryID}"
								value="#{cat.id}" />
						</h:commandButton>
						<h:outputText value="#{cat.name}" />
						<t:htmlTag value="ul" rendered="#{!cat.folded}">
							<!-- Souces -->
							<t:dataList value="#{cat.sources}" var="src" layout="simple">
								<t:htmlTag value="li">
								<!-- TODO
									<h:commandButton action="#{homeController.selectElement}"
										image="/media/puce.gif" alt="#{msgs['selectSource']}" title="#{msgs['selectSource']}"
										rendered="#{!src.selected or !cat.selected}">
								 -->
									<h:commandButton action="#{homeController.selectElement}"
										image="/media/puce.gif" alt="#{msgs['selectSource']}" title="#{msgs['selectSource']}">
										<t:updateActionListener property="#{homeController.sourceID}"
											value="#{src.id}" />
										<t:updateActionListener property="#{homeController.categoryID}"
											value="#{cat.id}" />
									</h:commandButton>
									<!-- TODO
									<h:graphicImage url="/media/puce.gif"
										alt="#{msgs['currentSource']}" title="#{msgs['currentSource']}"
										rendered="#{src.selected and cat.selected}" />
									 -->
									<h:graphicImage url="/media/puce.gif"
										alt="#{msgs['currentSource']}" title="#{msgs['currentSource']}"/>
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
			<t:htmlTag value="div" styleClass="menuTitle">&#160;</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuButton">
				<t:htmlTag value="ul">
					<t:htmlTag value="li">
						<h:commandButton id="treeSmallerButton"
							actionListener="#{homeController.adjustTreeSize}"
							image="/media/retract.gif" alt="#{msgs['treeSmaller']}" title="#{msgs['treeSmaller']}"/>
					</t:htmlTag>
					<t:htmlTag value="li">
						<h:commandButton id="treeLargerButton"
							actionListener="#{homeController.adjustTreeSize}"
							image="/media/extand.gif" alt="#{msgs['treeLarger']}" title="#{msgs['treeLarger']}"/>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</f:subview>
</jsp:root>
