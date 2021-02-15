function update() {
    var time = new Date().getTime();
    var tiles = game.data.tiles;
    var blockData = game.data.blockData;
    var heights = game.data.heights;

    $('.name').remove();

    for (var y = 0; y < game.heightInTiles; y++) {
        for (var x = 0; x < game.widthInTiles; x++) {
            var id = x + y * game.widthInTiles;
            var tile = view.imageElements[id];

            if (heights[id] == game.offsetz) {
                opacity = "1";
            } else if (heights[id] == game.offsetz - 1) {
                opacity = "0.5";
            } else if (heights[id] == game.offsetz - 2) {
                opacity = "0.25";
            }
            if(tile.css('opacity') != opacity) {
                tile.css('opacity', opacity);
               }

            if (heights[id] <= game.offsetz && heights[id] >= game.offsetz - 2) {
                // TODO made a big diff checking if image already set
                if (tiles[id] == '2' && tile.attr('src') != 'path-tile.png') {
                    tile.attr('src', 'path-tile.png');
                } else if (tiles[id] == '1' && tile.attr('src') != 'black.png') { // ROCK
                    tile.attr('src', 'black.png');
                } else if (tiles[id] == '6') {
                    tile.attr('src', 'brutal-helm.png');
                } else if (tiles[id] == '4') {
                    tile.attr('src', 'beech.png');
                }

                if (blockData[x + '-' + y + '-' + game.offsetz]) {
                    var id = 'name' + (x + '-' + y + '-' + game.offsetz);
                    var name = $("<div></div>");
                    name.addClass('name');
                    name.attr('id', id);
                    tile.parent().append(name);
                    //name.style.left = (x * 24) + 'px';
                    //name.style.top = (y * 24) + 'px';
                    name.html(blockData[x + '-' + y + '-' + game.offsetz].name);
                }
            } else if (heights[id] < game.offsetz - 2) {
                tile.attr('src', 'black.png');
                tile.css('opacity', '0.2');
               // card.innerHTML = '<image src="black.png" style="opacity: 0.2;"></image>';
            } else {
                tile.attr('src', 'black.png');
            }
        }
    }
    var end = (new Date().getTime() - time) / 1000;
    console.log('Refresh time: ' + end);
}


(function() {
    window.view = {};
    view.imageElements = new Array(game.widthInTiles * game.heightInTiles);

    for (var y = 0; y < game.heightInTiles; y++) {
        var row = $('<tr></tr>');
        game.map.append(row);
        for (var x = 0; x < game.widthInTiles; x++) {
            var cell = $('<td></td>');
            var img = $('<img></img>');
            img.attr('id', y * game.widthInTiles + x);
            view.imageElements[y * game.widthInTiles + x] = img;
            row.append(cell);
            cell.append(img);
        }
    }

    var i = 0;

//    setInterval(function() {
//        i += 2;
//        console.log("File 1 counter: " + i);
//        getGameData();
//        refresh();
//    }, 5000);

    refresh();
}());