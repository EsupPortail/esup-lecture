<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="main" class="portlet-section-body esup-lecture">
	<%-- LEFT --%>
	<%-- TREE --%>
	<div id="left-menu" class="portlet-section lecture-left">
		<c:if test="${guestMode}">
			<div class="lecture-logoGuest"></div>
		</c:if>
		<c:if test="${!guestMode}">
			<div class="lecture-logo"></div>
		</c:if>
		<h3 class="portlet-section-header">
			<a href="
				<portlet:actionURL>
					<portlet:param name="action" value="selectContext" />
				</portlet:actionURL>">${context.name}</a>
		</h3>
		<div class="portlet-section-body">
			<ul class="fl-listmenu">
				<%-- Categories --%>
				<c:forEach var="cat" items="${context.categories}"> 
					<li class="<c:if test="${cat.folded}">collapsed</c:if><c:if test="${!cat.folded}">expanded</c:if>">
						<div class="fl-force-left">
							<a href="<portlet:actionURL><portlet:param name="action" value="toggleFoldedState" /><portlet:param name="catID" value="${cat.id}"/></portlet:actionURL>">homeController.toggleFoldedState</a>
						</div>
						<div class="lecture-category">
							<a href="<portlet:actionURL><portlet:param name="action" value="selectCategory" /><portlet:param name="catID" value="${cat.id}"/></portlet:actionURL>">${cat.name}</a>
						</div>
							<c:if test="${!cat.folded}"><ul class="fl-listmenu">
								<%-- Sources --%>
								<c:forEach var="src" items="${cat.sources}"><li class="currentSource">
									<a href="<portlet:actionURL><portlet:param name="action" value="selectSource" /><portlet:param name="catID" value="${cat.id}"/><portlet:param name="srcID" value="${src.id}"/></portlet:actionURL>">${src.name}</a>
								</li></c:forEach>
							</ul></c:if>
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
	<div class="portlet-section lecture-right">
		<h3 class="fl-force-left">homeController.context.name</h3>
		<div id="hr" class="lecture-menu fl-force-right">
			<ul class="fl-tabs">
				<li><spring:message code="selectorLabel"></spring:message>
					<select id="sidm">
						<option>homeController.availableItemDisplayModes</option>
					</select>
					<a>homeController.changeItemDisplayMode</a>
				</li>
				<li>
					<a>homeController.toggleTreeVisibilityTitle</a>
				</li>
				<li>
					<a><spring:message code="markAllAsRead"/></a>
				</li>
				<li>
					<a><spring:message code="markAllAsNotRead"/></a>
				</li>
			</ul>
		</div>
	</div>
	<%-- Source(s) and Items display --%>
	<div class="portlet-section lecture-right">
		<%-- categories display --%>
		<c:forEach var="cat" items="${context.selectedOrAllCategories}">
			<%-- sources display --%>
			<c:forEach var="src" items="${cat.selectedOrAllSources}">
				<div id="d4s">
					<h4 class="portlet-section-header fl-push">${cat.name} &gt; ${src.name}</h4>
					<%-- Items display --%>
					<c:forEach var="item" items="${src.sortedItems}">
						<%-- Read/Unread Button --%>
						<div class="lecture-article fl-push">
							<div class="lecture-toggleButton fl-force-left">
								<c:if test="${!item.read and !guestMode and !item.dummy and cat.userCanMarkRead}">
									<a class="lecture-markAsReadButton" href="
										<portlet:actionURL>
											<portlet:param name="action" value="toggleItemReadState" />
											<portlet:param name="catID" value="${cat.id}"/>
											<portlet:param name="srcID" value="${src.id}"/>
											<portlet:param name="itemID" value="${item.id}"/>
										</portlet:actionURL>">
										<spring:message code="markAsRead"/>
									</a>
								</c:if>
								<c:if test="${item.read and !guestMode and !item.dummy and cat.userCanMarkRead}}">
									<a class="lecture-markAsUnreadButton" href="
										<portlet:actionURL>
											<portlet:param name="action" value="toggleItemReadState" />
											<portlet:param name="catID" value="${cat.id}"/>
											<portlet:param name="srcID" value="${src.id}"/>
											<portlet:param name="itemID" value="${item.id}"/>
										</portlet:actionURL>">
										<spring:message code="markAsUnread"/>
									</a>
								</c:if>
							</div>
							<%-- Item Display --%>
							<div class="lecture-readArticle">
								${item.htmlContent}
							</div>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</c:forEach>
	</div>	
</div>
