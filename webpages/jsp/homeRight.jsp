<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<f:subview id="rightSubview">
		<t:htmlTag value="div" id="menuRight" forceId="true">
			<t:htmlTag value="div" styleClass="menuTitle">
				<t:htmlTag value="span" styleClass="portlet-section-header">
					<f:verbatim>Titre de la
	source</f:verbatim>
				</t:htmlTag>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuButton">
				<t:htmlTag value="ul">
					<t:htmlTag value="li">
						<h:outputText value="#{messages.selectorLabel}" />
						<h:selectOneMenu id="mode">
							<f:selectItem itemValue="all" itemLabel="#{messages.all}" />
							<f:selectItem itemValue="notRead" itemLabel="#{messages.notRead}" />
							<f:selectItem itemValue="unreadFirst"
								itemLabel="#{messages.unreadFirst}" />
						</h:selectOneMenu>
					</t:htmlTag>
					<t:htmlTag value="li">
						<h:commandButton actionListener="#{homeBean.toggleTreeVisibility}"
							image="/images/menuAndXML.gif" alt="#{messages.showTree}" />
					</t:htmlTag>
					<t:htmlTag value="li">
						<h:commandButton actionListener="#{homeBean.toggleTreeVisibility}"
							image="/images/XMLWithoutMenu.gif" alt="#{messages.showTree}" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
		<t:htmlTag value="div" id="right">
			<t:htmlTag value="div" styleClass="toggleButton">
				<h:commandLink action="ACION_MARKASREAD">
					<h:graphicImage value="/images/unread.gif"
						alt="#{messages.markAsUnread}" />
				</h:commandLink>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="unreadArticle">
				<f:verbatim>
					<p>Du 5 juillet au 30 ao�t : toute r�inscription doit se faire
					obligatoirement en ligne.</p>
					<img
						src="http://portail0.univ-rennes1.fr/html/htmlStatique/images/carte_etudiant_mini.png" />
	Du 5 juillet au 30 ao�t 2006 : ouverture des inscriptions
	universitaires par internet, � partir de votre <b>Espace num�rique de
					travail </b>onglet<b> Me r�inscrire</b>
					<br />
					<p>Cas particuliers�:<br />
					- Les �tudiants de <b>l'IUT de Saint-Malo</b> doivent se r�inscrire
					avant le <b>31 juillet.</b> <br />
					- Les �tudiants qui passent la <b>2�me session en septembre</b>, ne
					sont pas concern�s par la limite du 30 ao�t <br />
					Durant cette p�riode, r�alisez votre inscription pour l'ann�e
					universitaire 2006-2007 en quelques clics avec paiement des droits
					d'inscription et de s�curit� sociale par carte bancaire.<br />
					Cette inscription officialise votre statut d'�tudiant avec l'envoi
					de votre carte d'�tudiant.</p>
				</f:verbatim>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="toggleButton">
				<h:commandLink action="ACION_MARKASREAD">
					<h:graphicImage value="/images/unread.gif"
						alt="#{messages.markAsUnread}" />
				</h:commandLink>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="unreadArticle">
				<f:verbatim>
					<p>Article 1</p>
				</f:verbatim>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="toggleButton">
				<h:commandLink action="ACION_MARKASREAD">
					<h:graphicImage value="/images/unread.gif"
						alt="#{messages.markAsUnread}" />
				</h:commandLink>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="unreadArticle">
				<f:verbatim>
					<p>Article 1</p>
				</f:verbatim>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="toggleButton">
				<h:commandLink action="ACION_MARKASREAD">
					<h:graphicImage value="/images/read.gif"
						alt="#{messages.markAsRead}" />
				</h:commandLink>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="readArticle">
				<f:verbatim>
					<p>Article 1</p>
				</f:verbatim>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="toggleButton">
				<h:commandLink action="ACION_MARKASREAD">
					<h:graphicImage value="/images/read.gif"
						alt="#{messages.markAsRead}" />
				</h:commandLink>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="readArticle">
				<f:verbatim>
					<p>Article 1</p>
				</f:verbatim>
			</t:htmlTag>
		</t:htmlTag>
	</f:subview>
</jsp:root>
