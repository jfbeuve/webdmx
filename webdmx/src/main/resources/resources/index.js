function get(url){
	$.ajax({
      url: url,
      dataType: 'text',
      cache: false,
      success: function(data) {
      	console.log('GET '+url);
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
}

function show(){
	get('/show/run');
}
function strob(){
	get('/front/strob');
}
function blackout(){
	get('/show/blackout');
}
function tap(){
	get('/show/tap');
}

function fade(time){
	$.ajax({
      url: '/fade/'+time,
      success: function(data) {
      	console.log('FADE '+time);
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
}

function color(color){
	$.ajax({
      url: '/color/'+color,
      success: function(data) {
      	console.log('COLOR '+color);
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
}

$(":button,.override").click(function(){
	console.log(this.id);
	var btn = $("#"+this.id);
	if(btn.hasClass("active")){
		//TODO HTTP POST remove override
		 btn.removeClass("active");
	}
	else btn.addClass("active");
});

//{"fixtures":["PAR1"],"color":"ROUGE","dimmer":255,"fade":0}
var side = {
	fixtures: [],
	dimmer: 255,
	fade: 0
};

var front = {
	fixtures: [],
	dimmer: 255,
	fade: 0
};

function sideColor(color){
	side.color = color;
	console.log(color);
	side.fixtures = new Array();
	$.each( $(".active"), function() {
    	side.fixtures.push(this.id);
	});
	if(side.fixtures.length>0){
		$.ajax({
		  type: "POST",
	      url: "/override",
	      data: JSON.stringify(side),
	      contentType: 'application/json',
	      cache: false
	    });
	}
}
// http://api.jquery.com/jquery.getjson/
// var your_object = JSON.parse(json_text);
//var json_text = JSON.stringify(your_object, null, 2);

//TODO show color
//TODO fading time buttons + Ajax
//TODO init (show, strob, blackout, overrides)
//TODO side overrides
//TODO overrides dimmer
//TODO fade button hold/init

