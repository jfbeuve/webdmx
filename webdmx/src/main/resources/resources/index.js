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
		$("#speeddisplay").text(time + 'ms');
		$("#speed").val(time + 'ms');		
	}else{
		//tear up
		timestamp = Date.now();
		btn.addClass("active");
	}

}

function speedsel(){
	var time = $("#speed").val();
	if(time!="") speed(time);
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

function mastercolor(){
	var o = $("#mastercolor");
	o.removeClass("red");o.removeClass("green");o.removeClass("blue");
	o.removeClass("violet");o.removeClass("cyan");o.removeClass("yellow");
	o.removeClass("orange");o.removeClass("white");o.removeClass("black");
	switch (o.val()) {
    	case "red": color("ROUGE"); break;
    	case "green": color("VERT"); break;
    	case "blue": color("BLEU"); break;
    	case "violet": color("MAUVE"); break;
    	case "cyan": color("CYAN"); break;
    	case "yellow": color("JAUNE"); break;
    	case "orange": color("AMBRE"); break;
    	case "white": color("WHITE"); break;
    	case "black": color("BLACK"); break;
   }
   o.addClass(o.val());
}
function parcolor(){
	var o = $("#parcolor");
	o.removeClass("red");o.removeClass("green");o.removeClass("blue");
	o.removeClass("violet");o.removeClass("cyan");o.removeClass("yellow");
	o.removeClass("orange");o.removeClass("white");o.removeClass("black");
	switch (o.val()) {
    	case "red": sideColor("ROUGE"); break;
    	case "green": sideColor("VERT"); break;
    	case "blue": sideColor("BLEU"); break;
    	case "violet": sideColor("MAUVE"); break;
    	case "cyan": sideColor("CYAN"); break;
    	case "yellow": sideColor("JAUNE"); break;
    	case "orange": sideColor("AMBRE"); break;
    	case "white": sideColor("WHITE"); break;
    	case "black": sideColor("BLACK"); break;
   }
   o.addClass(o.val());
}

//TODO init mastercolor
$("#mastercolor").val("violet");
$("#mastercolor").addClass("violet");
$("#speed").val("");
$("#parcolor").val("");

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

$(".override>button").click(function(){
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
	$.each( $(".override>button.active"), function() {
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

