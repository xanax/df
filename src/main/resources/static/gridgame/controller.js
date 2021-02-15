(function() {
    window.game = {};

    game.init = function() {
        game.data = {};
        game.offsetx = 0;
        game.offsety = 0;
        game.offsetz = 5;
        game.cursorx = 0;
        game.cursory = 0;
        game.cursorz = 0;
        game.selectionx = 0;
        game.selectiony = 0;
        game.selectionz = 0;
        game.mode = 'navigate'

        minAjax({
            url: "/newMap",
            type: "GET",
            data: {},
            success: function(data) {}
        });
        view.init();
        input.init();
    }

    game.update = function() {
        console.log('x: '+game.offsetx+' y: '+game.offsety+' z: '+game.offsetz);
        minAjax({
            url: "/gameData",
            type: "GET",
            data: {
                left: game.offsetx,
                top: game.offsety,
                z: game.offsetz,
                width: view.widthInTiles,
                height: view.heightInTiles
            },
            success: function(data) {
                game.data = JSON.parse(data);
                //console.log(game.data);
                view.update();
            }
        });
    }

    game.init();
    game.update();
})();
