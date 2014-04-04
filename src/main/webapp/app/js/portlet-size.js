var $portletContainers;

$(document).ready(function() {
    $portletContainers = $(".portlet-container");
// Resize event isn't fired on DOM Content Loaded, we launch the function manually
    onWindowResize();
    $(window).resize(onWindowResize);
});

function onWindowResize() {
    $portletContainers.each(function(index) {
        var $that = $(this);
        var portletWidth = $that.width()
        $that.removeClass("xs sm md lg");
        if(portletWidth < 768)
            $that.addClass("xs");
        if(portletWidth >= 768 && portletWidth < 992)
            $that.addClass("sm");
        if(portletWidth >= 992 && portletWidth < 1200)
            $that.addClass("md");
        if(portletWidth >= 1200)
            $that.addClass("lg");
    });
}