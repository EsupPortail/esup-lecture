<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- 
TODO : 
- 
- toutes les chaines � externaliser
- gestion des actions
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
		<h:form>
			<table class="portlet-table-body">
				<tr>
					<td id="TDLeft" style="width: 20%">
					<div id="left">
					<p class="portlet-section-header">Nom du context</p>
					<ul>
						<li class="collapsed">Biblioth�ques</li>
						<li class="expended"><span class="portlet-section-alternate">Vie
						culturelle</span>
						<ul>
							<li><span class="portlet-section-selected">Cin�ma</span></li>
							<li>Th�atre</li>
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
						<li><h:commandLink action="ACTION_MOINS5POURCENT">
							<h:graphicImage value="/images/retract.gif"
								alt="Arbre moins large" />
						</h:commandLink></li>
						<li><h:commandLink action="ACTION_PLUS5POURCENT">
							<h:graphicImage value="/images/extand.gif" alt="Arbre plus large" />
						</h:commandLink></li>
					</ul>
					</div>
					</div>
					</td>
					<td id="TDRight" style="width: 80%">
					<div id="menuRight">
					<div class="menuTitle"><span class="portlet-section-header">Titre
					de la source</span></div>
					<div class="menuButton">
					<ul>
						<li>Afficher : <h:selectOneMenu id="mode"
							valueChangeListener="ACTION_GESTIONDESLUES">
							<f:selectItem itemValue="all" itemLabel="Tous" />
							<f:selectItem itemValue="notRead" itemLabel="Non Lus" />
							<f:selectItem itemValue="unreadFirst"
								itemLabel="D�j� lus en dernier" />
						</h:selectOneMenu></li>
						<li><h:commandLink action="ACTION_MENUANDXML">
							<h:graphicImage value="/images/menuAndXML.gif"
								alt="Afficher l'arbre et les annonces" />
						</h:commandLink></li>
						<li><h:commandLink action="ACION_XMLWITHOUTMENU">
							<h:graphicImage value="/images/XMLWithoutMenu.gif"
								alt="Afficher seulement les annonces" />
						</h:commandLink></li>
					</ul>
					</div>
					</div>
					<div id="right">
					<div class="unreadArticle">
					<p>Du 5 juillet au 30 ao�t : toute r�inscription doit se faire
					obligatoirement en ligne.</p>
					<img
						src="http://portail0.univ-rennes1.fr/html/htmlStatique/images/carte_etudiant_mini.png" />
					Du 5 juillet au 30 ao�t 2006 : ouverture des inscriptions
					universitaires par internet, � partir de votre <b>Espace num�rique
					de travail </b>onglet<b> Me r�inscrire</b><br />
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
					</div>
					<div class="unreadArticle">Article 1</div>
					<div class="unreadArticle">Article 1</div>
					<div class="readArticle">Article 1</div>
					<div class="readArticle">Article 1</div>
					</div>
					</td>
				</tr>
			</table>
		</h:form>
	</f:view>
</jsp:root>

