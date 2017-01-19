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
<rs:aggregatedResources path="/resourcesLecture.xml" />
<%-- <rs:resourceURL value=""/> --%>
<%-- <rs:aggregatedResources path="/resources-footer.xml" /> --%>
<portlet:resourceURL id='toggleItemReadState' var="toggleItemReadState"></portlet:resourceURL>
<portlet:resourceURL id='toggleAllItemReadState'
	var="toggleAllItemReadState"></portlet:resourceURL>
<portlet:resourceURL id='FilteredItem' var="FilteredItem"></portlet:resourceURL>
<script type="text/javascript">
// 	var dom = {};
// 	dom.query = jQuery.noConflict( true );
(function($) {
 	$(function() {
	  $('[data-toggle="tooltip"]').tooltip()
	  $("#<portlet:namespace />afficherListRubrique").click(function(){
			$("#<portlet:namespace />listOfCat").show();
			$("#<portlet:namespace />divModeDesk").removeClass('col-sm-12');
			$("#<portlet:namespace />divModeDesk").addClass('col-sm-9');
		});
	  $("#<portlet:namespace />cacherListRubrique").click(function(){
			$("#<portlet:namespace />listOfCat").hide();
			$("#<portlet:namespace />divModeDesk").removeClass('col-sm-9');
			$("#<portlet:namespace />divModeDesk").addClass('col-sm-12');
		});
	  if( $("#<portlet:namespace />treeVisible").val()=="false"){
		  $("#<portlet:namespace />divModeDesk").removeClass('col-sm-9');
		  $("#<portlet:namespace />divModeDesk").addClass('col-sm-12');
	  }
	  //pour griser les images des articles non lus 
	  $(".listeIdArtitrue").each(function(){ 
		  var id = $(this).val(); 
		  $('#'+id).addClass("griseImageArticle");	 
	  });
	});
	//marquer un article lu ou non lu

	 function <portlet:namespace />marquerItemLu(idCat, idSrc, idItem, readItem,isModePublisher){
		var urlAjax = "<%=toggleItemReadState%>";
		$.ajax({
		       url : urlAjax,  
		       type : 'GET',
		       data : 'p1='+idCat+'&p2='+idSrc+'&p3='+idItem+'&p4='+!readItem+'&p5='+isModePublisher,
		       success : function(data){ 
		   			var idSource = idSrc.split(' ').join('');
		   			idSource =idSource.split(':').join('');
					var ideye ='eye'+idItem+idSource;
					var classEye = 'eye'+idItem;
					var idImg = 'contenuArtitrue'+idItem+idSource;
					if(readItem==true){
 				 		if(isModePublisher==true){
 				 			$("."+classEye).each(function(){ 
 				 				var id = $(this).val(); 
 				 				var idImgi = 'contenuArtitrue'+id;
 				 				var ideyei ='eye'+id;
 				 				$('#'+idImgi).removeClass("griseImageArticle");
						 		var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="<portlet:namespace />marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+false+','+isModePublisher+')"></i>';
						 		$('#'+ideyei).html(htmlCode);
 				 			});
				 		}else{
				 			$('#'+idImg).removeClass("griseImageArticle");
					 		var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="<portlet:namespace />marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+false+','+isModePublisher+')"></i>';
					 		$('#'+ideye).html(htmlCode);
			 			}			 		
					}else{
 						if(isModePublisher==true){
 							$("."+classEye).each(function(){ 
 				 				var id = $(this).val(); 
 				 				var idImgi = 'contenuArtitrue'+id;
 				 				var ideyei ='eye'+id;
 				 				$('#'+idImgi).addClass("griseImageArticle");	
						 		var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="<portlet:namespace />marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+true+','+isModePublisher+')"></i>';
						 		$('#'+ideyei).html(htmlCode);
 				 			});
				 		}else{
				 			$('#'+idImg).addClass("griseImageArticle");	
							var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="<portlet:namespace />marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+true+','+isModePublisher+')"></i>';
							$('#'+ideye).html(htmlCode);							
				 		}			
					}
		       }
		    });
	}
	 window.<portlet:namespace />marquerItemLu=<portlet:namespace />marquerItemLu;
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
	window.<portlet:namespace />marquerToutItemLu=<portlet:namespace />marquerToutItemLu;
	//filtrer les articles
	function <portlet:namespace />filtrerNonLus(){
		var optionChoisit=$("#<portlet:namespace />listNonLu").val();
		var catSelectionne=$("#<portlet:namespace />catSeletc").val();
		var rubSelectionne=$("#<portlet:namespace />SrcSeletc").val();
		var urlAjax = "<%=FilteredItem%>";
			$.ajax({
				url : urlAjax,
				type : 'GET',
				data : 'p1=' + catSelectionne + '&p2=' + rubSelectionne
						+ '&p3=' + optionChoisit,
				success : function(reponse) {
					$("#<portlet:namespace />zoneArticles").html(reponse);
					$(".listeIdArtitrue").each(function() {
						var id = $(this).val();
						$('#' + id).addClass("griseImageArticle");
					});
				},
			});

		}
		window.<portlet:namespace />filtrerNonLus = <portlet:namespace />filtrerNonLus;

		function <portlet:namespace />filtrerParCategorie(catid) {
			$("#<portlet:namespace />catSeletc").val(catid);
			$("#<portlet:namespace />SrcSeletc").val('');
			<portlet:namespace />filtrerNonLus();
		}

		window.<portlet:namespace />filtrerParCategorie = <portlet:namespace />filtrerParCategorie;
		function <portlet:namespace />filtrerParRubrique(catid, srcid,
				afficherRubSelect) {
			if (afficherRubSelect != '') {
				var reponse = "<label>" + afficherRubSelect + "</label>";
				$("#<portlet:namespace />rubSelectedDiv").html(reponse);
			}
			$("#<portlet:namespace />catSeletc").val(catid);
			$("#<portlet:namespace />SrcSeletc").val(srcid);
			<portlet:namespace />filtrerNonLus();
		}
		window.<portlet:namespace />filtrerParRubrique = <portlet:namespace />filtrerParRubrique;

		function <portlet:namespace />AfficherTout() {
			$("#<portlet:namespace />catSeletc").val('');
			$("#<portlet:namespace />SrcSeletc").val('');
			var reponse = "<label>Toutes les actualités</label>";
			$("#<portlet:namespace />rubSelectedDiv").html(reponse);
			<portlet:namespace />filtrerNonLus();
		}
		window.<portlet:namespace />AfficherTout = <portlet:namespace />AfficherTout;
		function <portlet:namespace />filtrerPublisherNonLus() {
			if ($("#<portlet:namespace />checkBoxNonLu").is(':checked')) {
				$("#<portlet:namespace />listNonLu").val("val2")
				<portlet:namespace />filtrerNonLus();
			} else {
				$("#<portlet:namespace />listNonLu").val("val1")
				<portlet:namespace />AfficherTout();
			}
		}
		window.<portlet:namespace />filtrerPublisherNonLus = <portlet:namespace />filtrerPublisherNonLus;
	})(up.jQuery, window);
</script>
<div class="esup-lecture portlet-container"
	id="lecture-<portlet:namespace/>">
	<div class="container-fluid">
		<span class="spinner"></span>
		<c:if
			test="${contexte.viewDef=='true'&& contexte.modePublisher=='true'}">
			<%@include file="homeAccueil.jsp"%>
		</c:if>
		<c:if
			test="${contexte.viewDef=='false'||contexte.modePublisher=='false'}">
			<%@include file="homeAutre.jsp"%>
		</c:if>
	</div>
</div>
