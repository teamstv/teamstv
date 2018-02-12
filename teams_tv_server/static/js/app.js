var gridster;

var serialization = [];

$(function() {

    console.log(screen.width + ' ' + screen.height);

    gridster = $(".gridster ul").gridster({
        widget_base_dimensions: [Math.round(screen.width / 3), Math.round(screen.height / 2)],
        widget_margins: [0, 0],
        avoid_overlapped_widgets: false,
        shift_widgets_up: false
    }).data('gridster').disable();

    function getBlockLayout(block) {
        switch (block) {
            case "1":
                return {
                    col: 1,
                    row: 1,
                    size_x: 2,
                    size_y: 1
                };
                break;
            case "2":
                return {
                    col: 1,
                    row: 2,
                    size_x: 2,
                    size_y: 1
                };
                break;
            case "3":
                return {
                    col: 3,
                    row: 1,
                    size_x: 1,
                    size_y: 1
                };
                break;
            case "4":
                return {
                    col: 3,
                    row: 2,
                    size_x: 1,
                    size_y: 1
                };
                break;

            case "12":
                return {
                    col: 1,
                    row: 1,
                    size_x: 2,
                    size_y: 2
                };
                break;
            case "13":
                return {
                    col: 1,
                    row: 1,
                    size_x: 3,
                    size_y: 1
                };
                break;
            case "24":
                return {
                    col: 1,
                    row: 2,
                    size_x: 3,
                    size_y: 1
                };
                break;
            case "34":
                return {
                    col: 3,
                    row: 1,
                    size_x: 1,
                    size_y: 2
                };
                break;

            case "1234":
                return {
                    col: 1,
                    row: 1,
                    size_x: 3,
                    size_y: 2
                };
                break;
        }
    }


    function placeClock(id, options) {
        var options = options || {};
        var cid = "clock_" + id;

        $("#" + id).html("");
        $("#" + id).append("<iframe id='" + cid + "' src='/clock'></iframe>"); // It was decided to leave the clock in IFRAME

        setTimeout(function() {
            // THIS FIXES CLOCK BEING KNOCKED DOWN
            $(".weatherwidget-io").css({
                position: "static",
                display: "inline"
            });
        }, 50);
    }

    function placeNews(id, options) {
        options = options || {};

        var cid = "news_"+id;

        $("#"+id).html("");
        $("#"+id).append("<iframe id='" + cid + "' src='/news?" + $.param(options) + "'></iframe>"); // Let it live in IFRAME
    }

    function placeLogo(id, option) {
        if (options.src) {
            $("#" + id).html("<img src='" + options.src + "' />");
        }
    }

    function parseBlock(id, type, data) {
        if (!id || !type) return;

        type = type.toLowerCase();

        console.log('Parsing block', id, type, data);

        if (type === "map") {
            placeMap(id, data);
        }

        if (type === "text" || type === "html") {
            $("#" + id).html(data);
        }

        if (type === "carousel" || type === "carousel_img") {
            imgs = []
            $.ajax({
                dataType: "json",
                url: data.folder,
                cache: false,
                success: function(res) {
                    imgs = res;
                    placeImgCarousel(id, imgs, data.interval);
                }
            });
        }

        if (type === "carousel_text" || type === "carousel_html") {
            placeHtmlCarousel(id, data.pages, data.interval);
        }

        if (type === "clock") {
            placeClock(id, data);
        }

        if (type === "news") {
            placeNews(id, data);
        }

        if (type === "logo") {
            placeLogo(id);
        }
    }

    function placeMap(id, options) {
        var mid = "map_" + id;
        var wrapper = '<div id="' + mid + '" style="width: 100%; height: 100%"></div>';
        var refreshMapInterval;

        var myMap;

        var options = options || {};

        $("#" + id).html(wrapper);

        ymaps.ready(init);

        function ticker() {
            if (refreshMapInterval) {
                clearInterval(refreshMapInterval);
            }
            refreshMapInterval = setInterval(function () {
                myMap.destroy();
                init();
            }, (5 * 60 * 1000)); // traffic provider does not update the map events more often
        }

        function init() {
            myMap = new ymaps.Map(mid, {
                center: options.center || center[59.913, 30.316564],
                zoom: options.zoom || 12,
                controls: []
            });

            var trafficControl = new ymaps.control.TrafficControl({
                state: {
                    providerKey: 'traffic#actual',
                    trafficShown: true
                }
            });
            myMap.controls.add(trafficControl);
            trafficControl.getProvider('traffic#actual').state.set('infoLayerShown', true);

            ticker();
        }
    }

    function placeImgCarousel(id, pages, interval) {
        var cid = "carousel_" + id;
        var wrapper = '<div class="owl-carousel owl-theme" id="' + cid + '"></div>';

        $("#" + id).html(wrapper);

        function initCarousel(data, interval) {

            $.each(data, function(index, page) {
                $("#" + cid).append("<div style='height: 100%;'><img style='height: 100%; margin: auto;' src='" + page + "'/></div>");
            });

            $("#" + cid).owlCarousel({
                items: 1,
                loop: true,
                nav: false,
                dots: false,
                autoplay: true,
                autoplayTimeout: interval || (10 * 1000)
            });
        }

        initCarousel(pages, interval);
    }

    function placeHtmlCarousel(id, pages, interval) {
        var cid = "carousel_" + id;
        var wrapper = '<div class="owl-carousel owl-theme" id="' + cid + '"></div>';

        $("#" + id).html(wrapper);

        function initCarousel(data, interval) {

            $.each(data, function(index, page) {
                $("#" + cid).append("<div><center>" + page + "</center></div>");
            });

            $("#" + cid).owlCarousel({
                items: 1,
                loop: true,
                nav: false,
                dots: false,
                autoplay: true,
                autoplayTimeout: interval || 3000
            });
        }

        initCarousel(pages, interval);
    }

    function clearGrid(cleanup) {
        gridster.remove_all_widgets();
        if (cleanup) {
            gridster.destroy();
            gridster = null;
            $('#grid-container ul').empty();

            gridster = $(".gridster ul").gridster({
                widget_base_dimensions: [Math.round(screen.width / 3), Math.round(screen.height / 2)],
                widget_margins: [0, 0],
                avoid_overlapped_widgets: false,
                shift_widgets_up: false
            }).data('gridster').disable();
        }
    }

    function buildGrid(elements, cleanup) {
        clearGrid(cleanup);

        var temp_elements = _.clone(elements);

        temp_elements = Gridster.sort_by_row_and_col_asc(temp_elements);

        $.each(temp_elements, function(i, element) {
            gridster.add_widget('<li id="' + element.uid + '" type="' + element.type + '"></li>', element.size_x, element.size_y, element.col, element.row);
            parseBlock(element.uid, element.type, element.data);
        });
        console.log('Built grid', temp_elements);
    }

    function getData(url, func) {
        var eq = true;
        var firstLoad = true;
        var temp = [];
        var tids = "";
        var sids = "";

        $.ajax({
            dataType: "json",
            url: url,
            cache: false,
            success: function(data) {
                console.log('Got data', data);

                $.each(data, function(i, block) {
                    var blockLayout = getBlockLayout(block.blocks.join(""));

                    temp.push({
                        col: blockLayout.col,
                        row: blockLayout.row,
                        size_x: blockLayout.size_x,
                        size_y: blockLayout.size_y,
                        type: block.widget,
                        data: block.data,
                        uid: block.uid,
                        modified: block.last_modified
                    });
                });

                _.orderBy(temp, ['uid'], ['desc']);
                _.orderBy(serialization, ['uid'], ['desc']);

                if (serialization.length == 0) {
                    eq = false;
                } else if (serialization.length != temp.length) {
                    eq = false;
                } else {
                    for (var t = 0; t < temp.length; t++) {
                        tids = tids + temp[t].modified;
                    }

                    for (var s = 0; s < serialization.length; s++) {
                        sids = sids + serialization[s].modified;
                    }

                    if (tids != sids) {
                        eq = false;
                    }

                }

                if (!eq) {
                    if (!serialization.length) firstLoad = true;
                    else firstLoad = false;
                    serialization = temp;
                    buildGrid(serialization, !firstLoad);
                }

                // Used for Telebot
                if (func) {
                    func()
                }
            },
            error: function(error) {
                console.log("Request Error", error);
            }
        });
    }

    getData("events/current");
    var getDataInterval = setInterval(function() {
        getData("events/current");
    }, (60 * 1000));

    function getTelebot() {
        getData("telebot", getTelebot);
    }
    getTelebot();
});