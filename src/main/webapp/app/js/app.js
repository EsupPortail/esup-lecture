var lecture = lecture || {};
lecture.init = function($, namespace, portletId,urlMarkRead, urlMarkAllRead, urlFiltrItem) {
	(function($, namespace, portletId,urlMarkReadv, urlMarkAllRead, urlFiltrItem) {
		$(function() {
			$('[data-toggle="tooltip"]').tooltip()
//			$("#" + portletId + "afficherListRubrique").click(function() {
//				$("#" + portletId + "listOfCat").show();
//				$("#" + portletId + "divModeDesk").removeClass('col-sm-12');
//				$("#" + portletId + "divModeDesk").addClass('col-sm-9');
//			});
			$("#" + portletId + "cacherListRubrique").change(function(){
				if($(this).is(':checked')){
					$("#" + portletId + "listOfCat").hide();
					$("#" + portletId + "divModeDesk").removeClass('col-sm-9');
					$("#" + portletId + "divModeDesk").addClass('col-sm-12');
				}else{
					$("#" + portletId + "listOfCat").show();
					$("#" + portletId + "divModeDesk").removeClass('col-sm-12');
					$("#" + portletId + "divModeDesk").addClass('col-sm-9');	
				}
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
			//pour fixer le header et la liste des rubriques
			var HeaderTop = $("#" + portletId + "fixHeadtrue").offset().top;
			var listCatTop =$("#" + portletId + "menuRubrique").offset().top;
			var divScroll  = $("#" + portletId + "divModeDesk").height();
			var menuRubriqueHeight = $("#" + portletId + "menuRubrique").height();
			var divMenuScroll= divScroll-2*menuRubriqueHeight;
			$(window).scroll(function(){
	            if( $(window).scrollTop() > HeaderTop && $(window).scrollTop()<divScroll) {
	                    $("#" + portletId + "fixHeadtrue").css({position: 'fixed', top: '30px', display: 'block'});
	                    $("#" + portletId + "fixHeadtrue").addClass('classHeadLarg');
	            } else {
	                    $("#" + portletId + "fixHeadtrue").css({position: 'static', top: '0px'});
	                    $("#" + portletId + "fixHeadtrue").removeClass('classHeadLarg');
	            }
	            if($(window).scrollTop() > listCatTop && $(window).scrollTop()<divMenuScroll) {
	            	$("#" + portletId + "menuRubrique").css({position: 'fixed', top: '30px'});
	            }else{
	            	$("#" + portletId + "menuRubrique").css({position: 'static', top: '30px'});
	            }
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
	 	function filtrerNonLus(valFilter){
	 		if(valFilter!=''){
	 			$("#" + portletId + "listNonLu").val(valFilter);
	 		}
	 		var optionChoisit=$("#" + portletId + "listNonLu").val();
	 		var catSelectionne=$("#" + portletId + "catSeletc").val();
	 		var nomRub =$("#" + portletId + "rubSeletc").val();
	 		var idrubSelectionne=$("#" + portletId + "SrcSeletc").val();
	 		var urlAjax = urlFiltrItem;
	 			$.ajax({
	 				url : urlAjax,
	 				type : 'GET',
	 				data : 'p1=' + catSelectionne + '&p2=' + idrubSelectionne+'&nomSrc='+nomRub
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
	 			$("#" + portletId + "rubSeletc").val('');
	 			filtrerNonLus('');
	 		}
	 		window.filtrerParCategorie = filtrerParCategorie;
	 		
	 		function filtrerParRubrique(catid, srcid,
	 				afficherRubSelect,val) {
	 			if (afficherRubSelect != '') {
	 				var reponse = "<label>" + afficherRubSelect + "</label>";
	 				$("#" + portletId + "rubSelectedDiv1").html(reponse);
	 				$("#" + portletId + "rubSelectedDiv2").html(reponse);
	 			}
	 			$("#" + portletId + "catSeletc").val(catid);
	 			$("#" + portletId + "SrcSeletc").val(srcid);
	 			$("#" + portletId + "rubSeletc").val(afficherRubSelect);
	 			filtrerNonLus('');
	 			if(val=='show'){
	 				$("#mainmenurow"+portletId).show();
 					$("#menupos1"+portletId).hide();
 				}
	 		}
	 		window.filtrerParRubrique = filtrerParRubrique;

	 		function AfficherTout(val) {
	 			$("#" + portletId + "catSeletc").val('');
	 			$("#" + portletId + "SrcSeletc").val('');
	 			$("#" + portletId + "rubSeletc").val('');
	 			var reponse = "<label>Toutes les actualités</label>";
	 			$("#" + portletId + "rubSelectedDiv1").html(reponse);
	 			$("#" + portletId + "rubSelectedDiv2").html(reponse);
	 			filtrerNonLus('');
	 			if(val=='show'){
	 				$("#mainmenurow"+portletId).show();
 					$("#menupos1"+portletId).hide();
 				}
	 		}
	 		window.AfficherTout = AfficherTout;
	 		
	 		function filtrerPublisherNonLus() {
	 			if ($("#" + portletId + "checkBoxNonLu").is(':checked')) {
	 				$("#" + portletId + "listNonLu").val("val2")
	 				filtrerNonLus('');
	 			} else {
	 				$("#" + portletId + "listNonLu").val("val1")
	 				AfficherTout();
	 			}
	 		}
	 		window.filtrerPublisherNonLus = filtrerPublisherNonLus;
	 		function filtrerPublisherNonLusMobile() {
	 			if ($("#" + portletId + "checkBoxNonLu2").is(':checked')) {
	 				$("#" + portletId + "listNonLu").val("val2")
	 				filtrerNonLus('');
	 			} else {
	 				$("#" + portletId + "listNonLu").val("val1")
	 				AfficherTout();
	 			}
	 		}
	 		window.filtrerPublisherNonLusMobile = filtrerPublisherNonLusMobile;
	})($, namespace, portletId,urlMarkRead, urlMarkAllRead, urlFiltrItem);
}
