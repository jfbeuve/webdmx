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

function man(){
	speed(0);
	speedlist("","");
}

var timestamp=0;
function tap(){
	var btn = $("#tap");
	if(btn.hasClass("active")){
		// tear down
		var time = Date.now() - timestamp;
		speed(time);
		btn.removeClass("active");
		//$("#speeddisplay").text(time + 'ms');
		speedlist(time,time+' ms');
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
        console.error('/speed/'+time, status, err.toString());
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
function washcolorchange(){
	var o = $("#washcolor");
	o.removeClass("red");o.removeClass("green");o.removeClass("blue");
	o.removeClass("violet");o.removeClass("cyan");o.removeClass("yellow");
	o.removeClass("orange");o.removeClass("white");o.removeClass("black");
	switch (o.val()) {
    	case "red": washcolor("ROUGE"); break;
    	case "green": washcolor("VERT"); break;
    	case "blue": washcolor("BLEU"); break;
    	case "violet": washcolor("MAUVE"); break;
    	case "cyan": washcolor("CYAN"); break;
    	case "yellow": washcolor("JAUNE"); break;
    	case "orange": washcolor("AMBRE"); break;
    	case "white": washcolor("WHITE"); break;
    	case "black": washcolor("BLACK"); break;
   }
   o.addClass(o.val());
}
function washcolor(color){
	var data = {
		fixtures: ['LEFT'],
		color: color,
		dimmer: 255,
		fade: 0
	};
	console.log("WASH "+color);
		$.ajax({
		  type: "POST",
	      url: "/override",
	      data: JSON.stringify(data),
	      contentType: 'application/json'
	    });
}
function speedlist(value, text){
	$("#speed").empty()
	.append('<option value="'+value+'">'+text+'</option>')
	.append('<option value="4000">4s</option>')
	.append('<option value="30000">30s</option>')
	.append('<option value="180000">3m</option>')
	.val(value);
}

//TODO INIT
$("#mastercolor").val("violet");
$("#mastercolor").addClass("violet");
$("#parcolor").val("");
speedlist("","");

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

