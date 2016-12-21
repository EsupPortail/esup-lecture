<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<portlet:resourceURL id='toggleItemReadState' var="toggleItemReadState"></portlet:resourceURL>
<portlet:resourceURL id='toggleAllItemReadState'
	var="toggleAllItemReadState"></portlet:resourceURL>
<portlet:resourceURL id='FilteredItem' var="FilteredItem"></portlet:resourceURL>
<script type="text/javascript">
	$(function () {
	  $('[data-toggle="tooltip"]').tooltip()
	});
	//marquer un article lu ou non lu
	function <portlet:namespace />marquerItemLu(idCat, idSrc, idItem, readItem){
		var urlAjax = "<%=toggleItemReadState%>";
		$.ajax({
		       url : urlAjax,  
		       type : 'GET',
		       data : 'p1='+idCat+'&p2='+idSrc+'&p3='+idItem+'&p4='+!readItem,
		       success : function(data){ 
					var ideye ='eye'+idItem;
					if(readItem==true){
				 		var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="<portlet:namespace />marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+false+')"></i>';
				 		$('#'+ideye).html(htmlCode);	    
					}else{
						var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="<portlet:namespace />marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+true+')"></i>';
						$('#'+ideye).html(htmlCode);
					}
		       },
		    });
	}
	//marquer tous les articles lu/non lu
	function <portlet:namespace />marquerToutItemLu(isRead){
		var urlAjax = "<%=toggleAllItemReadState%>";
		var optionChoisit=$("#<portlet:namespace />listNonLu").val();
		var catSelectionne=$("#<portlet:namespace />catSeletc").val();
		var rubSelectionne=$("#<portlet:namespace />SrcSeletc").val();
		$.ajax({
		       url : urlAjax,  
		       type : 'GET',
		       data : 'p1='+isRead+'&p2='+catSelectionne+'&p3='+rubSelectionne+'&p4='+optionChoisit,
		       success:function(reponse){ 
		    		   $("#<portlet:namespace />zoneArticles").html(reponse);
		       },
		});
	}
	//afficher la liste des rubriques
	function <portlet:namespace />afficherListRubrique(){
		$("#<portlet:namespace />listOfCat").show();
	}
	
	//cacher la liste des rubriques
	function <portlet:namespace />cacherListRubrique(){
		$("#<portlet:namespace />listOfCat").hide();
	}
	
	//filtrer les articles
	function <portlet:namespace />filtrerNonLus(){
		var optionChoisit=$("#<portlet:namespace />listNonLu").val();
		var catSelectionne=$("#<portlet:namespace />catSeletc").val();
		var rubSelectionne=$("#<portlet:namespace />SrcSeletc").val();
		var urlAjax = "<%=FilteredItem%>";
		$.ajax({
		       url : urlAjax,  
		       type : 'GET',
		       data : 'p1='+catSelectionne+'&p2='+rubSelectionne+'&p3='+optionChoisit,
		       success:function(reponse){ 
		    		   $("#<portlet:namespace />zoneArticles").html(reponse);
		       },
		});
		
	}
	
	function <portlet:namespace />filtrerParCategorie(catid){
		$("#<portlet:namespace />catSeletc").val(catid);
		$("#<portlet:namespace />SrcSeletc").val('');
		<portlet:namespace />filtrerNonLus();
	}	
    function <portlet:namespace />filtrerParRubrique(catid,srcid){
    	$("#<portlet:namespace />catSeletc").val(catid);
    	$("#<portlet:namespace />SrcSeletc").val(srcid);
    	<portlet:namespace />filtrerNonLus();
	}
    function <portlet:namespace />AfficherTout(){
		$("#<portlet:namespace />catSeletc").val('');
		$("#<portlet:namespace />SrcSeletc").val('');
		<portlet:namespace />filtrerNonLus();
	}
 </script>
<div class="esup-lecture portlet-container"
	id="lecture-<portlet:namespace/>">
	<div class="container-fluid">
		<span class="spinner"></span>
		<div class='row'>
			<div class="col-sm-5" onclick="<portlet:namespace />AfficherTout()">
				<strong><c:out value="${contexte.name}"></c:out></strong>
			</div>
		</div>
		<div class="row">
			<input type="hidden" id="<portlet:namespace />catSeletc" value='' />
			<input type="hidden" id="<portlet:namespace />SrcSeletc" value='' />
			<c:if test="${contexte.treeVisible=='true'}">
				<nav class="col-sm-3" id='<portlet:namespace />listOfCat'>
					<ul class="nav nav-pills nav-stacked">
						<c:forEach items="${listCat}" var="cat">
							<li class="dropdown"><c:if test="${not empty cat.sources}">
									<div
										onclick="<portlet:namespace />filtrerParCategorie('${cat.id}')">
										<c:out value="${cat.name}"></c:out>
									</div>
									<div class="dropdown-toggle" data-toggle="dropdown">
										<span class="caret pull-right"></span>
									</div>
									<ul class="dropdown-menu">
										<c:forEach items="${cat.sources}" var="src">
											<li><div class="row"
													onclick="<portlet:namespace />filtrerParRubrique('${cat.id}','${src.id}')">
													<c:out value="${src.name}"></c:out>
													<span class="badge pull-right"><c:out
															value="${src.unreadItemsNumber}"></c:out></span>
												</div></li>
										</c:forEach>
									</ul>
								</c:if></li>
						</c:forEach>
					</ul>
				</nav>
			</c:if>
			<div class="col-md-9">
				<div class="row">
					<div class="panel panel-default">
						<div class="panel panel-heading">
							<div class="row">
								<div class="col-sm-1 col-md-offset-3">
									<i class="fa fa-eye fa-stack-1x"
										onclick="<portlet:namespace />marquerToutItemLu(false)"
										data-toggle="tooltip" data-placement="top"
										title="marquer tous comme non Lu"></i>
								</div>
								<div class="col-sm-1">
									<i class="fa fa-eye-slash fa-stack-1x"
										onclick="<portlet:namespace />marquerToutItemLu(true)"
										data-toggle="tooltip" data-placement="top"
										title="marquer tous comme lu"></i>
								</div>
								<c:if test="${contexte.treeVisible=='true'}">
									<div class="col-sm-1">
										<i class="fa fa-arrows-alt fa-stack-1x"
											onclick="<portlet:namespace />cacherListRubrique()"
											data-toggle="tooltip" data-placement="top"
											title="cacher la liste des rubriques"></i>
									</div>
									<div class="col-sm-1">
										<i class="fa fa-columns fa-stack-1x"
											onclick="<portlet:namespace />afficherListRubrique()"
											data-toggle="tooltip" data-placement="top"
											title="afficher la liste des rubriques"></i>
									</div>
								</c:if>
								<div class="col-sm-3">
									<select id="<portlet:namespace />listNonLu"
										onchange="<portlet:namespace />filtrerNonLus()">
										<option value="val1">Tous</option>
										<option value="val2">Non lus</option>
										<option value="val3">Non lus en premier</option>
									</select>
								</div>
							</div>
						</div>
						<div class="panel-body" id="<portlet:namespace />zoneArticles">
							<c:forEach items="${listCat}" var="cat">
								<c:forEach items="${cat.sources}" var="src">
									<c:forEach items="${src.items}" var="article">
										<div class='row'>
											<div class='col-md-11'>
												<c:out value="${article.htmlContent}" escapeXml="false"></c:out>
											</div>
											<div class='col-md-1 articleDiv' id="eye${article.id}">
												<c:if test="${article.read=='true'}">
													<i class="fa fa-eye-slash fa-stack-1x"
														onclick="<portlet:namespace />marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read})"></i>
												</c:if>
												<c:if test="${article.read=='false'}">
													<i class="fa fa-eye fa-stack-1x"
														onclick="<portlet:namespace />marquerItemLu('${cat.id}','${src.id}','${article.id}',${article.read})"></i>
												</c:if>
											</div>
										</div>
									</c:forEach>
								</c:forEach>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
