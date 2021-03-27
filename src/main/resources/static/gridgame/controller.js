(function() {
    window.game = {};

    game.newGame = function() {
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
        game.playerId = '';

        //TODO bring from backend
        game.map = {};
        game.map.width = 100;
        game.map.height = 100;

        game.mode = 'navigate'

        minAjax({
            url: "/newGame",
            type: "GET",
            data: {},
            success: function(data) {
                //game.data.gameId = JSON.parse(data);
                game.data.gameId = data;
                console.log('Game ID: '+game.data.gameId);

                minAjax({
                    url: "/newPlayer",
                    type: "GET",
                    data: {
                        gameId: game.data.gameId
                    },
                    success: function(data) {
                        game.playerId = data;
                        console.log('Player ID: '+game.playerId);
                        view.init();
                        input.init();

                        game.update();
                    }
                });
            }
        });
    }

    game.update = function() {
        console.log('x: '+game.offsetx+' y: '+game.offsety+' z: '+game.offsetz);
        minAjax({
            url: "/frameData",
            type: "GET",
            data: {
                playerId: game.playerId,
                left: game.offsetx,
                top: game.offsety,
                z: game.offsetz,
                width: view.widthInTiles,
                height: view.heightInTiles
            },
            success: function(data) {
                game.frameData = JSON.parse(data);
                //console.log(game.data);
                view.update();
            }
        });
    }

})();
