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

	});

})(jQuery);
