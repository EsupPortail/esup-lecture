<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<!-- 
	<f:subview id="rightSubview">
 -->
		<!-- MENU with Source name, sort list and zoom -->
		<t:htmlTag value="div" id="menuRight" forceId="true">
			<t:htmlTag value="span" styleClass="portlet-section-header" rendered="#{homeController.guestMode}">
				<h:outputText value="#{homeController.selectedCategory.name}"/>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuTitle" rendered="#{!homeController.guestMode}">
				<t:htmlTag value="span" styleClass="portlet-section-header">
					<h:outputText value="#{homeController.selectedCategory.name}"/>
				</t:htmlTag>
			</t:htmlTag>
			<t:htmlTag value="div" styleClass="menuButton" rendered="#{!homeController.guestMode}">
				<t:htmlTag value="ul">
					<t:htmlTag value="li" rendered="#{homeController.selectedCategory.withSelectedSource}">
						<h:outputText value="#{msgs['selectorLabel']}" />
						<h:selectOneMenu value="#{homeController.itemDisplayMode}" converter="#{itemDisplayModeConverter}">
                            <!--  onchange="submit();" -->
							<f:selectItem itemValue="#{homeController.all}" itemLabel="#{msgs['all']}" />
							<f:selectItem itemValue="#{homeController.unread}" itemLabel="#{msgs['notRead']}" />
							<f:selectItem itemValue="#{homeController.unreadfirt}" itemLabel="#{msgs['unreadFirst']}" />
						</h:selectOneMenu>
						<h:commandButton id="submit" value="#{msgs['changeItemDisplayModeButtonLabel']}" action="#{homeController.changeItemDisplayMode}"/>
					</t:htmlTag>
					<t:htmlTag id="menuAndXML" value="li" rendered="#{!homeController.treeVisible}">
						<h:commandButton action="#{homeController.toggleTreeVisibility}"
							image="/media/menuAndXML.gif" alt="#{msgs['showTree']}" title="#{msgs['showTree']}"
							styleClass="valign"/>
					</t:htmlTag>
					<t:htmlTag id="XMLWithoutMenu" value="li" rendered="#{homeController.treeVisible}">
						<h:commandButton action="#{homeController.toggleTreeVisibility}"
							image="/media/XMLWithoutMenu.gif" alt="#{msgs['hideTree']}" title="#{msgs['hideTree']}"
							styleClass="valign"/>             
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
		<!-- Items display -->
		<t:htmlTag value="div" id="right">
			<t:htmlTag value="hr"/>
            <t:dataList value="#{homeController.selectedCategory.selectedOrAllSources}" var="source" layout="simple" >
				<t:htmlTag value="div" styleClass="portlet-section-subheader source-title">
				    <h:outputText value="#{source.name}"/>
				</t:htmlTag>
	            <t:dataList value="#{source.sortedItems}" var="item" layout="simple">
	                <!-- Read/Unread Button -->
	                <t:htmlTag value="div" styleClass="toggleButton">
	                    <h:commandButton action="#{homeController.toggleItemReadState}"
	                        image="/media/unread.png" alt="#{msgs['markAsRead']}"
	                        title="#{msgs['markAsRead']}" rendered="#{!item.read and !homeController.guestMode and !item.dummy}">
                            <t:updateActionListener property="#{homeController.ualSource}" value="#{source}" />
	                        <t:updateActionListener property="#{homeController.ualItem}" value="#{item}" />
	                    </h:commandButton>
	                    <h:commandButton action="#{homeController.toggleItemReadState}"
	                        image="/media/read.png" alt="#{msgs['markAsUnread']}"
	                        title="#{msgs['markAsUnread']}" rendered="#{item.read and !homeController.guestMode and !item.dummy}">
                            <t:updateActionListener property="#{homeController.ualSource}" value="#{source}" />
	                        <t:updateActionListener property="#{homeController.ualItem}" value="#{item}" />
	                    </h:commandButton>
	                </t:htmlTag>
	                <!-- Item Display -->
	                <t:htmlTag value="div"
	                    styleClass="#{item.read ? 'readArticle' : 'unreadArticle'}">
	                    <f:verbatim>
	                        <h:outputText value="#{item.htmlContent}" escape="false" />
	                    </f:verbatim>
	                </t:htmlTag>
	            </t:dataList>
            </t:dataList>
		</t:htmlTag>
<!-- 
	</f:subview>
 -->
</jsp:root>
