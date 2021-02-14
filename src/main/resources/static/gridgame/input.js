(function() {
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
                refresh();
                break;
            case keys.select:
                game.selectionx = game.cursorx + game.offsetx;
                game.selectiony = game.cursory + game.offsety;
                game.selectionz = game.offsetz;
                break;
            case keys.zone:
                if (game.selectionx != -1) {

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
                            game.selectionx = -1;
                            //alert(data);
                        }
                    });
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
                location.reload();
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
                game.offsetx += dx * Math.trunc(game.widthInTiles / 2);
                game.offsety += dy * Math.trunc(game.heightInTiles / 2);
                refresh();
            }
            return false;
        } else if (zinc !== 0) {
            game.offsetz += zinc;
            refresh();
            return false;
        }
    });

    var table = $("#table");

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
            var rowCells = table.find("tr").eq(i).find("td");
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

    $(document).mouseup(function() {
        isMouseDown = false;
    });
})();