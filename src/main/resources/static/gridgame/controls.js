(function() {
    window.controls = {};
    controls.element = $('#controls');

// TODO get from server
    controls.data = {
        zone_types: [
            'mine', 'forest'
        ]
    };

    controls.zones = function() {
        controls.element.empty();
        for(i = 0; i < controls.data.zone_types.length; i++) {
            var button = $('<button>'+controls.data.zone_types[i]+'</button>');
            controls.element.append(button);
            button.click(function() {
                console.log(this);
                controls.element.hide();
                var type = $(this).text();
                if(type == 'mine') {
                   minAjax({
                        url: "/zone",
                        type: "GET",
                        data: {
                            startx: game.selectionx,
                            starty: game.selectiony,
                            startz: game.selectionz,
                            endx: game.cursorx,
                            endy: game.cursory,
                            endz: game.offsetz
                        },
                        success: function(data) {
                            $('.selected').removeClass('selected');
                            game.selectionx = -1;
                            //alert(data);
                        }
                    });
                }
            });
        }
        return controls.element;
    }


})();