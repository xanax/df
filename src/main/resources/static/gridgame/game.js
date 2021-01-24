var cards = document.getElementById("game-board").getElementsByClassName("card");
var gameInfo = document.getElementById('game-info');
var board = document.getElementById("game-board");
var cursor = document.getElementById("cursor");
cursor.style.display = 'none';
var cursorx = 0;
var cursory = 0;
var selectionx;
var selectiony;
var mode = 'navigate';

var x = 0;
var y = 0;
var z = 4;
var width = 24;
var height = 20;
var code = '';
var mapWidth = 100;
var mapHeight = 100;
var mapDepth = 8;

function init() {
    for(var y = 0; y < height; y++) {
        for(var x = 0; x < width; x++) {
        var newItem = document.createElement("div");
        addClass(newItem, 'card');
        newItem.id = x + y * width;
        board.appendChild(newItem);
        }
    }
        minAjax({
        url:"/newMap",
        type:"GET",
        data:{},
        success: function(data){
        }
      });

    refresh();
    var intervalId = setInterval(function() {
    //refresh();
      gameInfo.innerHTML = 'x-offset '+x+' y-offsey '+ y +' z '+z+'<br>'+
      'x-selection '+selectionx+' y-selection '+selectiony+'<br>'+
      code+' time: '+new Date();
    }, 1000);

    // You can clear a periodic function by uncommenting:
    // clearInterval(intervalId);
}

function refresh() {
        if(x < 0) {
            x = 0;
        } else if(x > mapWidth - width) {
            x = mapWidth - width;
        }
        if(y < 0) {
            y = 0;
        } else if(y > mapHeight - height) {
            y = mapHeight - height;
        }
        if(z < 1) {
            z = 1;
        } else if(z > mapDepth - 1) {
            z = mapDepth - 1;
        }
        minAjax({
        url:"/gameData",
        type:"GET",
        data:{
          left: x,
          top: y,
          z: z,
          width: width,
          height: height
        },
        success: function(data){
          map = JSON.parse(data);
            for(var y = 0; y < height; y++) {
                for(var x = 0; x < width; x++) {
                var id = x + y * width;
                //i = map.map[x][y] * 32;
                //newItem.style.backgroundPosition = i+'px 0px';
                var card = document.getElementById(id);
                if(map.map[x][y][0] == -1) {
                    card.style.backgroundColor = "white";
                } else if(map.map[x][y][0] > 101) {
                    card.style.backgroundColor = "lightgray";
                } else if(map.map[x][y][0] > 1) {
                    card.style.backgroundColor = "gray";
                } else {
                    card.style.backgroundColor = "black";
                }

                if(map.map[x][y].length > 1 ) {
                    card.innerHTML = 'X';
                } else {
                card.innerHTML = '';
                }
                //document.getElementById("1").innerHTML = '&#9786';
                //addClass(document.getElementById("1"), 'bold');
            }
            }


            addClass(cards[0], 'active');


        }
      });
}

(function() {
    var keys = { up: 87, down: 83, left: 65, right: 68,
                 refresh: 190,
                 pageUp: 38, pageDown: 40, pageLeft: 37, pageRight: 39, zup: 79, zdown: 76,
                 mode: 32, select: 13};
    addEvent(document, "keydown", function(e) {

		// get key press in cross browser way
		code = e.which || e.keyCode;

		var zinc = 0, index, newIndex;
        var dx = 0;
        var dy = 0;
		switch(code) {
            case keys.select:
                selectionx = cursorx + x;
                selectiony = cursory + y;
                break;
		    case keys.mode:
		        if(mode == 'cursor') {
		            mode = 'navigate';
		            cursor.style.display = 'none';
		        } else {
		            mode = 'cursor';
		            cursor.style.display = 'block';
		        }
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
		if(dx !== 0 || dy !== 0) {
		    if(mode == 'cursor') {
                cursorx += dx;
                cursory += dy;
                cursor.style.left = (cursorx * 24) + 'px';
                cursor.style.top = (cursory * 24) + 'px';
		    } else {
                x += dx * width / 2;
                y += dy * height / 2;
                refresh();
		    }
		    return false;
		} else if (zinc !== 0) {
            z += zinc;
            refresh();
            return false;
		}
    });
    init();
})();