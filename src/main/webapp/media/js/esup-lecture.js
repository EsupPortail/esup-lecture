(function($){
	$(document).ready(function() {  
		$(".itemDisplayModeSelector").change(function () {
			$(".itemDisplayModeButton").click();
		});

		$(".itemDisplayModeButton").hide();

		$('.lecture-readArticle .lecture-link, .lecture-unreadArticle .lecture-link').hide();
		
		$('.lecture-highlightable').mouseover(function() {
			$(this).addClass('lecture-highlight');
		}).mouseout(function(){
			$(this).removeClass('lecture-highlight');
		});
		
		$('.lecture-clikable').css('cursor', 'pointer').attr('title', $(this).find('.lecture-link:first a:first').text());
		
		$('.lecture-clikable').click(function (event) {
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
	    
	    /** 
	     * Functions to open and close news inside portal
	     */
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
				
	});

})(jQuery);
