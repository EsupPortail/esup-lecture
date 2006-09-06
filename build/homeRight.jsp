<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<f:subview id="rightSubview">
		<!-- MENU with Source name, sort list and zoom -->
		<t:htmlTag value="div" id="menuRight" forceId="true">
			<t:htmlTag value="div" styleClass="menuTitle">
				<t:htmlTag value="span" styleClass="portlet-section-header">
					<h:outputText value="#{homeBean.currentCategoryName}" />
				</t:htmlTag>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuButton">
				<t:htmlTag value="ul">
					<t:htmlTag value="li">
						<h:outputText value="#{messages.selectorLabel}" />
						<h:selectOneMenu value="#{homeBean.itemDisplayMode}" onchange="document.forms['home'].elements['home:rightSubview:submit'].click();">
							<f:selectItem itemValue="all" itemLabel="#{messages.all}" />
							<f:selectItem itemValue="notRead" itemLabel="#{messages.notRead}" />
							<f:selectItem itemValue="unreadFirst"
								itemLabel="#{messages.unreadFirst}" />
						</h:selectOneMenu>
						<h:commandButton id="submit" value="#{messages.changeItemDisplayModeButtonLabel}" action="#{homeBean.changeItemDisplayMode}"/>
					</t:htmlTag>
					<t:htmlTag id="menuAndXML" value="li" rendered="#{!homeBean.treeVisible}">
						<h:commandButton actionListener="#{homeBean.toggleTreeVisibility}"
							image="/images/menuAndXML.gif" alt="#{messages.showTree}" />
					</t:htmlTag>
					<t:htmlTag id="XMLWithoutMenu" value="li" rendered="#{homeBean.treeVisible}">
						<h:commandButton actionListener="#{homeBean.toggleTreeVisibility}"
							image="/images/XMLWithoutMenu.gif" alt="#{messages.showTree}" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
		<!-- Items display -->
		<t:htmlTag value="div" id="right">
			<t:dataList value="#{homeBean.items}" var="item" layout="simple">
				<!-- Read/Unread Button -->
				<t:htmlTag value="div" styleClass="toggleButton">
					<h:commandLink actionListener="#{homeBean.toggleItemReadState}"
						rendered="#{!item.read}">
						<f:param name="itemID" value="#{item.id}" />
						<h:graphicImage value="/images/unread.gif"
							alt="#{messages.markAsUnread}" />
					</h:commandLink>
					<h:commandLink actionListener="#{homeBean.toggleItemReadState}"
						rendered="#{item.read}">
						<f:param name="itemID" value="#{item.id}" />
						<h:graphicImage value="/images/read.gif"
							alt="#{messages.markAsRead}" />
					</h:commandLink>
				</t:htmlTag>
				<!-- Item Display -->
				<t:htmlTag value="div"
					styleClass="#{item.read ? 'readArticle' : 'unreadArticle'}">
					<f:verbatim>
						<h:outputText value="#{item.body}" escape="false" />
					</f:verbatim>
				</t:htmlTag>
			</t:dataList>
		</t:htmlTag>
	</f:subview>
</jsp:root>
