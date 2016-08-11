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

var timestamp=0;
function tap(){
	var btn = $("#tap");
	if(btn.hasClass("active")){
		// tear down
		var time = Date.now() - timestamp;
		speed(time);
		btn.removeClass("active");
		$("#speed").text(time + 'ms');		
	}else{
		//tear up
		timestamp = Date.now();
		btn.addClass("active");
	}
}

function speed(time){
	$.ajax({
      url: '/speed/'+time,
      success: function(data) {
      	console.log('SPEED '+time);
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

$(".fixture>button").click(function(){
	console.log(this.id);
	var btn = $("#"+this.id);
	if(btn.hasClass("active")){
		 btn.removeClass("active");
		 $.ajax({
		  type: "POST",
	      url: "/override",
	      data: JSON.stringify({fixtures:[this.id]}),
	      contentType: 'application/json'
	    });
	}
	else btn.addClass("active");
});

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
	$.each( $(".fixture>button.active"), function() {
    	side.fixtures.push(this.id);
	});
	if(side.fixtures.length>0){
		$.ajax({
		  type: "POST",
	      url: "/override",
	      data: JSON.stringify(side),
	      contentType: 'application/json'
	    });
	}
}

