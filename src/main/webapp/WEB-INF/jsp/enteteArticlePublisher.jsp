
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<c:set var="treeVisible"><c:if test="${contexte.treeVisible=='false'}">noTreeVisible</c:if></c:set>


<div class=" enteteLectureAll ${treeVisible}">
<!--   "col-xs-7 col-sm-7" -->
  <div class="pull-left noMenuDropDown" id="${n}rubSelectedDiv1">
    <label class="rubrique_Active ${n}"><c:out value="${ctxTextFilter}"></c:out></label>
  </div>
  <div id="${n}rubSelectedDiv2" class="pull-left withMenuDropDown" data-toggle="modal"
      data-target="#modalRubriqueList${n}">
      <label class="rubrique_Active ${n}"><c:out value="${ctxTextFilter}"></c:out></label>
      <span class="caret"></span>
   </div>
	<c:if test="${contexte.userCanMarkRead=='true' && affichereye=='true'}">
		<div class="dropdown readNotRead ">

			<button  class="btn btn-primary dropdown-toggle pull-right" type="button" data-toggle="dropdown">
				<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
			</button>
			 <portlet:actionURL var="submitFormURL">
			  <portlet:param name="action" value="filteredItem"/>
			</portlet:actionURL>
			<ul class="dropdown-menu pull-right">
			  <li class="checkbox afficherLuWith">

					<label for="checkReadItem${n})" onclick="lecture['${n}'].jq('input#checkReadItem${n}').click()"> <c:out value="${showUnreadNews}"></c:out>&nbsp;
					</label>
					<input 	id = "checkReadItem${n}"
							class="checkReadItem"
							type="checkbox"
							${contexte.itemDisplayMode=='UNREAD'? 'checked' : ''}
							onchange="lecture.${n}.filterPublisherNotRead(this); lecture['${n}'].jq('#${n}rubSelectedDiv1 ').click();"
					  id="${n}checkBoxNonLu">

			  </li>
			</ul>
		</div>
	</c:if>
</div>

<c:set var="nbCat"  value="0" />
<c:set var="nbSrc" value="0" />

<div id="modalRubriqueList${n}" class="modal fade ${treeVisible}" role="dialog">
  <div class="modal-dialog modalMarge">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-body modalPadding">
        <a href="#" class="list-group-item" onclick="lecture.${n}.filterByRubriqueClass('rubrique_all')"
          data-dismiss="modal"><c:out value="${ctxTextFilter}"></c:out>
			<c:if test="${contexte.userCanMarkRead=='true' && affichereye=='true'}" >
          <span class="badge pull-right"><c:out
				  value="${nombreArticleNonLu}"></c:out></span>
			</c:if>
          </a>
        <c:forEach items="${listCat}" var="cat">
        <c:set var="nbCat" value="${nbCat+1}" />
        	<c:if var="notFromPublisher" test="${!cat.fromPublisher}" >
        	<!-- ******** -->
     			<c:if test="${not empty cat.sources}">
     			<div class="withCategoriesAndSources">
        		<div class="filtreInDropDown" >
        		<div  class="list-group-item rubriqueFiltre  ${n} cat_${nbCat}"
	              		onclick="lecture.${n}.filterByRubriqueClass('cat_${nbCat}')"
	              		data-dismiss="modal">
	              		<c:out value="${cat.name}"></c:out>
	         	</div>

	            <div id="divCollapseInDropDown${nbCat}${n}"
	            		class="collapseInDropDown"
	            		data-toggle="collapse"
						data-target="#ulInDropDown${nbCat}${n}"
						aria-expanded="true"
						aria-controls="ulInDropDown${nbCat}${n}">
							<span class="glyphicon glyphicon-triangle-bottom "></span>
							<span class="glyphicon glyphicon-triangle-right "></span>
				</div>
				</div>
        		<ul id="ulInDropDown${nbCat}${n}" class="collapse in"
        			 aria-expanded="true"
					aria-labelledby="divCollapseInDropDown${nbCat}${n}">
	          <c:forEach items="${cat.sources}" var="src">
	          	<c:set var="nbSrc" value="${nbSrc+1}" />
                  <li><div class="list-group-item rubriqueFiltre ${n} src_${nbSrc}"
	             		onclick="lecture.${n}.filterByRubriqueClass('src_${nbSrc}')"
	            		data-dismiss="modal" >
					<c:out value="${src.name}"></c:out>
					<span class="badge pull-right ${(contexte.userCanMarkRead=='true' && cat.userCanMarkRead=='true') ? '' : 'emptyTextCircle'}" style="background-color:${src.color}">
						<c:if test="${contexte.userCanMarkRead=='true' && cat.userCanMarkRead=='true'}">
	              			<c:out value="${src.unreadItemsNumber}"></c:out>
						</c:if>
                    </span>
	            </div></li>
	          </c:forEach>

	            </ul>
	            </div>
	          <!-- ******** -->
          </c:if>
     		</c:if>
     		<c:if test="${!notFromPublisher}" >
	          <c:forEach items="${cat.sources}" var="src">
                  <c:if test="${!(src.hiddenIfEmpty && src.itemsNumber == 0)}" >
                      <c:set var="nbSrc" value="${nbSrc+1}" />
                        <a href="#" class="list-group-item"
                          onclick="lecture.${n}.filterByRubriqueClass('rubrique_${src.uid}')"
                          data-dismiss="modal"><c:out value="${src.name}"></c:out>
                            <span class="badge pull-right ${(contexte.userCanMarkRead=='true' && cat.userCanMarkRead=='true') ? '' : 'emptyTextCircle'}" style="background-color:${src.color}">
                                <c:if test="${contexte.userCanMarkRead=='true' && cat.userCanMarkRead=='true'}" ><c:out
                              value="${src.unreadItemsNumber}"></c:out></c:if></span></a>
                  </c:if>
	          </c:forEach>
          	</c:if>
        </c:forEach>
      </div>
    </div>
  </div>
</div>
