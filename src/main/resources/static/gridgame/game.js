var cards = document.getElementById("game-board").getElementsByClassName("card");
var gameInfo = document.getElementById('game-info');
var board = document.getElementById("game-board");
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
      gameInfo.innerHTML = 'x '+x+' y '+ y +' z '+z+'<br>'+ code+' time: '+new Date();
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
                 pageUp: 38, pageDown: 40, pageLeft: 37, pageRight: 39, zup: 79, zdown: 76};
    addEvent(document, "keydown", function(e) {

		// get key press in cross browser way
		code = e.which || e.keyCode;

		var increment, pageXinc = 0, pageYinc = 0, zinc = 0, index, newIndex, active;
		switch(code) {
			case keys.up:
			    pageYinc = -height / 2;
				break;
			case keys.down:
			    pageYinc = height / 2;
				break;
			case keys.left:
			    pageXinc = -width / 2;
				break;
			case keys.right:
			    pageXinc = width / 2;
				break;
			case keys.refresh:
				location.reload();
				break;
//			case keys.up:
//				increment = -width;
//				break;
//			case keys.down:
//				increment = width;
//				break;
//			case keys.left:
//				increment = -1;
//				break;
//			case keys.right:
//				increment = 1;
//				break;
			case keys.zup:
				zinc = 1;
				break;
			case keys.zdown:
			    zinc = -1;
				break;
			default:
				increment = 0;
				break;
		}
		if(pageXinc != 0 || pageYinc != 0 || zinc != 0) {
		    x += pageXinc;
		    y += pageYinc;
		    z += zinc;
		    refresh();
		    return false;
		}
		if (increment !== 0) {
			active = document.getElementById("game-board").getElementsByClassName("active")[0];
			index = findItem(cards, active);
			newIndex = index + increment;
			if (newIndex >= 0 && newIndex < cards.length) {
				removeClass(active, "active");
				addClass(cards[newIndex], "active");
			}
			// prevent default handling of up, down, left, right keys
			return false;
		}

    });
    init();
})();