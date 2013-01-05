<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<div id="main" class="portlet-section-body esup-lecture">
	<%-- LEFT --%>
	<%-- TREE --%>
	<div id="left-menu" class="portlet-section lecture-left" role="region">
		<h3 class="portlet-section-header" role="heading">
			<a>${context.name}</a>
		</h3>
		<div class="portlet-section-body">
			<ul class="fl-listmenu">
				<%-- Categories --%>
				<c:forEach var="cat" items="${context.categories}">
					<li class="collapsed">
						<div class="fl-force-left">
							<a href="
								<portlet:actionURL>
									<portlet:param name="action" value="toggleFoldedState" />
									<portlet:param name="catID" value="${cat.id}"/>
								</portlet:actionURL>">homeController.toggleFoldedState</a>
						</div>
						<div class="lecture-category">
							<a>${cat.name}</a>
						</div>
							<ul class="fl-listmenu">
								<%-- Sources --%>
								<c:forEach var="src" items="${cat.sources}">
									<li class="currentSource">
										<a>${src.name}</a>
									</li>
								</c:forEach>
							</ul>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<%-- Adjust Tree Size buttons --%>
	<div class="portlet-section lecture-left">
		<div class="fl-force-left treeButtonsArea">
			<a>homeController.adjustTreeSize -</a>
			<a>homeController.adjustTreeSize +</a>
		</div>
	</div>
	<%-- RIGHT --%>
	<%-- MENU with Source name, sort list and zoom --%>
	<div class="portlet-section lecture-right" role="region">
		<h3 class="fl-force-left">homeController.context.name</h3>
		<div id="hr" layout="block" styleClass="lecture-menu fl-force-right">
			<ul class="fl-tabs">
				<li>msgs['selectorLabel']
					<select id="sidm">
						<option>homeController.availableItemDisplayModes</option>
					</select>
					<a>homeController.changeItemDisplayMode</a>
				</li>
				<li>
					<a>homeController.toggleTreeVisibilityTitle</a>
				</li>
				<li>
					<a>msgs['markAllAsRead']</a>
				</li>
				<li>
					<a>msgs['markAllAsNotRead']</a>
				</li>
			</ul>
		</div>
	</div>
	<%-- Source(s) and Items display --%>
	<div class="portlet-section lecture-right" role="region">
		<%-- categories display --%>
			<%-- sources display --%>
				<div id="d4s" layout="block">
					<h4 class="portlet-section-header fl-push">cat.name &gt; source.name</h4>
					<%-- Items display --%>
						<%-- Read/Unread Button --%>
						<div class="lecture-article fl-push">
							<div class="lecture-toggleButton fl-force-left">
								<a>msgs['markAsRead']</a>
								<a>msgs['markAsUnread']</a>
							</div>
							<%-- Item Display --%>
							<div class="lecture-readArticle">
								item.htmlContent
							</div>
						</div>
				</div>
	</div>	
</div>
