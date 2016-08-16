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
	colorlist(o);
	color(style2color(o.val()));
}
function sidecolor(color){
	var data = {fixtures:[],color:color,dimmer:255,fade:0};
	console.log(color);
	$.each( $("#fixture>button.active"), function() {
    	data.fixtures.push(this.id);
	});
	if(data.fixtures.length>0){
		$.ajax({
		  type: "POST",
	      url: "/override",
	      data: JSON.stringify(data),
	      contentType: 'application/json'
	    });
	}
}
function parcolor(){
	var o = $("#parcolor");
	colorlist(o);
	sidecolor(style2color(o.val()));
}

function colorlist(o){
	o.removeClass("red");o.removeClass("green");o.removeClass("blue");
	o.removeClass("violet");o.removeClass("cyan");o.removeClass("yellow");
	o.removeClass("orange");o.removeClass("white");o.removeClass("black");
    o.addClass(o.val());
}
function style2color(style){
	switch (style) {
    	case "red": return "ROUGE";
    	case "green": return "VERT";
    	case "blue": return "BLEU";
    	case "violet": return "MAUVE";
    	case "cyan": return "CYAN";
    	case "yellow":  return "JAUNE";
    	case "orange": return "AMBRE";
    	case "white": return "WHITE";
    	case "black": return "BLACK";
   }
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
$("#mastercolor").val("");
$("#mastercolor").addClass("");
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

$("#fixture>button").click(function(){
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

function overstrob(){
	var btn = $("#overstrob");
	if(btn.hasClass("active")) btn.removeClass("active");
	else btn.addClass("active");
}



