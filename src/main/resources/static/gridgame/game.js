var cards = document.getElementById("game-board").getElementsByClassName("card");
var gameInfo = document.getElementById('game-info');
var board = document.getElementById("game-board");
var cursor = document.getElementById("cursor");
cursor.style.display = 'none';
var cursorx = 0;
var cursory = 0;
var selectionx = -1;
var selectiony;
var selectionz;
var mode = 'navigate';

var x = 0;
var y = 40;
var z = 5;
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
      'x-selection '+selectionx+' y-selection '+selectiony+' z-selection '+selectionz+'<br>'+
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
          var allData = JSON.parse(data);
          var tiles = allData.tiles;
          var heights = allData.heights;
          var blockData = allData.blockData;

var elements = document.getElementsByClassName('name');
while(elements.length > 0){
    elements[0].classList.remove('name');
}

            for(var y = 0; y < height; y++) {
                for(var x = 0; x < width; x++) {
                var id = x + y * width;
                var card = document.getElementById(id);
                if(heights[id] == z) {
                    opacity = "1";
                } else if(heights[id] == z-1) {
                    opacity = "0.5";
                } else if(heights[id] == z-2) {
                    opacity = "0.25";
                }


                if(heights[id] <= z && heights[id] >= z - 2) {
                    if(tiles[id] == '2') {
                        card.innerHTML = '<image src="path-tile.png" style="opacity: '+opacity+';"></image>';
                    } else if(tiles[id] == '1') { // ROCK
                        card.innerHTML = '<image src="black.png" style="opacity: '+opacity+';"></image>';
                    } else if(tiles[id] == '6') {
                        card.innerHTML = '<image src="brutal-helm.png" style="opacity: '+opacity+';"></image>';
                    } else if(tiles[id] == '4') {
                        card.innerHTML = '<image src="beech.png" style="opacity: '+opacity+';"></image>';
                    } else {
                    }
        if(blockData[x+'-'+y+'-'+z]) {
                var id = 'name'+(x + '-'+ y +'-'+ z);
                var name = document.createElement("div");
                addClass(name, 'name');
                name.id = id;
                card.appendChild(name);
               //name.style.left = (x * 24) + 'px';
               //name.style.top = (y * 24) + 'px';
               name.innerHTML = blockData[x+'-'+y+'-'+z].name;
            }
              } else if(heights[id] < z - 2) {
                card.innerHTML = '<image src="black.png" style="opacity: 0.2;"></image>';
              } else {
                  card.innerHTML = '<image src="black.png"></image>';
              }
//                } else if(map.map[x][y][0] > 101) {
//                   //card.style.backgroundColor = "lightgray";
//                    card.innerHTML = '<image src="path-tile.png" style="opacity: 0.5;"></image>';
//                } else if(map.map[x][y][0] == 4) {
//                   // card.innerHTML = '^';
//                } else if(map.map[x][y][0] > 1) {
//                    card.innerHTML = '<image src="grass.png"></image>';
//                } else {
//                    card.innerHTML = '<image src="path-tile.png"></image>';
//                   // card.style.backgroundColor = "black";
//                }
//                //addClass(card, map.map[x][y][0]);
//                if(map.map[x][y].length > 1 ) {
//                    card.innerHTML = '<image src="brutal-helm.png"></image>';
//                } else {
//                }
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
                 mode: 32, select: 13, zone: 90};
    addEvent(document, "keydown", function(e) {

		// get key press in cross browser way
		code = e.which || e.keyCode;

		var zinc = 0, index, newIndex;
        var dx = 0;
        var dy = 0;
		switch(code) {
            case keys.select:
                if(selectionx != -1) {

                    minAjax({
                    url:"/findPath",
                    type:"GET",
                    data:{
                        startx: selectionx,
                        starty: selectiony,
                        startz: selectionz,
                        endx: cursorx + x,
                        endy: cursory + y,
                        endz: z
                    },
                    success: function(data){
                        selectionx = -1;
                        //alert(data);
                    }
                    });
                } else {
                    selectionx = cursorx + x;
                    selectiony = cursory + y;
                    selectionz = z;
                }
                break;
		    case keys.zone:
                if(selectionx != -1) {

                    minAjax({
                    url:"/zone",
                    type:"GET",
                    data:{
                        startx: selectionx,
                        starty: selectiony,
                        startz: selectionz,
                        endx: cursorx + x,
                        endy: cursory + y,
                        endz: z
                    },
                    success: function(data){
                        selectionx = -1;
                        //alert(data);
                    }
                    });
                }

		        break;
		    case keys.mode:
		        if(mode == 'cursor') {
		            mode = 'navigate';
		            cursor.style.display = 'none';
		        } else {
		            mode = 'cursor';
		            cursor.style.display = 'block';
		            cursorx = width/2;
		            cursory = height / 2;
                    cursor.style.left = (cursorx * 24) + 'px';
                    cursor.style.top = (cursory * 24) + 'px';
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
                if(selectionx == -1) {
                    cursor.style.left = (cursorx * 24) + 'px';
                    cursor.style.top = (cursory * 24) + 'px';
                    cursor.style.width = '24px';
                    cursor.style.height = '24px';
                } else {
                    cursor.style.left = ((selectionx - x) * 24) + 'px';
                    cursor.style.top = ((selectiony - y) * 24) + 'px';
                    cursor.style.width = ((cursorx - (selectionx - x) +1) * 24 ) +'px';
                    cursor.style.height = ((cursory - (selectiony - y) +1) * 24 ) +'px';

                }
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
