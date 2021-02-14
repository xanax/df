(function() {
    window.game = {};
    game.map = $("#table");
    game.data = {};
    game.tileSize = 24;
    game.widthInTiles = Math.trunc($(document).width() / game.tileSize);
    game.heightInTiles = Math.trunc($(document).height() / game.tileSize);
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
    console.log("Calculated width/height in tiles: " + game.widthInTiles + '/' + game.heightInTiles);

    init();
})();

function init() {
    minAjax({
        url: "/newMap",
        type: "GET",
        data: {},
        success: function(data) {}
    });
}

function refresh() {
    console.log('x: '+game.offsetx+' y: '+game.offsety+' z: '+game.offsetz)
    minAjax({
        url: "/gameData",
        type: "GET",
        data: {
            left: game.offsetx,
            top: game.offsety,
            z: game.offsetz,
            width: game.widthInTiles,
            height: game.heightInTiles
        },
        success: function(data) {
            game.data = JSON.parse(data);
            console.log(game.data);
            update();
        }
    });
}