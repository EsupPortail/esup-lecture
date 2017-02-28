var lecture = lecture || {};
lecture.init = function($, namespace, portletId, urlMarkRead, urlMarkAllRead,
    urlFiltrItem) {
  (function($, namespace, portletId, urlMarkReadv, urlMarkAllRead,
      urlFiltrItem) {
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
//filtrer par rubribreque les articles publisher
      $(".rubriqueFiltre").each(function() {
        $(this).click(function(e) {
          e.preventDefault();
          var afficherRubSelect = $(this).find(".srcName").val();
          var reponse1 = "<label>" + afficherRubSelect + "</label>";
          var reponse = "<label>" + afficherRubSelect
              + "</label><span class='caret margeCarret'></span>";
          $("#" + portletId + "rubSelectedDiv1").html(reponse1);
          $("#" + portletId + "rubSelectedDiv2").html(reponse);
          var srcId = $(this).find(".srcId").val();
          $("#" + portletId + "catSeletc").val('');
          $("#" + portletId + "SrcSeletc").val(srcId);
          $("#" + portletId + "rubSeletc").val(afficherRubSelect);
          if(srcId=="toutRub"){
            $(".itemShowFilter").each(function() {
              $(this).show();
            });
          }else{
          $(".itemShowFilter").each(function() {
            var showItem = false;
            $(".uidDesRubriquesItem").each(function() {
              if ($(this).val() == srcId) {
                showItem = true;
              }
            });
            if (showItem == false) {
              $(this).hide();
            }else{
              $(this).show();
            }
          });
          }
        });
      });
//filter les non lus
      $(".checkReadItem").each(function(){
        $(this).change(function(e) {
          if ($("#" + portletId + "checkBoxNonLu").is(':checked')) {
                  $("#" + portletId + "listNonLu").val("val2")
                } else {
                  $("#" + portletId + "listNonLu").val("val1")
                }
         $("#formReadMode"+portletId).submit();
        });
      });
//$(".checkReadItem").each(function(){
//   $(this).change(function(e) {
//   if ($(this).is(':checked')) {
//     $(".itemShowFilter").each(function() {
//       var isRead=$(this).find(".itemShowFilterIsRead").val();
//       if(isRead=="true"){
//         $(this).hide();
//       }else{
//         $(this).show();
//       }
//     });
//   }else{
//     $(".itemShowFilter").each(function() {
//     $(this).show();
//     });
//   }
//   });
//});
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
                                  $("#lecture-"
                                      + portletId
                                      + " #"
                                      + portletId
                                      + "modalPublisher"));
                          $(
                              "#lecture-"
                                  + portletId
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
                            var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="marquerItemLu(\''
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
                  var htmlCode = '<i class="fa fa-eye fa-stack-1x" onclick="marquerItemLu(\''
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
                            var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="marquerItemLu(\''
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
                  var htmlCode = '<i class="fa fa-eye-slash fa-stack-1x" onclick="marquerItemLu(\''
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
    window.marquerItemLu = marquerItemLu;
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
    window.marquerToutItemLu = marquerToutItemLu;
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
    window.filtrerNonLus = filtrerNonLus;

    function filtrerParCategorie(catid) {
      $("#" + portletId + "catSeletc").val(catid);
      $("#" + portletId + "SrcSeletc").val('');
      $("#" + portletId + "rubSeletc").val('');
      filtrerNonLus('');
    }
    window.filtrerParCategorie = filtrerParCategorie;

    function filtrerParRubrique(catid, srcid, afficherRubSelect, val) {
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
      filtrerNonLus('');
      if (val == 'show') {
        $("#mainmenurow" + portletId).show();
        $("#menupos1" + portletId).hide();
      }
    }
    window.filtrerParRubrique = filtrerParRubrique;

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
        $("#" + portletId + "listNonLu").val("val2");
        filtrerNonLus('');
      } else {
        $("#" + portletId + "listNonLu").val("val1");
        AfficherTout();
      }
    }
    window.filtrerPublisherNonLusMobile = filtrerPublisherNonLusMobile;
  })($, namespace, portletId, urlMarkRead, urlMarkAllRead, urlFiltrItem);
}
