// refined add event cross browser
function addEvent(elem, event, fn) {
    if (typeof elem === "string") {
        elem = document.getElementById(elem);
    }

    // avoid memory overhead of new anonymous functions for every event handler that's installed
    // by using local functions
    function listenHandler(e) {
        var ret = fn.apply(this, arguments);
        if (ret === false) {
            e.stopPropagation();
            e.preventDefault();
        }
        return(ret);
    }

    function attachHandler() {
        // set the this pointer same as addEventListener when fn is called
        // and make sure the event is passed to the fn also so that works the same too
        var ret = fn.call(elem, window.event);
        if (ret === false) {
            window.event.returnValue = false;
            window.event.cancelBubble = true;
        }
        return(ret);
    }

    if (elem.addEventListener) {
        elem.addEventListener(event, listenHandler, false);
    } else {
        elem.attachEvent("on" + event, attachHandler);
    }
}



function addClass(elem, cls) {
    var oldCls = elem.className;
    if (oldCls) {
        oldCls += " ";
    }
    elem.className = oldCls + cls;
}

function removeClass(elem, cls) {
    var str = " " + elem.className + " ";
    elem.className = str.replace(" " + cls + " ", " ").replace(/^\s+|\s+$/g, "");
}


function findItem(items, target) {
    for (var i = 0; i < items.length; i++) {
        if (items[i] === target) {
            return(i);
        }
    }
    return(-1);
}
