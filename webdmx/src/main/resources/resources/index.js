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
	}
	override();
}

function solostrob(on) {
	console.log('solostrob '+on);
	override();
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
	//TODO POST /live/override
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
	var leadid = $("#leadid").val();
	var lead = $("#lead").hasClass("active");
	var strob = $("#colorstrob").hasClass("active");
	var dim = $("#colordim").val();
	//TODO POST /live/scene
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

var colors = ['ff8000','ffff00','00ffff','ff00ff', 'ffffff', '000000'];
if (typeof(localStorage.colors) === "undefined") localStorage.colors = JSON.stringify(colors);
colors = JSON.parse(localStorage.colors);
console.log(colors);

//TODO bind presets
// TODO bind colors

/*
 * COLORS
 */

function colorpresets(){
	var html = '';
	for (var i = 0; i < colors.length; i++) {
	    html = html + '<div id="'+colors[i]+'" style="background-color:#'+colors[i]+'" onclick="setcolor(\''+colors[i]+'\')"> </div>';
	}
	$("#colorpicker").html(html);
}
colorpresets();

function coloradd(){
	var c = $("#color").val().toLowerCase();
	var exists = false;
	for (var i = 0; i < colors.length; i++) {
		if(colors[i]==c) exists=true;
	}
	if(!exists)colors.push(c);
	localStorage.colors = JSON.stringify(colors);
	colorpresets();
}

function colordel(){
	var c = $("#color").val().toLowerCase();
	var newcolors = [];
	for (var i = 0; i < colors.length; i++) {
		if(colors[i]!=c||i<6) newcolors.push(colors[i]);
	}
	colors=newcolors;
	localStorage.colors = JSON.stringify(colors);
	colorpresets();
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

bindsolo('PAR1');
bindsolo('PAR2');
bindsolo('PAR3');
bindsolo('PAR4');
