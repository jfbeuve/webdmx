function get(url) {
	$.ajax({
		url : url,
		dataType : 'text',
		cache : false,
		success : function(data) {
			console.log('GET ' + url);
		}.bind(this)
	});
}

function shown(name) {
	$("#show").val(name);
	show();
}

function snapshow() {
	$("#speedsel").val("400");
	speedsel();
	shown('CHASEMIX');
}

function fadeshow() {
	if ($("#snap").hasClass("active"))
		snap();
	$("#speedsel").val("4000");
	speedsel();
	$("#fadesel").val("2000");
	fadesel();
	shown('CHASEMIX');
}

function man() {
	get("/speed/-1");
}

var timestamp = 0;
var speedtap = 0;
function tap() {
	get("/speed/0");
	if (timestamp == 0)
		timestamp = Date.now();
	else {
		$("#speedsel").val("");
		speedtap = Date.now() - timestamp;
		timestamp = Date.now();
		if (speedtap > 10000)
			speedtap = 0;
		else
			printms($("#speedval"), speedtap);
	}

}

function colorange() {
	time = colorval();
	printms($("#colorval"), time);
}

function colorval() {
	var range = $("#colorange").val();
	return 4000 + Math.round(176000 * range / 100);
}

function speedrange() {
	var time = $("#speedsel").val();
	var range = $("#speedrange").val();
	if (speedtap > 0 && time == "")
		time = speedtap;
	if (time != "") {
		if (range == 0)
			range = 1;
		time = time * range / 50;
		get("/speed/" + time);
		printms($("#speedval"), time);
	}
}

function strobospeed() {
	var range = $("#stroborange").val();
	var time = Math.round(20 + 80 * range / 100);
	get("/strobospeed/" + time);
	printms($("#stroboval"), time);
}

function speedsel() {
	$("#speedrange").val(50);
	speedrange();
}

function fadesel() {
	var range = $("#faderange").val(50);
	faderange();
}

function faderange() {
	var time = $("#fadesel").val();
	var range = $("#faderange").val();

	if (time != "") {
		time = Math.round(time * range / 50);
		get("/fade/" + time);
		printms($("#fadeval"), time);
	}
}

/**
 * print friendly time in millis
 */
function printms(o, v) {
	var u = "ms";
	if (v >= 1000) {
		v = v / 1000;
		u = "s";
		if (v >= 60) {
			v = Math.round(v * 100 / 60) / 100;
			u = "m";
		}
	}
	o.html(v + u);
}

/**
 * Apply master color change (style and ajax call)
 */
function color() {
	var o = $("#color");
	colorlist(o);
	if (o.val() == "")
		return;
	get('/color/' + style2color(o.val()));
}

function setcolor(c) {
	$("#color").val(c);
	color();
}

/**
 * Apply style change to a color list
 */
function colorlist(o) {
	o.removeClass("red");
	o.removeClass("green");
	o.removeClass("blue");
	o.removeClass("violet");
	o.removeClass("cyan");
	o.removeClass("yellow");
	o.removeClass("orange");
	o.removeClass("white");
	o.removeClass("black");
	if (o.val() != null)
		o.addClass(o.val());
}

/**
 * returns webdmx color enum from css style
 */
function style2color(style) {
	switch (style) {
	case "red":
		return "ROUGE";
	case "green":
		return "VERT";
	case "blue":
		return "BLEU";
	case "violet":
		return "MAUVE";
	case "cyan":
		return "CYAN";
	case "yellow":
		return "JAUNE";
	case "orange":
		return "AMBRE";
	case "white":
		return "WHITE";
	case "black":
		return "BLACK";
	case "auto":
		return "AUTO";
	}
}

/**
 * ajax post and style change on override fixture select/unselect
 */
function fixture(name) {
	var btn = $("#" + name);
	if (btn.hasClass("active")) {
		btn.removeClass("active");
		get("/solo/" + name + "/-1/false");
	} else {
		var a = $("#fixture>button.active");
		if (a.length > 0) {
			a.removeClass('active');
		}
		btn.addClass("active");
		solorange();
	}
}

function bgblack() {
	var btn = $("#bgblack");
	if (btn.hasClass("active")) {
		btn.removeClass("active");
		get("/bgblack/false");
	} else {
		btn.addClass("active");
		get("/bgblack/true");
	}
}

function snap() {
	var btn = $("#snap");
	if (btn.hasClass("active")) {
		btn.removeClass("active");
		faderange();
	} else {
		btn.addClass("active");
		get("/fade/0");
	}
}

function solostrob() {
	var btn = $("#solostrob");
	if (btn.hasClass("active"))
		btn.removeClass("active");
	else
		btn.addClass("active");
	solorange();
}

function autocolor() {
	var btn = $("#autocolor");
	if (btn.hasClass("active")){
		btn.removeClass("active");
		get('/autocolor/-1');
	}else{
		btn.addClass("active");
		get('/autocolor/'+colorval());
	}
}
function autocolorupd() {
	var btn = $("#autocolor");
	if (btn.hasClass("active")){
		get('/autocolor/'+colorval());
	}
}

function solorange() {
	var range = $("#solorange").val();
	var dim = Math.round(255 * range / 100);
	var strob = $("#solostrob").hasClass("active");
	$("#solodimval").html(dim);
	var a = $("#fixture>button.active");
	if (a.length > 0) {
		get("/solo/" + a.get(0).id + "/" + dim + "/" + strob);
	}
}

function show() {
	var name = $("#show").val();
	if (name != "")
		get("/show/" + name);
	if (name == "blackout") {
		$("#speedsel").val("");
		speedtap = 0;
		var a = $("#fixture>button.active");
		if (a != null)
			a.removeClass('active');
	}
}

function masterdim(o) {
	var val = Math.round(o.value * 255 / 100);
	get("/dim/" + val);
	$("#masterdimval").html(val);
}

function setcolortap(name){
	$("#"+name).bind("tap", function(e) {
		setcolor(name);
	});
}

function setfixturetap(name){
	$("#"+name).bind("tap", function(e) {
		fixture(name);
	});
}

$("#man").bind("tap", function(e) {
	man();
});

$("#tap").bind("tap", function(e) {
	tap();
});

$("#blackout").bind("tap", function(e) {
	shown('blackout');
});

$("#flash").bind("tap", function(e) {
	shown('FLASH');
});

$("#snapshow").bind("tap", function(e) {
	snapshow();
});

$("#fadeshow").bind("tap", function(e) {
	fadeshow();
});

setcolortap('red');
setcolortap('green');
setcolortap('blue');
setcolortap('violet');
setcolortap('yellow');
setcolortap('cyan');
setcolortap('orange');
setcolortap('white');
setcolortap('black');

$("#bgblack").bind("tap", function(e) {
	bgblack();
});

$("#autocolor").bind("tap", function(e) {
	autocolor();
});

setfixturetap('PAR1');
setfixturetap('PAR2');
setfixturetap('PAR3');
setfixturetap('PAR4');

$("#solostrob").bind("tap", function(e) {
	solostrob();
});

$("#snap").bind("tap", function(e) {
	snap();
});

