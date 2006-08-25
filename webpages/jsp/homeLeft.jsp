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
				<f:verbatim>Nom du contexte</f:verbatim>
			</t:htmlTag>
			<!-- Categories -->
			<t:htmlTag value="ul">
				<t:dataList value="#{homeBean.categories}" var="cat" layout="simple">
					<t:htmlTag value="li"
						styleClass="#{cat.expanded ? 'expanded' : 'collapsed'}">
						<h:commandLink action="ACTION_SELECTCATEGORY" value="#{cat.name}" />
						<t:htmlTag value="ul">
							<!-- Souces -->
							<t:dataList value="#{cat.sources}" var="src" layout="simple">
								<t:htmlTag value="li">
									<t:htmlTag value="span" styleClass="portlet-section-selected"
										rendered="#{src.selected}">
										<h:outputText value="#{src.name}" />
									</t:htmlTag>
									<h:commandLink actionListener="#{homeBean.selectASource}"
										rendered="#{src.withUnread and !src.selected}">
										<t:htmlTag value="span" styleClass="portlet-section-alternate">
											<h:outputText value="#{src.name}" />
										</t:htmlTag>
										<f:param name="sourceID" value="#{src.id }" />
									</h:commandLink>
									<h:commandLink action="#{homeBean.selectASource}"
										rendered="#{!src.withUnread and !src.selected}">
										<h:outputText value="#{src.name}" />
										<f:param name="sourceID" value="#{src.id }" />
									</h:commandLink>
								</t:htmlTag>
							</t:dataList>
						</t:htmlTag>
					</t:htmlTag>
				</t:dataList>
			</t:htmlTag>
		</t:htmlTag>
		<!-- Ajust Tree Size buttons -->
		<t:htmlTag value="hr"/>
		<t:htmlTag value="div" id="menuLeft" forceId="true">
			<t:htmlTag value="div" styleClass="menuTitle">&#160;</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuButton">
				<t:htmlTag value="ul">
					<t:htmlTag value="li">
						<h:commandButton id="treeSmallerButton"
							actionListener="#{homeBean.adjustTreeSize}"
							image="/images/retract.gif" alt="#{messages.treeSmaller}" />
					</t:htmlTag>
					<t:htmlTag value="li">
						<h:commandButton id="treeLargerButton"
							actionListener="#{homeBean.adjustTreeSize}"
							image="/images/extand.gif" alt="#{messages.treeLarger}" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</f:subview>
</jsp:root>
