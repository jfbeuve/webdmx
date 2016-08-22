function get(url){
	$.ajax({
      url: url,
      dataType: 'text',
      cache: false,
      success: function(data) {
      	console.log('GET '+url);
      }.bind(this)
    });
}

function man(){
	get("/speed/0");
	speedlist("","");
}

var timestamp=0;
function tap(){
	var btn = $("#tap");
	if(btn.hasClass("active")){
		// tear down
		var time = Date.now() - timestamp;
		get("/speed/"+time);
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
	if(time!="") get("/speed/"+time);
}
/**
 * Apply master color change (style and ajax call) 
 */
function mastercolor(){
	var o = $("#mastercolor");
	colorlist(o);
	get('/color/'+style2color(o.val()));
}
/**
 * Apply override color change (styles and ajax post)  
 */
function parcolor(){
	var o = $("#parcolor");
	colorlist(o);
	var color = style2color(o.val());
	var dimmer = $("#overdim").val()*255/100;
	var strob = $("#overstrob").hasClass("active");
	var data = {fixtures:[],color:color,dimmer:dimmer,fade:0,strob:strob};
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
/**
 * Apply style change to a color list 
 */
function colorlist(o){
	o.removeClass("red");o.removeClass("green");o.removeClass("blue");
	o.removeClass("violet");o.removeClass("cyan");o.removeClass("yellow");
	o.removeClass("orange");o.removeClass("white");o.removeClass("black");
    if(o.val()!=null) o.addClass(o.val());
}
/**
 * returns webdmx color enum from css style 
 */
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
/**
 * refreshes speed list
 * @param current custom tap speed 
 */
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

/**
 * ajax post and style change on override fixture select/unselect 
 */
function fixture(o){
	console.log(o.id);
	var btn = $("#"+o.id);
	if(btn.hasClass("active")){
		 btn.removeClass("active");
		 if(o.id=="LEFT") $("#overstrob").removeClass("active");
		 $.ajax({
		  type: "POST",
	      url: "/override",
	      data: JSON.stringify({fixtures:[o.id]}),
	      contentType: 'application/json'
	    });
	}
	else {
		btn.addClass("active");
		if($("#parcolor").val()!=null) parcolor();
	}
}

function overstrob(){
	var btn = $("#overstrob");
	if(btn.hasClass("active")) btn.removeClass("active");
	else btn.addClass("active");
	if($("#parcolor").val()!=null) parcolor();
}
function masterdim(o){
	get("/dim/"+o.value*255/100);
}
function blackout(){
	get('/show/blackout');
	speedlist("","");
	$("#parcolor").val("");
	colorlist($("#parcolor"));
	$("#overstrob").removeClass("active");
	$.each( $("#fixture>button.active"), function(o) {
    	$("#"+this.id).removeClass('active');
	});
}
function overdim(){
	if($("#parcolor").val()!=null) parcolor();
}
