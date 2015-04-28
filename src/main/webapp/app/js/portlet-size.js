(function($){

    var $portletContainers;

    $(document).ready(function() {
        $portletContainers = $('.portlet-container');
        $(window).resize(onWindowResize);
        onWindowResize();
    });

    function onWindowResize() {

        $portletContainers.each(function(index) {
            var $that = $(this);
            var portletWidth = $that.width();

            $that.removeClass("xs sm md lg");
            if(portletWidth < 768) {
                $that.addClass("xs");
            }
            if(portletWidth >= 768 && portletWidth < 992) {
                $that.addClass("sm");
            }
            if(portletWidth >= 992 && portletWidth < 1200) {
                $that.addClass("md");
            }
            if(portletWidth >= 1200) {
                $that.addClass("lg");
            }
        });
    }

})(jQuery);