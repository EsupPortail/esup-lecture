(function($){
	$(document).ready(function() {

		/***************************************
		 * esup-lecture functions
		 * **************************************/
//hide unused button when js activated
		$(".itemDisplayModeSelector").change(function () {
			$(".itemDisplayModeButton").click();
		});
		$(".itemDisplayModeButton").hide();
		
//resize 
		$(".treeButtonsArea").hide();
		$(".leftArea").css("width", "25%");
		$(".rightArea").css("width", "72%");
		$(".rightArea")
			.css("border-left-style", "solid")
			.css("border-color", "#CCCCCC");
		$(".leftArea")
			.resizable({
			handles : 'e',
			helper: 'ui-resizable-helper',
            stop: function (event, ui) {
                var offsetWidth = ui.size.width - ui.originalSize.width;
                $(".rightArea").css("width", $(".rightArea").width() - offsetWidth);
            }
		});
		
		$(window).resize(function() {
			$(".leftArea").css("width", "25%");
			$(".rightArea").css("width", "72%");
		});

		/***************************************
		 * rss-jquery-clickable functions
		 * **************************************/
		$('.lecture-highlightable').mouseover(function() {
			$(this).addClass('lecture-highlight');
		}).mouseout(function(){
			$(this).removeClass('lecture-highlight');
		});

		$('.lecture-clickable').css('cursor', 'pointer').attr('title', $(this).find('.lecture-link:first a:first').text());

		$('.lecture-clickable').click(function (event) {
			var current = $(this);
			var markAsUnreadButton = current.closest('.lecture-article').find('.lecture-markAsUnreadButton')[0];
			var markAsReadButton = current.closest('.lecture-article').find('.lecture-markAsReadButton')[0];
			var a = current.find('.lecture-link:first a:first'); 
			if(a.length != 0){
				window.open(a.attr('href'));
			};
			if(markAsUnreadButton) {
				markAsUnreadButton.click();
			}
			if (markAsReadButton) {
				markAsReadButton.click();
			}
		});

		/***************************************
		 * rss-jquery-inline functions
		 * **************************************/
		//See "En savoir plus.." link in xsl
		$(".article-block").children("a").click(function(){
			openNews($(this).attr("href"),$(this));
			return false;
		}); 

		//See "Resume..." link in xsl
		$(".replierArticle").children("a").click(function(){
			closeNews($(this));
			return false;
		});

		// Functions to open and close news inside portal
		function openNews(lien, p) {
			var resume = p.parent().parent();
			var div=resume.parent();

			resume.hide();
			div.children(".contenuArticle:first").each(function(){
				if ($(this).hasClass("articleCharge")) {
					$(this).show();
					div.children(".replierArticle:first").show();
					scrollNews($(this));
				} else {
					$(this).addClass("wait");
					$(this).show();
					$(this).load(lien.toString()+" #container", function() {
						$(this).removeClass("wait");
						$(this).addClass("articleCharge");
						$(this).find(".meta, .postmeta, #footer, #header").remove();
						$(this).find(".entry").children("h2:first, br:first").remove();
						$(this).find(".entry").children("div").children("div").children("p").removeAttr("style");
						div.children(".replierArticle:first").show();
						scrollNews($(this));
					});     
				}               
			});                     
		}                           

		function closeNews(p) {   
			var div=p.parent().parent();
			div.children(".replierArticle:first, .contenuArticle:first").hide();
			div.children(".resumeArticle:first").each(function(){
				$(this).show();
				scrollNews($(this));
			});
		}   

		function scrollNews(div) {
			var x = div.offset().top - 100;
			$('html,body').animate({scrollTop: x}, 500);
		}

		/***************************************
		 * rss-jquery-dialog functions
		 * **************************************/
		// Dialogs
		$('.dialog').each(function(index) {
			$(this).dialog({
				autoOpen: false,
				width: 850,
				buttons: {
					"Ok": function() { 
						$(this).dialog("close"); 
					}, 
				},
				resizable: false
			});
		})

		// Dialog Link
		$('.dialog_link').click(function(event){
			event.preventDefault();
			var href = $(this).attr('href');
			var s1 = 'iframe[src="';
			var s2 = '"]';
			var selector = s1.concat(href).concat(s2);
			//find div (parent of iframe with href as src attribute
			var div = $(selector).parent();
			div.dialog('open');
			return false;
		});

		//hover states on the static widgets
		$('.dialog_link').hover(
				function() { $(this).addClass('ui-state-hover'); }, 
				function() { $(this).removeClass('ui-state-hover'); }
		);

	});
})(jQuery);
