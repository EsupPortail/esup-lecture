<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server"%>
<rs:aggregatedResources path="/resources.xml" />
<%-- <portlet:resourceURL id='toggleItemReadState' var="toggleItemReadState"></portlet:resourceURL> --%>
<portlet:resourceURL id='toggleAllItemReadState'
  var="toggleAllItemReadState"></portlet:resourceURL>
<%-- <portlet:resourceURL id='FilteredItem' var="FilteredItem"></portlet:resourceURL> --%>
<portlet:actionURL var="toggleItemReadState">
  <portlet:param name="action" value="toggleItemReadState" />
</portlet:actionURL>

<portlet:resourceURL var="FilteredItem" id="filterUnreadOnly" >
	<!-- portlet:param name="javax.portlet.action" value="filterUnreadOnly"/-->
</portlet:resourceURL>
<portlet:resourceURL var="markReadUrl" id="markRead" >
	<!-- portlet:param name="javax.portlet.action" value="filterUnreadOnly"/-->
</portlet:resourceURL>
<%-- <portlet:actionURL var="toggleAllItemReadState"> --%>
<%--   <portlet:param name="action" value="toggleAllItemReadState"/> --%>
<%-- </portlet:actionURL> --%>
<%-- <portlet:actionURL var="FilteredItem"> --%>
<%--   <portlet:param name="action" value="FilteredItem"/> --%>
<%-- </portlet:actionURL> --%>
<c:set var="n">
  <portlet:namespace />
</c:set>
<c:set var="ctx_name">${contexte.id}</c:set>
<input type="hidden" id="${n}treeVisible"
  value="${contexte.treeVisible}" />
<script type="text/javascript">
	lecture.init(
		up.jQuery,
		'${n}',
		'${markReadUrl}' , 
		'${toggleItemReadState}' , 
		'${toggleAllItemReadState}',
		'${FilteredItem}',
		'${contexte.itemDisplayMode}');
	console.log("test after lecture.init" + lecture.${n}.urlFilterItem);
</script>
<div class="esup-lecture portlet-container ${n}  ${contexte.viewDef=='true' ? 'viewDef' :''} ${contexte.modePublisher=='true' ? 'modePublisher' : 'notModePublisher'}"
  id="lecture-${n}">
  <div id="${n}homeJsp">
    <span class="spinner"></span>
    
  <div 	id="${n}modalPublisher"
  		class="modal fade" 
  		role="dialog" 
  		tabindex="0">
  <div class="modal-content">
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"
        aria-label="Close">
        <span aria-hidden="true">&times;</span>
      </button>
      <h3 class="modal-title" id="myModalLabel{$uuid}">
        <xsl:value-of select="title" />
      </h3>
    </div>
    <div class="modal-body">
      <p>REMOTE CONTENT</p>
    </div>
    <div class="modal-footer">
      <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="close" /></button>
    </div>
  </div>
</div>
    
    <c:if
      test="${contexte.viewDef=='true' && (contexte.modePublisher=='true') }"> 
      <%@include file="homeAccueil.jsp"%>
    </c:if>
    <c:if
      test="${contexte.viewDef=='false'||contexte.modePublisher=='false'}">
      <%@include file="homeAutre.jsp"%>
    </c:if>
  </div>
  <iframe class="${n} iframeCacher" style="display:none!important" ></iframe>
</div>
