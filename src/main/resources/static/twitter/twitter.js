var tweetsDiv = document.getElementById('tweets');
function refresh() {
    tweetsDiv.innerHTML = 'Loading...';
    minAjax({
    url:"/twitter.json",
    type:"GET",
    data:{
      name:"batman",
      profession:"detective",
      worth:"Rich",
      company:"Wayne Enterprises"
    },
    success: function(data) {
        tweetsDiv.innerHTML = '';
      tweets = JSON.parse(data);
      for(i = 0; i < tweets.length; i++) {
      var tweet = document.createElement('table');
        tweet.id = i;
        tweet.innerHTML = '<tr><td valign="top"><img class="profile-pic" src="'+tweets[i].profileImage+'"/></td>'+
        '<td>'+
        '<b>'+tweets[i].user+"</b> "+tweets[i].time+'<br>'+
        tweets[i].text+'<br>'+
        tweets[i].retweets+
        '-'+tweets[i].likes+'</td></tr>';
        tweetsDiv.appendChild(tweet);
      }
    }
    });
}

(function() {
    var keys = { up: 87, down: 83, left: 65, right: 68,
                 refresh: 190,
                 pageUp: 38, pageDown: 40, pageLeft: 37, pageRight: 39};
    addEvent(document, "keydown", function(e) {

		// get key press in cross browser way
		var code = e.which || e.keyCode;
		switch(code) {
			case keys.refresh:
				refresh();
				break;
			default:
				increment = 0;
				break;
		}
    });
    try {
        refresh();
    }catch(err) {
        alert(err);
    }
})();