<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<f:subview id="leftSubview">
		<!-- TREE -->
		<t:htmlTag value="div" id="left" forceId="true">
			<!-- Title -->
			<t:htmlTag value="p" styleClass="portlet-section-header">
				<h:outputText value="#{homeBean.context.name}"/>
			</t:htmlTag>
			<!-- Categories -->
			<t:htmlTag value="ul">
			<!-- 
				<t:dataList value="#{homeBean.categories}" var="cat" layout="simple">
			 -->
				<t:dataList value="#{homeBean.context.categories}" var="cat" layout="simple">
					<!-- 
					<t:htmlTag value="li"
						styleClass="#{cat.expanded ? 'expanded' : 'collapsed'}">
					 -->
					<t:htmlTag value="li">
						<!-- 
						<h:commandButton action="#{homeBean.selectElement2}"
							image="/images/moins.gif" alt="#{messages.colapseCategory}"
							title="#{messages.colapseCategory}" rendered="#{cat.expanded}">
							<t:updateActionListener property="#{homeBean.sourceID}" value="0" />
							<t:updateActionListener property="#{homeBean.categoryID}"
								value="#{cat.id}" />
						</h:commandButton>
						<h:commandButton action="#{homeBean.selectElement2}"
							image="/images/plus.gif" alt="#{messages.expandCategory}"
							title="#{messages.expandCategory}" rendered="#{!cat.expanded}">
							<t:updateActionListener property="#{homeBean.sourceID}" value="0" />
							<t:updateActionListener property="#{homeBean.categoryID}"
								value="#{cat.id}" />
						</h:commandButton>
						 -->
						<h:outputText value="#{cat.name}" />
						<!-- 
						<t:htmlTag value="ul" rendered="#{cat.expanded}">
						 -->
						<t:htmlTag value="ul">
							<!-- Souces -->
							<t:dataList value="#{cat.sources}" var="src" layout="simple">
								<t:htmlTag value="li">
								<!-- 
									<h:commandButton action="#{homeBean.selectElement2}"
										image="/images/puce.gif" alt="#{messages.selectSource}" title="#{messages.selectSource}"
										rendered="#{!src.selected or !cat.selected}">
										<t:updateActionListener property="#{homeBean.sourceID}"
											value="#{src.id}" />
										<t:updateActionListener property="#{homeBean.categoryID}"
											value="#{cat.id}" />
									</h:commandButton>
									<h:graphicImage url="/images/puce.gif"
										alt="#{messages.currentSource}" title="#{messages.currentSource}"
										rendered="#{src.selected and cat.selected}" />
									<t:htmlTag value="span" styleClass="portlet-section-alternate"
										rendered="#{src.withUnread}">
										<h:outputText value="#{src.name}" />
									</t:htmlTag>
								 -->
								 <!-- 
									<h:outputText value="#{src.name}" rendered="#{!src.withUnread}" />
								  -->
									<h:outputText value="#{src.name}" />
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
							actionListener="#{homeBean.adjustTreeSize}"
							image="/images/retract.gif" alt="#{messages.treeSmaller}" title="#{messages.treeSmaller}"/>
					</t:htmlTag>
					<t:htmlTag value="li">
						<h:commandButton id="treeLargerButton"
							actionListener="#{homeBean.adjustTreeSize}"
							image="/images/extand.gif" alt="#{messages.treeLarger}" title="#{messages.treeLarger}"/>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</f:subview>
</jsp:root>
