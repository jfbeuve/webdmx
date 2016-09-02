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
	get("/speed/-1");
	$("#speedsel").val("");
}

function tap(){
	get("/speed/0");
	$("#speedsel").val("");
}

function speedrange(){
	var time = $("#speedsel").val();
	var range = $("#speedrange").val();
	if(time!=""){
		if(range==0) range=1;
		time = time * range / 50; 
		get("/speed/"+time);
		printms($("#speedval"),time);
	}
}
function speedsel(){
	$("#speedrange").val(50);
	speedrange();
}
function fadesel(){
	var range = $("#faderange").val(50);
	faderange();
}
function faderange(){
	var time = $("#fadesel").val();
	var range = $("#faderange").val();
	 
	if(time!=""){
		time = Math.round(time * range / 50);
		get("/fade/"+time);
		printms($("#fadeval"),time);
	}
}
/**
 * print friendly time in millis
 */
function printms(o,v){
	var u = " ms";
	if(v>=1000){
		v = v / 1000;
		u=" s";
	}
	o.html(v+u);
}
/**
 * Apply master color change (style and ajax call) 
 */
function color(){
	var o = $("#color");
	colorlist(o);
	get('/color/'+style2color(o.val()));
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
    	case "auto": return "AUTO";
   }
}

/**
 * ajax post and style change on override fixture select/unselect 
 */
function fixture(o){
	var btn = $("#"+o.id);
	if(btn.hasClass("active")){
		 btn.removeClass("active");
		 get("/solo/"+o.id+"/-1");
	}
	else {
		var a = $("#fixture>button.active");
		if (a.length>0) {
			a.removeClass('active');
		}
		btn.addClass("active");
		solorange();
	}
}
function solorange(){
	var range = $("#solorange").val();
	var dim = Math.round(255 * range / 100);
	$("#solodimval").html(dim);
	var a = $("#fixture>button.active");
	if (a.length>0) {
		get("/solo/"+a.get(0).id+"/"+dim);
	}
}

function show(){
	var name = $("#show").val();
	if(name!="") get("/show/"+name);
	if(name=="blackout"){
		$("#speedsel").val("");
		var a = $("#fixture>button.active");
		if (a!=null) a.removeClass('active');
	}
}
function masterdim(o){
	var val = Math.round(o.value*255/100);
	get("/dim/"+val);
	$("#masterdimval").html(val);
}
