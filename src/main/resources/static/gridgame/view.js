(function() {

var i = 0;

    window.game = {};

    game.test = 99;
  setInterval(function() {
    i += 2;
    console.log("File 1 counter: " + i);
  }, 1000);

var table = $("#table");

var isMouseDown = false;
var startRowIndex = null;
var startCellIndex = null;
document.write($(document).height());
document.write($(window).height());
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
}

table.find("td").mousedown(function (e) {
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
.mouseover(function () {
    if (!isMouseDown) return;
    table.find(".selected").removeClass("selected");
    selectTo($(this));
})
.bind("selectstart", function () {
    return false;
});

$(document).mouseup(function () {
    isMouseDown = false;
});

}());

