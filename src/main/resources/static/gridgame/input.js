(function() {
    window.input = {};

    var keys = {
        up: 87,
        down: 83,
        left: 65,
        right: 68,
        refresh: 190,
        pageUp: 38,
        pageDown: 40,
        pageLeft: 37,
        pageRight: 39,
        zup: 79,
        zdown: 76,
        mode: 32,
        select: 13,
        zone: 90,
        update: 88 // x
    };
    addEvent(document, "keydown", function(e) {

        // get key press in cross browser way
        code = e.which || e.keyCode;
        console.log('Keycode: '+code);
        var zinc = 0,
            index, newIndex;
        var dx = 0;
        var dy = 0;
        switch (code) {
            case keys.update:
                game.update();
                break;
            case keys.select:
                game.selectionx = game.cursorx + game.offsetx;
                game.selectiony = game.cursory + game.offsety;
                game.selectionz = game.offsetz;
                break;
            case keys.zone:
                if (game.selectionx != -1) {


                }

                break;
            case keys.mode:
                break;
            case keys.up:
                dy--;
                break;
            case keys.down:
                dy++;
                break;
            case keys.left:
                dx--;
                break;
            case keys.right:
                dx++;
                break;
            case keys.refresh:
                if($('#game-menu').css('display') == 'none') {
                    $('#game-menu').css('display', 'block');
                    $('#game').css('display', 'none');
                } else {
                    $('#game-menu').css('display', 'none');
                    $('#game').css('display', 'block');
                }
                break;
            case keys.zup:
                zinc = 1;
                break;
            case keys.zdown:
                zinc = -1;
                break;
            default:
                break;
        }
        if (dx !== 0 || dy !== 0) {
            if (game.mode == 'cursor') {
                game.cursorx += dx;
                game.cursory += dy;
                if (game.selectionx == -1) {
                    cursor.style.left = (game.cursorx * 24) + 'px';
                    cursor.style.top = (game.cursory * 24) + 'px';
                    cursor.style.width = '24px';
                    cursor.style.height = '24px';
                } else {
                    cursor.style.left = ((game.selectionx - x) * 24) + 'px';
                    cursor.style.top = ((game.selectiony - y) * 24) + 'px';
                    cursor.style.width = ((game.cursorx - (game.selectionx - x) + 1) * 24) + 'px';
                    cursor.style.height = ((game.cursory - (game.selectiony - y) + 1) * 24) + 'px';
                }
            } else {
                game.offsetx += dx * Math.trunc(view.widthInTiles / 4);
                game.offsety += dy * Math.trunc(view.heightInTiles / 4);
                if(game.offsetx < 0) {game.offsetx = 0}
                else if(game.offsetx > game.map.width - view.widthInTiles) {
                    game.offsetx = game.map.width - view.widthInTiles;
                }
                if(game.offsety < 0) {game.offsety = 0}
                else if(game.offsety > game.map.height - view.heightInTiles) {
                    game.offsety = game.map.height - view.heightInTiles;
                }
                game.update();
            }
            return false;
        } else if (zinc !== 0) {
            game.offsetz += zinc;
            game.update();
            return false;
        }
    });

    var isMouseDown = false;
    var startRowIndex = null;
    var startCellIndex = null;

    function selectTo(cell) {

        var row = cell.parent();
        var cellIndex = cell.index();
        var rowIndex = row.index();

        var rowStart, rowEnd, cellStart, cellEnd;

        if (rowIndex < startRowIndex) {
            rowStart = rowIndex;
            rowEnd = startRowIndex;
        } else {
            rowStart = startRowIndex;
            rowEnd = rowIndex;
        }

        if (cellIndex < startCellIndex) {
            cellStart = cellIndex;
            cellEnd = startCellIndex;
        } else {
            cellStart = startCellIndex;
            cellEnd = cellIndex;
        }

        for (var i = rowStart; i <= rowEnd; i++) {
            var rowCells = $(view.map).find("tr").eq(i).find("td");
            for (var j = cellStart; j <= cellEnd; j++) {
                rowCells.eq(j).addClass("selected");
            }
        }
        game.selectionx = game.offsetx + cellStart;
        game.selectiony = game.offsety + rowStart;
        game.selectionz = game.offsetz;

        game.cursorx = game.selectionx + cellEnd - cellStart;
        game.cursory = game.selectiony + rowEnd - rowStart;
        game.cursorz = game.offsetz;

    }

    input.init = function() {
        var table = $(view.map);

        table.find("td").mousedown(function(e) {
                isMouseDown = true;
                var cell = $(this);

                table.find(".selected").removeClass("selected"); // deselect everything

                if (e.shiftKey) {
                    selectTo(cell);
                } else {
                    cell.addClass("selected");
                    startCellIndex = cell.index();
                    startRowIndex = cell.parent().index();
                }

                return false; // prevent text selection
            })
            .mouseover(function() {
                if (!isMouseDown) return;
                table.find(".selected").removeClass("selected");
                selectTo($(this));

            })
            .bind("selectstart", function() {
                return false;
            });
        table.mouseup(function(e) {
            isMouseDown = false;
            var z = controls.zones();
            z.css('display', 'block');
            z.css('left', (e.clientX + 20) + 'px');
            z.css('top', (e.clientY + 20) + 'px');
        });
    }
})();