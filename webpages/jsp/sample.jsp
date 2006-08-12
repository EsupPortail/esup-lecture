<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- 
TODO : 
- gestion des actions dans un bean
--- en cours mais il faut initialiser homeBean
--- passer tous les bouton en commandbutton
 -->
<!-- 
CSS Class :
Portlet :
portlet-table-body: table body
portlet-section-header: header
portlet-section-alternate: With unread element
portlet-section-selected: for selected element

Lecture specific:
collapsed: collapsed tree element
expended: expended tree element
menuTitle: text in menu bar
menuButton: buttons in menu bar
unreadArticle: unread article
readArticle: read article
toggleButton: read/unread toggle button
 -->
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<link rel="stylesheet"
		href="http://localhost:8080/esup-lecture/stylesheets/lecture.css"
		media="screen" />
	<f:view>
		<f:loadBundle basename="messages" var="messages" />
		<!-- TODO : trouver un autre moyen d'initialiser homeBean -->
		<h:outputText value="#{homeBean.treeSize}" />
		<h:form id="home">
			<table class="portlet-table-body">
				<tr>
					<td id="TDLeft" style="width: ${sessionScope.homeBean.treeSize}%">
					<div id="left">
					<p class="portlet-section-header">Nom du contexte</p>
					<ul>
						<li class="collapsed"><h:commandLink
							action="ACTION_SELECTCATEGORY" value="Bibliothèques" /></li>
						<li class="expended"><span class="portlet-section-alternate">Vie
						culturelle</span>
						<ul>
							<li><span class="portlet-section-selected">Cinéma</span></li>
							<li><h:commandLink action="ACTION_SELECTSOURCE" value="Théatre" /></li>
							<li><span class="portlet-section-alternate">Concert</span></li>
							<li>Danse</li>
						</ul>
						</li>
						<li class="collapsed">Vie de l'ENT</li>
					</ul>
					</div>
					<div id="menuLeft">
					<div class="menuTitle">&#160;</div>
					<div class="menuButton">
					<ul>
						<li><h:commandButton id="treeSmaller" 
							actionListener="#{homeBean.adjustTreeSize}" image="/images/retract.gif"
							alt="#{messages.treeSmaller}" /></li>
						<li><h:commandButton id="treeLarger"
							actionListener="#{homeBean.adjustTreeSize}" image="/images/extand.gif"
							alt="#{messages.treeLarger}" /></li>
					</ul>
					</div>
					</div>
					</td>
					<td id="TDRight"
						style="width: ${100 - sessionScope.homeBean.treeSize}%">
					<div id="menuRight">
					<div class="menuTitle"><span class="portlet-section-header">Titre
					de la source</span></div>
					<div class="menuButton">
					<ul>
						<li><h:outputText value="#{messages.selectorLabel}" /><h:selectOneMenu
							id="mode">
							<f:selectItem itemValue="all" itemLabel="#{messages.all}" />
							<f:selectItem itemValue="notRead" itemLabel="#{messages.notRead}" />
							<f:selectItem itemValue="unreadFirst"
								itemLabel="#{messages.unreadFirst}" />
						</h:selectOneMenu></li>
						<li><h:commandLink action="ACTION_MENUANDXML">
							<h:graphicImage value="/images/menuAndXML.gif"
								alt="#{messages.showTree}" />
						</h:commandLink></li>
						<li><h:commandLink action="ACION_XMLWITHOUTMENU">
							<h:graphicImage value="/images/XMLWithoutMenu.gif"
								alt="#{messages.hideTree}" />
						</h:commandLink></li>
					</ul>
					</div>
					</div>
					<div id="right">
					<div class="toggleButton"><h:commandLink action="ACION_MARKASREAD">
						<h:graphicImage value="/images/unread.gif"
							alt="#{messages.markAsUnread}" />
					</h:commandLink></div>
					<div class="unreadArticle">
					<p>Du 5 juillet au 30 août : toute réinscription doit se faire
					obligatoirement en ligne.</p>
					<img
						src="http://portail0.univ-rennes1.fr/html/htmlStatique/images/carte_etudiant_mini.png" />
					Du 5 juillet au 30 août 2006 : ouverture des inscriptions
					universitaires par internet, à partir de votre <b>Espace numérique
					de travail </b>onglet<b> Me réinscrire</b><br />
					<p>Cas particuliersé:<br />
					- Les étudiants de <b>l'IUT de Saint-Malo</b> doivent se réinscrire
					avant le <b>31 juillet.</b> <br />
					- Les étudiants qui passent la <b>2ème session en septembre</b>, ne
					sont pas concernés par la limite du 30 août <br />
					Durant cette période, réalisez votre inscription pour l'année
					universitaire 2006-2007 en quelques clics avec paiement des droits
					d'inscription et de sécurité sociale par carte bancaire.<br />
					Cette inscription officialise votre statut d'étudiant avec l'envoi
					de votre carte d'étudiant.</p>
					</div>
					<div class="toggleButton"><h:commandLink action="ACION_MARKASREAD">
						<h:graphicImage value="/images/unread.gif"
							alt="#{messages.markAsUnread}" />
					</h:commandLink></div>
					<div class="unreadArticle">
					<p>Article 1</p>
					</div>
					<div class="toggleButton"><h:commandLink action="ACION_MARKASREAD">
						<h:graphicImage value="/images/unread.gif"
							alt="#{messages.markAsUnread}" />
					</h:commandLink></div>
					<div class="unreadArticle">
					<p>Article 1</p>
					</div>
					<div class="toggleButton"><h:commandLink action="ACION_MARKASREAD">
						<h:graphicImage value="/images/read.gif"
							alt="#{messages.markAsRead}" />
					</h:commandLink></div>
					<div class="readArticle">
					<p>Article 1</p>
					</div>
					<div class="toggleButton"><h:commandLink action="ACION_MARKASREAD">
						<h:graphicImage value="/images/read.gif"
							alt="#{messages.markAsRead}" />
					</h:commandLink></div>
					<div class="readArticle">
					<p>Article 1</p>
					</div>
					</div>
					</td>
				</tr>
			</table>
		</h:form>
	</f:view>
</jsp:root>

