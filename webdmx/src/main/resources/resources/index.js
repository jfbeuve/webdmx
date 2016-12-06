/*
 * HTTP
 */
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
/*
 * CONTROL
 */
function next() {
	get("/live/speed/0");
}
function stop() {
	get("/live/speed/-1");
}
function blackout() {
	get("/live/blackout/"+fade());
	
	// RESET COLOR
	setcolor('000000');
	
	// RESET SOLO
	var a = $("#fixture>button.active");
	if (a.length > 0) a.removeClass('active');
	localStorage.solo='';
}

var timestamp = 0;
function tap() {
	if (timestamp == 0)
		timestamp = Date.now();
	else {
		localStorage.speed = Date.now() - timestamp;
		get("/live/speed/"+localStorage.speed);
		timestamp = Date.now();
	}
}

/**
 * display and return fade time
 */
function fade() {
	localStorage.fade = $("#faderange").val();
	var time = 5000 * localStorage.fade / 100;
	printms($("#fadeval"), time);
	return time;
}

/**
 * print friendly for time in millis
 */
function printms(o, v) {
	var u = "ms";
	if (v==0) u = "s";
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
/*
 * OVERRIDE
 */
function solo(name) {
	var btn = $("#" + name);
	if (btn.hasClass("active")) {
		// RESET OLD OVERRIDE
		btn.removeClass("active");
		localStorage.solo='';
	} else {
		// SET NEW OVERRIDE
		var a = $("#fixture>button.active");
		if (a.length > 0) {
			// RESET OLD OVERRIDE
			a.removeClass('active');
		}
		btn.addClass("active");
		localStorage.solo=name;
		
		// disable lead
		if($("#lead").hasClass("active")) lead();
	}
	override();
}

function solostrob(on) {
	console.log('solostrob '+on);
	override();
}
function solosnap(on) {
	console.log('solosnap '+on);
}
function solodim(){
	localStorage.solodim = $("#solodim").val();
	override();
	$("#solodimval").html(localStorage.solodim+'%');
}
/**
 * POST override change
 */
function override(){
	var dim = $("#solodim").val();
	var strob = $("#solostrob").hasClass("active");
	var solosnap = $("#solosnap").hasClass("active");
	var o = {"override":[{"id":0,"dim":0,"r":0,"g":0,"b":0,"strob":false}],"reset":[],"fade":0,"layer":2};
		
	// SOLO ID
	var f = $("#fixture>button");
	for (var i = 0; i < f.length; i++) {
		if(f.hasClass("active")){
			o.override[0].id=i;
			o.override[0].dim=dim;
			o.override[0].r=255;
			o.override[0].g=255;
			o.override[0].b=255;
			o.override[0].strob = strob;
		} else {
			o.reset.push(i);
		}
	}
	
	if(!solosnap){
		o.fade=fade();
	}
	
	// HTTP POST
	$.ajax({
		  type: "POST",
	      url: "/live/override",
	      data: JSON.stringify(o),
	      contentType: 'application/json',
	      cache: false
		});
}
/*
 * SCENE
 */
function color(){
	localStorage.color=$("#color").val();
	scene();
}
function setcolor(c){
	document.getElementById('color').jscolor.fromString(c);
	color();	
}
function leadid(){
	localStorage.leadid=$("#leadid").val();
	scene();
}
function colorstrob(on){
	console.log('colorstrob '+on);
	scene();
}

function lead(on){
	console.log('lead '+on);
	if(on){
		// CANCEL SOLO
		var a = $("#fixture>button.active");
		if (a.length > 0) {
			solo(a[0].id);
		}
	}
	scene();
}

function colordim(){
	localStorage.colordim = $("#colordim").val();
	scene();
	$("#colordimval").html(localStorage.colordim+'%');
}


/**
 * POST scene change
 */
function scene(){
	var colorhex = $("#color").val();
	var c = hexToRgb(colorhex);
	
	var leadid = $("#leadid").val();
	var lead = $("#lead").hasClass("active");
	var strob = $("#colorstrob").hasClass("active");
	var dim = $("#colordim").val();
	
	var sc = {"fixtures":[{"id":0,"dim":0,"r":0,"g":0,"b":0,"strob":false},{"id":1,"dim":0,"r":0,"g":0,"b":0,"strob":false},{"id":2,"dim":0,"r":0,"g":0,"b":0,"strob":false},{"id":3,"dim":0,"r":0,"g":0,"b":0,"strob":false}],"fade":0};
	
	// BG COLOR, DIMMER, STROB
	for (var i = 0; i < sc.fixtures.length; i++) {
		sc.fixtures[i].r = c.r;
		sc.fixtures[i].g = c.g;
		sc.fixtures[i].b = c.b;
		sc.fixtures[i].dim = dim;
		sc.fixtures[i].strob = strob;
	}
	
	// FADE
	sc.fade = fade();
	
	// LEAD
	if(lead){
		sc.fixtures[leadid].r = 255;
		sc.fixtures[leadid].g = 255;
		sc.fixtures[leadid].b = 255;
		sc.fixtures[leadid].dim = dim/2;
		sc.fixtures[leadid].strob=false;
	}
	
	// HTTP POST
	$.ajax({
		  type: "POST",
	      url: "/live/scene",
	      data: JSON.stringify(sc),
	      contentType: 'application/json',
	      cache: false
		});
}

/*
 * DATA INIT
 */

if (typeof(localStorage.colordim) === "undefined") localStorage.colordim = 20;
$("#colordim").val(localStorage.colordim); $("#colordimval").html(localStorage.colordim+'%');

if (typeof(localStorage.solodim) === "undefined") localStorage.solodim = 100;
$("#solodim").val(localStorage.solodim); $("#solodimval").html(localStorage.solodim+'%');

if (typeof(localStorage.fade) === "undefined") localStorage.fade = 40;
$("#faderange").val(localStorage.fade); fade();

if (typeof(localStorage.solo) === "undefined") localStorage.solo = '';
if(localStorage.solo!='') $('#'+localStorage.solo).addClass("active");

if (typeof(localStorage.speed) === "undefined") localStorage.speed = 300;

if (typeof(localStorage.color) === "undefined") localStorage.color = '000000';
$('#color').val(localStorage.color);

if (typeof(localStorage.leadid) === "undefined") localStorage.leadid = 0;
$('#leadid').val(localStorage.leadid);

var factorycolors = ['ff8000','ffff00','00ffff','ff00ff', 'ffffff', '000000','ff4000','00ff40','0040ff'];
var customcolors = [];
if (typeof(localStorage.colors) === "undefined") localStorage.colors = JSON.stringify(customcolors);
localStorage.colors = JSON.stringify(customcolors);
customcolors = JSON.parse(localStorage.colors);

/*
 * PRESETS
 */

var presetnames = {'fchase':'1324','schase':'10302040','flash':'1/0','wchase':'W1234','bchase':'B1234'};
var presets = {};
$.ajax({
	url : '/live/sequence.json',
	dataType : 'text',
	cache : false,
	success : function(data) {
		presets = JSON.parse(data);
		var html = '';
		for(name in presets){
			var displayname = presetnames[name];
			if(typeof(displayname) === "undefined") displayname = name;
			html = html + '<button id="'+name+'" onclick="preset(\''+name+'\')">'+displayname+'</button>';
		}
		$("#presets").html(html);
	}.bind(this)
});

function preset(name){
	console.log(name);
	
	var p = presets[name];
	
	// override dimmmer for bchase, wchase, fire, flash with color dimmer
	if(name=='flash'||name=='wchase'||name=='bchase'||name=='fire'){
		var dim = $("#colordim").val();
		for (var step = 0; step < p.length; step++) {
			for (var fixture = 0; fixture < p[step].fixtures.length; fixture++) {
				p[step].fixtures[fixture].dim = dim;
			}
		}
	}
	console.log(p);
	
	// HTTP POST
	$.ajax({
		  type: "POST",
	      url: "/live/play",
	      data: JSON.stringify(p),
	      contentType: 'application/json',
	      cache: false
		});
}

/*
 * COLORS
 */

function colorpresets(){
	var html = '';
	for (var i = 0; i < factorycolors.length; i++) {
	    html = html + '<div id="'+factorycolors[i]+'" style="background-color:#'+factorycolors[i]+'" onclick="setcolor(\''+factorycolors[i]+'\')">'+factorycolors[i]+'</div>';
	}
	for (var i = 0; i < customcolors.length; i++) {
	    html = html + '<div id="'+customcolors[i]+'" style="background-color:#'+customcolors[i]+'" onclick="setcolor(\''+customcolors[i]+'\')">'+customcolors[i]+'</div>';
	}
	$("#colorpicker").html(html);
}
colorpresets();

function coloradd(){
	var c = $("#color").val().toLowerCase();
	var exists = false;
	for (var i = 0; i < factorycolors.length; i++) {
		if(factorycolors[i]==c) exists=true;
	}
	for (var i = 0; i < customcolors.length; i++) {
		if(customcolors[i]==c) exists=true;
	}
	if(!exists)customcolors.push(c);
	localStorage.colors = JSON.stringify(customcolors);
	colorpresets();
}

function colordel(){
	var c = $("#color").val().toLowerCase();
	var newcolors = [];
	for (var i = 0; i < customcolors.length; i++) {
		if(customcolors[i]!=c) newcolors.push(customcolors[i]);
	}
	customcolors=newcolors;
	localStorage.colors = JSON.stringify(customcolors);
	colorpresets();
}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

/*
 * EVENTS
 */

function bindsolo(name){
	$("#"+name).bind("tap", function(e) {
		solo(name);
	});
}

function bind(id){
	$("#"+id).bind("tap", function(e) {
		window[id]();
	});
}

function bindhold(id){
	var btn = $('#'+id); 
	// BIND TAP EVENT
	btn.bind("tap", function(e) {
		holdbtn(id);
	});
	// INITIALIZE LOCAL STORAGE
	if (typeof(localStorage[id]) === "undefined") localStorage[id] = false;
	if(localStorage[id]=='true') btn.addClass("active");
}

function holdbtn(name) {
	var btn = $("#"+name);
	if (btn.hasClass("active")){
		// SWITCH OFF
		btn.removeClass("active");
		localStorage[name]=false;
		window[name](false);
	}else{
		// SWITCH ON
		btn.addClass("active");
		localStorage[name]=true;
		window[name](true);
	}
}

bind('next');
bind('stop');
bind('tap');
bind('blackout');
bindhold('solostrob');
bindhold('colorstrob');
bindhold('lead');
bindhold('solosnap');

bindsolo('PAR1');
bindsolo('PAR2');
bindsolo('PAR3');
bindsolo('PAR4');
