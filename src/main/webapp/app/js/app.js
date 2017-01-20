var lecture = lecture || {};
lecture.init = function($, namespace, portletId,urlMarkRead, urlMarkAllRead, urlFiltrItem) {
	(function($, namespace, portletId,urlMarkReadv, urlMarkAllRead, urlFiltrItem) {
		$(function() {
			$('[data-toggle="tooltip"]').tooltip()
			$("#" + portletId + "afficherListRubrique").click(function() {
				$("#" + portletId + "listOfCat").show();
				$("#" + portletId + "divModeDesk").removeClass('col-sm-12');
				$("#" + portletId + "divModeDesk").addClass('col-sm-9');
			});
			$("#" + portletId + "cacherListRubrique").click(function() {
				$("#" + portletId + "listOfCat").hide();
				$("#" + portletId + "divModeDesk").removeClass('col-sm-9');
				$("#" + portletId + "divModeDesk").addClass('col-sm-12');
			});
			if ($("#" + portletId + "treeVisible").val() == "false") {
				$("#" + portletId + "divModeDesk").removeClass('col-sm-9');
				$("#" + portletId + "divModeDesk").addClass('col-sm-12');
			}
			// pour griser les images des articles non lus
			$(".listeIdArtitrue").each(function() {
				var id = $(this).val();
				$('#' + id).addClass("griseImageArticle");
			});
		});
	 	 function marquerItemLu(idCat, idSrc, idItem, readItem,isModePublisher){
		 		var urlAjax = urlMarkRead; 
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
		 						 		var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+false+','+isModePublisher+')"></i>';
		 						 		$('#'+ideyei).html(htmlCode);
		  				 			});
		 				 		}else{
		 				 			$('#'+idImg).removeClass("griseImageArticle");
		 					 		var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+false+','+isModePublisher+')"></i>';
		 					 		$('#'+ideye).html(htmlCode);
		 			 			}			 		
		 					}else{
		  						if(isModePublisher==true){
		  							$("."+classEye).each(function(){ 
		  				 				var id = $(this).val(); 
		  				 				var idImgi = 'contenuArtitrue'+id;
		  				 				var ideyei ='eye'+id;
		  				 				$('#'+idImgi).addClass("griseImageArticle");	
		 						 		var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+true+','+isModePublisher+')"></i>';
		 						 		$('#'+ideyei).html(htmlCode);
		  				 			});
		 				 		}else{
		 				 			$('#'+idImg).addClass("griseImageArticle");	
		 							var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="marquerItemLu(\''+idCat+'\',\''+idSrc+'\',\''+idItem+'\','+true+','+isModePublisher+')"></i>';
		 							$('#'+ideye).html(htmlCode);							
		 				 		}			
		 					}
		 		       }
		 		    });
		 	}
	 	window.marquerItemLu=marquerItemLu;
//  	//marquer tous les articles lu/non lu
	 	function marquerToutItemLu(isRead){
	 		var urlAjax = urlMarkAllRead;
	 		var optionChoisit=$("#" + portletId + "listNonLu").val();
	 		var catSelectionne=$("#" + portletId + "catSeletc").val();
	 		var rubSelectionne=$("#" + portletId + "SrcSeletc").val();
	 		$.ajax({
	 		       url : urlAjax,  
	 		       type : 'GET',
	 		       data : 'p1='+isRead+'&p2='+catSelectionne+'&p3='+rubSelectionne+'&p4='+optionChoisit,
	 		       success:function(reponse){ 
	 		    	   $("#" + portletId + "zoneArticles").html(reponse);
	 		       },
	 		});
	 	}
	 	window.marquerToutItemLu=marquerToutItemLu;
//	 	//filtrer les articles
	 	function filtrerNonLus(){
	 		var optionChoisit=$("#" + portletId + "listNonLu").val();
	 		var catSelectionne=$("#" + portletId + "catSeletc").val();
	 		var rubSelectionne=$("#" + portletId + "SrcSeletc").val();
	 		var urlAjax = urlFiltrItem;
	 			$.ajax({
	 				url : urlAjax,
	 				type : 'GET',
	 				data : 'p1=' + catSelectionne + '&p2=' + rubSelectionne
	 						+ '&p3=' + optionChoisit,
	 				success : function(reponse) {
	 					$("#" + portletId + "zoneArticles").html(reponse);
	 					$(".listeIdArtitrue").each(function() {
	 						var id = $(this).val();
	 						$('#' + id).addClass("griseImageArticle");
	 					});
	 				},
	 			});
	 		}
	 		window.filtrerNonLus = filtrerNonLus;

	 		function filtrerParCategorie(catid) {
	 			$("#" + portletId + "catSeletc").val(catid);
	 			$("#" + portletId + "SrcSeletc").val('');
	 			filtrerNonLus();
	 		}
	 		window.filtrerParCategorie = filtrerParCategorie;
	 		
	 		function filtrerParRubrique(catid, srcid,
	 				afficherRubSelect) {
	 			if (afficherRubSelect != '') {
	 				var reponse = "<label>" + afficherRubSelect + "</label>";
	 				$("#" + portletId + "rubSelectedDiv").html(reponse);
	 			}
	 			$("#" + portletId + "catSeletc").val(catid);
	 			$("#" + portletId + "SrcSeletc").val(srcid);
	 			filtrerNonLus();
	 		}
	 		window.filtrerParRubrique = filtrerParRubrique;

	 		function AfficherTout() {
	 			$("#" + portletId + "catSeletc").val('');
	 			$("#" + portletId + "SrcSeletc").val('');
	 			var reponse = "<label>Toutes les actualités</label>";
	 			$("#" + portletId + "rubSelectedDiv").html(reponse);
	 			filtrerNonLus();
	 		}
	 		window.AfficherTout = AfficherTout;
	 		
	 		function filtrerPublisherNonLus() {
	 			if ($("#" + portletId + "checkBoxNonLu").is(':checked')) {
	 				$("#" + portletId + "listNonLu").val("val2")
	 				filtrerNonLus();
	 			} else {
	 				$("#" + portletId + "listNonLu").val("val1")
	 				AfficherTout();
	 			}
	 		}
	 		window.filtrerPublisherNonLus = filtrerPublisherNonLus;
	})($, namespace, portletId,urlMarkRead, urlMarkAllRead, urlFiltrItem);
}
