(function ($) {
    $.fn.blink = function (options) {
        var defaults = {delay: 500};
        var options = $.extend(defaults, options);
        return $(this).each(function (x, y) {
            setInterval(function () {
                if ($(y).css("visibility") === "visible") {
                    $(y).css('visibility', 'hidden');
                } else {
                    $(y).css('visibility', 'visible');
                }
            }, options.delay);
        });
    };
}(jQuery));