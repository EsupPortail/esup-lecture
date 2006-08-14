<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<f:subview id="leftSubview">
		<t:htmlTag value="div" id="left" forceId="true">
			<t:htmlTag value="p" styleClass="portlet-section-header">
				<f:verbatim>Nom du contexte</f:verbatim>
			</t:htmlTag>
			<t:htmlTag value="ul">
				<t:htmlTag value="li" styleClass="collapsed">
					<h:commandLink action="ACTION_SELECTCATEGORY" value="Bibliothèques" />
				</t:htmlTag>
				<t:htmlTag value="li" styleClass="expended">
					<t:htmlTag value="span" styleClass="portlet-section-alternate">
						<f:verbatim>Vie
				culturelle</f:verbatim>
					</t:htmlTag>
					<t:htmlTag value="ul">
						<t:htmlTag value="li">
							<t:htmlTag value="span" styleClass="portlet-section-selected">
								<f:verbatim>Cinéma</f:verbatim>
							</t:htmlTag>
						</t:htmlTag>
						<t:htmlTag value="li">
							<h:commandLink action="ACTION_SELECTSOURCE" value="Théatre" />
						</t:htmlTag>
						<t:htmlTag value="li">
							<t:htmlTag value="span" styleClass="portlet-section-alternate">
								<f:verbatim>Concert</f:verbatim>
							</t:htmlTag>
						</t:htmlTag>
						<t:htmlTag value="li">
							<f:verbatim>Danse</f:verbatim>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
				<t:htmlTag value="li" styleClass="collapsed">
					<f:verbatim>Vie de l'ENT</f:verbatim>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
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
