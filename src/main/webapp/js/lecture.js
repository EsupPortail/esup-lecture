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
	
	
	
	
	//console.log("test lecture " + lecture[namespace].urlMarkRead +"   "+ lecture[namespace].urlMarkAllRead);
	
	
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
      
      var container = $('div.esup-lecture.portlet-container.'+namespace);
      if (container) {
	      var menu = $('ul.menuRubrique, nav.affixTree', container);
	      
	      if (menu) {
		      container = $('div.scrollDivArticle', container);
		      
		      var menuObj = menu[0];
		      if (menuObj) {
			      menuObj.mempos = menu.offset().top;
			      priv.gereAffixMenu = function(){
			    	  var limite = container.offset().top + container.height();
			    	  var bas = menuObj.mempos + $(window).scrollTop() + menu.height();
			    	  var displayHeight = $(parent).height() - menuObj.mempos;
			    	
			    	  if ((limite  < bas) || (displayHeight < menu.height())) {
				    		$(menu).removeClass('affix');
			    	  } else {
			    		  $(menu).addClass('affix');
			    	  }
			      };
			      
			      $(window).scroll(function () {
			    	  priv.gereAffixMenu();
			   
			      });
			      priv.gereAffixMenu();
		      }
	      }
      }
  
      var modalPublisherSelector = selector + " #"+ portletId + "modalPublisher";
      
      //pour afficher la modal
     $("#lecture-" + portletId + " .actualite ")
          .each(function(){onClickReadMoreInModal(modalPublisherSelector, 
          							this, "a.publisherReadMore", $('.iframeCacher.'+ namespace) )});
     $("#lecture-" + portletId + " .modeNoPublisher.contenuArticle ")
     .each(function(){onClickReadMoreInModal(modalPublisherSelector, 
     							this, "a.readMore").hide()}); 
    });


    function onClickReadMoreInModal(modalSelector, cibleOnClick, selectorHref, iframe){
    	// modalSelector : selector jquery de la modal
    	// cibleOnClick object sur lequel on place le onclick 
    	// selectorHref selecteur de l'object contenant le lien href de chargement de la modal. null si doit etre egale a cibleOnClick 
    	// iframe : peut etre null ; iframe cacher de prechargement en cas de cross domain avec l'autentification CAS (pour publisher)
    	// renvoie l'ancre : object selectioné par selectorHref
    	var ancre;
    	var ref;// = ancre ? $(ancre).attr('href') : $(cibleOnClick).attr('href');
    	if (selectorHref) {
    		ancre = $(selectorHref, cibleOnClick)[0];
    	} else {
    		ancre = cibleOnClick;
    	}
    	
    	if (ancre) {
    		var ref = $(ancre).attr('href') ;
    		if (ref) {
		    	$(cibleOnClick).click(function(e) {
		            e.preventDefault();
		            var modal = $(modalSelector);
		            if (!modal.traitementScroll) {
		            	// gestion de la scroll principale
			            modal.on('show.bs.modal', function (e) {
			            	$('html').css('overflow-y', 'hidden');
			            });
			            modal.on('hidden.bs.modal', function (e) {
			            	$('html').css('overflow-y', 'auto');
			            });
			            modal.traitementScroll = true;
		            }
		            modal.modal('show');
		            var modalBody= modal.find('.modal-body');
		            	
		            if (iframe) { // on utilise une iframe pour resoudre les pb de cross domaine cas avec publisher
			            modalBody.load(
			            		ref, 
			            		function(responseTxt, statusTxt, xhr) {
			            				// si le chargement est en error (pb cross domain) 
				                	if (statusTxt == 'error') {
				                			// on fait le chargement dans une iframe caché (plus de pb crossdomaine)
				                		var fr = $(iframe);
				                		fr.on('load', 
				                				function(){
				                						// quand iframe est chargé on recharge dans la modal
				                						// et c'est ok car le pb de crossdomain ne sont qu'a l'autentificaton
				                					modalBody.load(ref, 
				                									function(){
				                											// on vire les on(load) posé pour éviter l'accumulation
				                										fr.unbind('load');
				                									});
				                				})
				                		fr.attr("src" ,ref);
				                	}   	
		                });
		            } else {
		            	modalBody.load(ref);
		            }
		        });
		    	$(cibleOnClick).css('cursor', 'pointer');
    		}
    	}
    	return $(ancre);
    };

    
   

    function toggleArticleRead(idCat, idSrc, idItem, idDivRow, isModePublisher) {
    	var urlAjax = urlActionMarkRead;
    	var divRow = $('div#'+ idDivRow);
    	var divInput;
    	var divEye;
    	var readItem ;
 //   	console.log("toggleArticleRead :" + urlAjax);
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
	      //      	console.log ("mise a jour ok");
	            	// on doit décrementer les rubriques/src qui ont été modifieées ou l'inverse
	            		// table des rubriques (sources) dont les nb de non lue est modfifié. 
	            	var tabSrc = data.srcsIds;
	            	if (tabSrc) {
	            			// pour le compteur total on ajoute une pseudo rubrique
	            		tabSrc.push('all'); 
	            		var prefixSelector = 'div.' + portletId + " > span.badge > span[data-idsrc='";
	            		var sufixSelector = "']";
		            	for (i = 0; i < tabSrc.length; i++) {
		            		var idSrc = tabSrc[i];
		            		$(prefixSelector + idSrc + sufixSelector).each(
		            				function(){
			            			//	console.log("		dataIdsrc  OK " );
				            			var cpt = $(this).html();
				            			$(this).text(readItem ? --cpt : ++cpt);
				            		});	            		
		            	}
	            	}
	            }
	    	});
	    	
	    	if (isModePublisher || true) {
	    		if (readItem) {
	    			$('div.articleEye i', divRow ).addClass('fa-eye-slash').removeClass('fa-eye');
	    			divRow.addClass('dejaLue');
	    		} else {
	    			$('div.articleEye i', divRow ).addClass('fa-eye').removeClass('fa-eye-slash');
	    			divRow.removeClass('dejaLue');
	    		}
	    	}
    	}
    	markFirtVisible();
    	if (priv.gereAffixMenu) {
    		priv.gereAffixMenu() 
    	};
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
  //  	console.log("filtrerParRubrique");
      if (afficherRubSelect != '') {
        var reponse1 = "<label>" + afficherRubSelect + "</label>";
        var reponse = "<label>" + afficherRubSelect
            + "</label><span class='caret'></span>";
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
 
    /** Used in listeRubZonePublisher.jsp */
    function filterByRubriqueClass(classRubrique, notRubriqueObj){
    	var text=$("div.rubriqueFiltre.rubrique_all" + contexte + " input.titleName").val();
    	var contexte = namespace;
    	
    	$("div.rubriqueFiltre."+contexte).each(function(){
	    			if ($(this).hasClass(classRubrique)) {
	    				$(this).addClass('active');
	    				var aux=$('input.titleName', this).val();
	    				if (aux) {
	    					text = aux;
	    				}
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
    //	console.log("div#"+contexte+"rubSelectedDiv1 label.rubrique_Active: "+text);
    	$("label.rubrique_Active."+contexte).html(text);
    	markFirtVisible();
    	if (priv.gereAffixMenu) {priv.gereAffixMenu(); }
    }
    priv.filterByRubriqueClass =  filterByRubriqueClass;
 
    
 /**
  * affichage des non lue seulement ssi l'object passé est checked.
  * On memorise l'état coté serveur et client
  */
    function filterPublisherNotRead(obj) {
    	var contextClass = namespace;
    	var url=urlFiltrItem;
    	if (obj) {
	    	var laDiv = $('div.divModeDesk div.panel.'+contextClass);
	    	if (laDiv) {
		    	if (obj.checked){
		    		laDiv.addClass('nonLueSeulement');
		    		priv.notReadOnly = true;
		    	} else {
		    		laDiv.removeClass('nonLueSeulement');
		    		priv.notReadOnly = false;
		    	}
	//	    	console.log("urlFiltrItem " + urlFiltrItem);
		    	
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
	    	markFirtVisible();
	    	if (priv.gereAffixMenu) {priv.gereAffixMenu() };
    	}
    }
    priv.filterPublisherNotRead = filterPublisherNotRead;
    
    function markFirtVisible (){
    	var divNonLue = $('div.divModeDesk div.panel.nonLueSeulement.'+namespace)[0];
    	var first=true;
    	var lue = true;
    	if (divNonLue) {
    		
    		lue = false;
    		
    	} 
    //	console.log("markFirtVisible " + lue);
    	$('div.itemShowFilter.'+namespace).each(function() {
   // 		console.log($(this).attr('class'));
    		if (first && (lue ||!$(this).hasClass('dejaLue') ) && ! $(this).hasClass('rubriqueInactive')) {
    			$(this).addClass('premierVisible');
    			first = false;
    //			console.log("  marck pv");
    		} else {
    			$(this).removeClass('premierVisible');
    		}
    	});
    }

    function resetMinHeightPortlet() {
    	var divPortlet = $('.esup-lecture.portlet-container.'+ namespace);
    	var divAffix = $('.esup-lecture.'+ namespace + ' nav.navModeDesk.navClass.affix.affixTree');
    	var divTop = $('.esup-lecture.'+ namespace + ' div.scrollDivArticle');
    	if (divAffix && divTop) {
    		var affixHeight = $(divAffix).height();
    		var affixTop = $(divTop).offset().top;
    		var portletTop =  $(divPortlet).offset().top;
   // 		console.log('affix =' + affixHeight + " " + affixTop+ " " + portletTop);
    		if (affixHeight && affixTop && portletTop) {
    			var minHeight = affixHeight + affixTop - portletTop;
    			minHeight = $(divPortlet).css('min-height', minHeight + 'px').css('min-height');
   // 			console.log('min height =' + minHeight)
    		}
    	}
    }
    $(function() {
    	// retaillage a cause de l'affix hort flux qui dimensionne mal la fenetre:
    	resetMinHeightPortlet();
		markFirtVisible();
		if (priv.gereAffixMenu) {priv.gereAffixMenu() };
		
		// effacement des eventuels articles vide
		$('div.'+namespace + ' div.modeNoPublisher.contenuArticle').each(function() {
			if (! $('> *:first', this).size()) {
				$(this).hide();
			}
		});
	});
}
