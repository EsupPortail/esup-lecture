var lecture = lecture || {};
lecture.init = function($, namespace, urlActionMarkRead, urlMarkRead, urlMarkAllRead, urlFiltrItem, initialItemDisplayMode) {
	// creation d'un object privé (par namespace)
	var priv= new Object();
	var portletId = namespace;
	var selector = '#lecture-'+namespace;
	lecture[namespace] = priv ;
	priv.urlMarkRead = urlMarkRead;
	priv.urlMarkAllRead = urlMarkAllRead;
	priv.urlActionMarkRead = urlActionMarkRead;
	priv.urlFilterItem = urlFiltrItem;
	priv.selector = selector;
	priv.namespace=namespace;
	priv.initialNotReadOnly = 'ALL' != initialItemDisplayMode;
	priv.jq = $;
	
	
	
	console.log("test lecture " + lecture[namespace].urlMarkRead +"   "+ lecture[namespace].urlMarkAllRead);
	
	//  (function($, namespace, namespace, urlMarkReadv, urlMarkAllRead,
 //     urlFiltrItem) {
    $(function() {
  //afficher & cacher la liste des catégories
      $("#" + portletId + "cacherListRubrique").change(
          function() {
            if ($(this).is(':checked')) {
              $("#" + portletId + "listOfCat").hide();
              $("#" + portletId + "divModeDesk").removeClass(
                  'col-sm-9');
              $("#" + portletId + "divModeDesk").addClass(
                  'col-sm-12');
            } else {
              $("#" + portletId + "listOfCat").show();
              $("#" + portletId + "divModeDesk").removeClass(
                  'col-sm-12');
              $("#" + portletId + "divModeDesk").addClass(
                  'col-sm-9');
            }
          });
      
      // pour griser les images des articles non lus
      $(".listeIdArtitrue").each(function() {
        var id = $(this).val();
        $('#' + id).addClass("griseImageArticle");
      });
      // pour fixer le header et la liste des rubriques
      var elm = $("#" + portletId + "fixHeadtrue");
      if (elm && $(elm).offset()) {
        var HeaderTop = $(elm).offset().top;
      }
      elm = $("#" + portletId + "menuRubrique");
      if (elm && $(elm).offset()) {
        var listCatTop = $(elm).offset().top;
      }
      var divScroll = $("#" + portletId + "divModeDesk").height();
      var menuRubriqueHeight = $("#" + portletId + "menuRubrique")
          .height();
      var divMenuScroll = divScroll - 2 * menuRubriqueHeight;
      $(window).scroll(
          function() {
            if (HeaderTop && $(window).scrollTop() > HeaderTop
                && $(window).scrollTop() < divScroll) {
              $("#" + portletId + "fixHeadtrue").css({
                position : 'fixed',
                top : '30px',
                display : 'block'
              });
              $("#" + portletId + "fixHeadtrue").addClass(
                  'classHeadLarg');
            } else {
              $("#" + portletId + "fixHeadtrue").css({
                position : 'static',
                top : '0px'
              });
              $("#" + portletId + "fixHeadtrue").removeClass(
                  'classHeadLarg');
            }
            if (listCatTop && $(window).scrollTop() > listCatTop
                && $(window).scrollTop() < divMenuScroll) {
              $("#" + portletId + "menuRubrique").css({
                position : 'fixed',
                top : '30px'
              });
            } else {
              $("#" + portletId + "menuRubrique").css({
                position : 'static',
                top : '30px'
              });
            }
          });
      //pour afficher la modale
      $("#lecture-" + portletId + " .actualite a.publisherReadMore")
          .each(
              function() {
                $(this)
                    .click(
                        function(e) {
                          e.preventDefault();
                          console
                              .log(
                                  "test",
                                  $(selector
                                      + " #"
                                      + portletId
                                      + "modalPublisher"));
                          $(selector
                                  + " #"
                                  + portletId
                                  + "modalPublisher")
                              .modal('show')
                              .find('.modal-body')
                              .load(
                                  $(this)
                                      .attr(
                                          'href'));
                        });
              });
    });

 

    function toggleArticleRead(idCat, idSrc, idItem, idDivRow, isModePublisher) {
    	var urlAjax = urlActionMarkRead;
    	var divRow = $('div#'+ idDivRow);
    	var divInput;
    	var divEye;
    	var readItem ;
    	console.log("toggleArticleRead :" + urlAjax);
    	if (divRow) {
    		
    		divInput=$('input.itemShowFilterIsRead', divRow);
    		readItem =  divInput.val() == 'true' ? false : true;
    		divInput.val(readItem);
    		
	    	$.ajax({
	    		url : urlAjax,
	    		type : 'POST',
	    		async : true,
	    		datatype:'json',
	    		data : {
	                'catId' : idCat,
	                'srcId' : idSrc,
	                'itemId' : idItem,
	                'isRead' : readItem,
	                'isPublisherMode' : isModePublisher
	              },
	            success : function(data){
	            	console.log ("mise a jour ok");
	            }
	    	});
	    	
	    	if (isModePublisher) {
	    		if (readItem) {
	    			$('div.articleEye i', divRow ).addClass('fa-eye-slash').removeClass('fa-eye');
	    			divRow.addClass('dejaLue');
	    		} else {
	    			$('div.articleEye i', divRow ).addClass('fa-eye').removeClass('fa-eye-slash');
	    			divRow.removeClass('dejaLue');
	    		}
	    	}
    	}
    }
    priv.toggleArticleRead = toggleArticleRead;
    
    
    //marquer un item LU
    function marquerItemLu(idCat, idSrc, idItem, readItem, isModePublisher) {
      var urlAjax = urlMarkRead;
      $
          .ajax({
            url : urlAjax,
            type : 'POST',
            data : {
              'p1' : idCat,
              'p2' : idSrc,
              'p3' : idItem,
              'p4' : !readItem,
              'p5' : isModePublisher
            },
            success : function(data) {
              var idSource = idSrc.split(' ').join('');
              idSource = idSource.split(':').join('');
              var ideye = 'eye' + idItem + idSource;
              var classEye = 'eye' + idItem;
              var idImg = 'contenuArtitrue' + idItem + idSource;
              if (readItem == true) {
                if (isModePublisher == true) {
                  $("." + classEye)
                      .each(
                          function() {
                            var id = $(this).val();
                            var idImgi = 'contenuArtitrue'
                                + id;
                            var ideyei = 'eye' + id;
                            $('#' + idImgi)
                                .removeClass(
                                    "griseImageArticle");
                            var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="lecture.'+namespace+'.marquerItemLu(\''
                                + idCat
                                + '\',\''
                                + idSrc
                                + '\',\''
                                + idItem
                                + '\','
                                + false
                                + ','
                                + isModePublisher
                                + ')"></i>';
                            $('#' + ideyei).html(
                                htmlCode);
                          });
                } else {
                  $('#' + idImg).removeClass(
                      "griseImageArticle");
                  var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="lecture.'+namespace+'.marquerItemLu(\''
                      + idCat
                      + '\',\''
                      + idSrc
                      + '\',\''
                      + idItem
                      + '\','
                      + false
                      + ','
                      + isModePublisher + ')"></i>';
                  $('#' + ideye).html(htmlCode);
                }
              } else {
                if (isModePublisher == true) {
                  $("." + classEye)
                      .each(
                          function() {
                            var id = $(this).val();
                            var idImgi = 'contenuArtitrue'
                                + id;
                            var ideyei = 'eye' + id;
                            $('#' + idImgi)
                                .addClass(
                                    "griseImageArticle");
                            var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="lecture.'+namespace+'.marquerItemLu(\''
                                + idCat
                                + '\',\''
                                + idSrc
                                + '\',\''
                                + idItem
                                + '\','
                                + true
                                + ','
                                + isModePublisher
                                + ')"></i>';
                            $('#' + ideyei).html(
                                htmlCode);
                          });
                } else {
                  $('#' + idImg)
                      .addClass("griseImageArticle");
                  var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="lecture.'+namespace+'.marquerItemLu(\''
                      + idCat
                      + '\',\''
                      + idSrc
                      + '\',\''
                      + idItem
                      + '\','
                      + true
                      + ','
                      + isModePublisher + ')"></i>';
                  $('#' + ideye).html(htmlCode);
                }
              }
            }
          });
    }
    

    priv.marquerItemLu = marquerItemLu;
    
    
    
    // //marquer tous les articles lu/non lu
    function marquerToutItemLu(isRead) {
      var urlAjax = urlMarkAllRead;
      var optionChoisit = $("#" + portletId + "listNonLu").val();
      var catSelectionne = $("#" + portletId + "catSeletc").val();
      var rubSelectionne = $("#" + portletId + "SrcSeletc").val();
      $.ajax({
        url : urlAjax,
        type : 'post',
        data : {
          'p1' : isRead,
          'p2' : catSelectionne,
          'p3' : rubSelectionne,
          'p4' : optionChoisit
        },
        success : function(reponse) {
          $("#" + portletId + "zoneArticles").html(reponse);
        },
      });
    }
    priv.marquerToutItemLu = marquerToutItemLu;
    
    
   
    
    // //filtrer les articles
    function filtrerNonLus(valFilter) {
      if (valFilter != '') {
        $("#" + portletId + "listNonLu").val(valFilter);
      }
      var optionChoisit = $("#" + portletId + "listNonLu").val();
      var catSelectionne = $("#" + portletId + "catSeletc").val();
      var nomRub = $("#" + portletId + "rubSeletc").val();
      var idrubSelectionne = $("#" + portletId + "SrcSeletc").val();
      var urlAjax = urlFiltrItem;
      $.ajax({
        url : urlAjax,
        type : 'post',
        data : {
          'p1' : catSelectionne,
          'p2' : idrubSelectionne,
          'nomSrc' : nomRub,
          'p3' : optionChoisit
        },
        success : function(reponse) {
          $("#" + portletId + "zoneArticles").html(reponse);
         $(".listeIdArtitrue").each(function() {
            var id = $(this).val();
           $('#' + id).addClass("griseImageArticle");
          });
        },
      });
    }
    priv.filtrerNonLus = filtrerNonLus;

    
    
    
    function filtrerParCategorie(catid) {
      $("#" + portletId + "catSeletc").val(catid);
      $("#" + portletId + "SrcSeletc").val('');
      $("#" + portletId + "rubSeletc").val('');
      filtrerNonLus('');
    }
    priv.filtrerParCategorie = filtrerParCategorie;

 
    function filtrerParRubrique(catid, srcid, afficherRubSelect, val) {
    	console.log("filtrerParRubrique");
      if (afficherRubSelect != '') {
        var reponse1 = "<label>" + afficherRubSelect + "</label>";
        var reponse = "<label>" + afficherRubSelect
            + "</label><span class='caret margeCarret'></span>";
        $("#" + portletId + "rubSelectedDiv1").html(reponse1);
        $("#" + portletId + "rubSelectedDiv2").html(reponse);
      }
      $("#" + portletId + "catSeletc").val(catid);
      $("#" + portletId + "SrcSeletc").val(srcid);
      $("#" + portletId + "rubSeletc").val(afficherRubSelect);
   //   filtrerNonLus('');
      if (val == 'show') {
        $("#mainmenurow" + portletId).show();
        $("#menupos1" + portletId).hide();
      }
    }
    priv.filtrerParRubrique = filtrerParRubrique;
 
    
    function filterByRubriqueClass(classRubrique){
    	var text="Toutes les actualités!";
    	var contexte = namespace;
    	$("div.rubriqueFiltre."+contexte).each(function(){
    			if ($(this).hasClass(classRubrique)) {
    				$(this).addClass('active');
    				text=$('input.srcName', this).val();
    			} else {
    				$(this).removeClass('active');
    			}
    	});
    	
    	
    	$("div.itemShowFilter."+contexte).each(function() {
    		if (classRubrique == 'rubrique_all' || $(this).hasClass(classRubrique)) {
    			$(this).removeClass('rubriqueInactive');
    		} else {
    			$(this).addClass('rubriqueInactive');
    		}
    	});
    	console.log("div#"+contexte+"rubSelectedDiv1 label.rubrique_Active: "+text);
    	$("label.rubrique_Active."+contexte).html(text);
    }
    priv.filterByRubriqueClass =  filterByRubriqueClass;
    
 
    
    function AfficherTout(val) {
      $("#" + portletId + "catSeletc").val('');
      $("#" + portletId + "SrcSeletc").val('');
      $("#" + portletId + "rubSeletc").val('');
      var reponse1 = "<label>Toutes les actualités</label>"
      var reponse = "<label>Toutes les actualités</label><span class='caret margeCarret'></span>";
      $("#" + portletId + "rubSelectedDiv1").html(reponse1);
      $("#" + portletId + "rubSelectedDiv2").html(reponse);
      filtrerNonLus('');
      if (val == 'show') {
        $("#mainmenurow" + portletId).show();
        $("#menupos1" + portletId).hide();
      }
    }
    priv.AfficherTout = AfficherTout;
    
 
 
    function filterPublisherNotRead(obj) {
    	var contextClass = namespace;
    	var url=urlFiltrItem;
    	if (obj) {
	    	var laDiv = $('div.divModeDesk div.panel.'+contextClass);
	    	if (laDiv) {
		    	if (obj.checked){
		    		laDiv.addClass('nonLueSeulement');
		    	} else {
		    		laDiv.removeClass('nonLueSeulement');
		    	}
		    	console.log("urlFiltrItem " + urlFiltrItem);
		    	
		    	$.ajax({
				    url: url,
				    type: 'POST',
				    data: {filter : obj.checked},
				    datatype:'json',
				    success: function(){
				    		if (!obj.checked && priv.initialNotReadOnly ) {
				    		//	location.reload();
				    		};
				    	}
		    	});	
		    	
		    }
    	}
    }
    priv.filterPublisherNotRead = filterPublisherNotRead;
    
    
 
/*    function filtrerPublisherNonLus() {
      if ($("#" + portletId + "checkBoxNonLu").is(':checked')) {
        $("#" + portletId + "listNonLu").val("val2")
        filtrerNonLus('');
      } else {
        $("#" + portletId + "listNonLu").val("val1")
        AfficherTout();
      }
    }

    window.filtrerPublisherNonLus = filtrerPublisherNonLus;
  */  
    
    function filtrerPublisherNonLusMobile() {
      if ($("#" + portletId + "checkBoxNonLu2").is(':checked')) {
        $("#" + portletId + "listNonLu").val("val2");
        filtrerNonLus('');
      } else {
        $("#" + portletId + "listNonLu").val("val1");
        AfficherTout();
      }
    }
    priv.filtrerPublisherNonLusMobile = filtrerPublisherNonLusMobile;
 
}
