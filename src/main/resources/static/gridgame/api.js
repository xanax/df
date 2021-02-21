(function() {
    window.api = {};

    function newPlayer() {
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


})();