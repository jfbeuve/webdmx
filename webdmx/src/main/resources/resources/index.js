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
	document.getElementById('color').jscolor.fromString('000000');
	localStorage.color=$("#color").val();
	
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
	o.html('fade ' + v + u);
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
		if($("#lead").hasClass("active")) holdbtn('lead');
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
	var o = {"override":[],"reset":[],"fade":0,"layer":2};
		
	// SOLO ID
	var f = $("#fixture>button");
	for (var i = 0; i < f.length; i++) {
		var btn = $('#'+f[i].id);
		if(btn.hasClass("active")){
			o.override.push({'id':i,'dim':dim,'r':255,'g':255,'b':255,'strob':strob});
		} else {
			o.reset.push(i);
		}
	}
	
	if(!solosnap){
		o.fade=fade();
	}
	
	autocolor(false);
	
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

function setrevcol(){
	var col = $("#color").val();
	col = getrevcol(col);
	$('#revcol').css('background-color','#'+col);
}

function getrevcol(col){
	var revcolval = col;
	var c = hexToRgb(revcolval);
	
	if(c.r>127&&c.g<128&&c.b<128) revcolval = 'FFFF00'; 
	if(c.r>127&&c.g>127&&c.b<128) revcolval = 'FF4000'; 
	if(c.r>127&&c.g<128&&c.b>127) revcolval = '00FFFF'; 
	if(c.r<128&&c.g>127&&c.b>127) revcolval = 'FF00FF'; 
	if(c.r<128&&c.g<128&&c.b>127) revcolval = '00FF40';
	if(c.r<128&&c.g>127&&c.b<128) revcolval = '0040FF';
	if(c.r<128&&c.g<128&&c.b<128) revcolval = 'FFFFFF';  
	if(c.r>127&&c.g>127&&c.b>127) revcolval = '000000';
	
	return revcolval;	
}

/**
 * POST scene change
 */
function scene(){
	setrevcol();
	
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
	
	autocolor(false);
	
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

if (typeof(localStorage.speed) === "undefined") localStorage.speed = 200;

if (typeof(localStorage.color) === "undefined") localStorage.color = '000000';
$('#color').val(localStorage.color); setrevcol();

if (typeof(localStorage.leadid) === "undefined") localStorage.leadid = 0;
$('#leadid').val(localStorage.leadid);

var factorycolors = ['ff8000','ffff00','00ffff','ff00ff','ff4000','00ff40','0040ff','ffff20','ffffff', '000000','ff0000','00ff00','0000ff'];
var customcolors = [];
if (typeof(localStorage.colors) === "undefined") localStorage.colors = JSON.stringify(customcolors);
customcolors = JSON.parse(localStorage.colors);

if (typeof(localStorage.disco) === "undefined") localStorage.disco = false;
if(localStorage.disco=='true') $('#disco').show(); else $('#disco').hide(); 

if (typeof(localStorage.strobdim) === "undefined") localStorage.strobdim = 100;
$("#strobdim").val(localStorage.strobdim); $("#strobdimval").html('dim '+localStorage.strobdim+'%');

if (typeof(localStorage.strobospeed) === "undefined") localStorage.strobospeed = 80;
$("#strobospeed").val(localStorage.strobospeed); $("#strobospeedval").html('speed '+localStorage.strobospeed+'%');

// INIT DISCO SWITCH AND STROB
$.ajax({
	type: "POST",
    url: "/live/read",
    data: JSON.stringify([1,6,11,16,21,22,23,24,25,26]), 
    contentType: 'application/json',
	dataType : 'text',
	cache : false,
	success : function(data) {
		//console.log(data);
		var o = JSON.parse(data);
		initdmxbtn(o['26'],$('#strob'));
		initdmxbtn(o['21'],$('#sw1'));
		initdmxbtn(o['22'],$('#sw2'));
		initdmxbtn(o['23'],$('#sw3'));
		initdmxbtn(o['24'],$('#sw4'));
		initdmxbtn(o['1'],$('#auto1'));
		initdmxbtn(o['6'],$('#auto2'));
		initdmxbtn(o['11'],$('#auto3'));
		initdmxbtn(o['16'],$('#auto4'));
	}.bind(this)
});

function initdmxbtn(val,btn){
	if(val>0) btn.addClass('active');
	else btn.removeClass('active');
}

/*
 * PRESETS
 */

var presetnames = {};
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
	
	var p = JSON.parse(JSON.stringify(presets[name]));
	
	// override dimmmer when required
	var dim = $("#colordim").val();
	
	for (var step = 0; step < p.scenes.length; step++) {
		for (var fixture = 0; fixture < p.scenes[step].fixtures.length; fixture++) {
			if(p.scenes[step].fixtures[fixture].dim==-1) p.scenes[step].fixtures[fixture].dim = dim;
		}
	}
	
	// override speed when required
	if(p.speed==-1) p.speed=localStorage.speed;
	
	// override colors when required
	if(name=='fire'||name=='wave'){
		var col = hexToRgb($("#color").val()); 
		var rev = hexToRgb(getrevcol($("#color").val()));
		p.scenes[0].fixtures[0].r = col.r;
		p.scenes[0].fixtures[0].g = col.g;
		p.scenes[0].fixtures[0].b = col.b;
		p.scenes[0].fixtures[2].r = col.r;
		p.scenes[0].fixtures[2].g = col.g;
		p.scenes[0].fixtures[2].b = col.b;
		p.scenes[1].fixtures[1].r = col.r;
		p.scenes[1].fixtures[1].g = col.g;
		p.scenes[1].fixtures[1].b = col.b;
		p.scenes[1].fixtures[3].r = col.r;
		p.scenes[1].fixtures[3].g = col.g;
		p.scenes[1].fixtures[3].b = col.b;
		p.scenes[1].fixtures[0].r = rev.r;
		p.scenes[1].fixtures[0].g = rev.g;
		p.scenes[1].fixtures[0].b = rev.b;
		p.scenes[1].fixtures[2].r = rev.r;
		p.scenes[1].fixtures[2].g = rev.g;
		p.scenes[1].fixtures[2].b = rev.b;
		p.scenes[0].fixtures[1].r = rev.r;
		p.scenes[0].fixtures[1].g = rev.g;
		p.scenes[0].fixtures[1].b = rev.b;
		p.scenes[0].fixtures[3].r = rev.r;
		p.scenes[0].fixtures[3].g = rev.g;
		p.scenes[0].fixtures[3].b = rev.b;
	}
	
	autocolor(false);
	
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

function colorclear(){
	customcolors = [];
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

/*
 * DISCO
 */ 
function disco(){
	$('#disco').toggle();
	localStorage.disco = localStorage.disco=='false';
}
function strobreset(){
	localStorage.strobdim = 100;
	$("#strobdim").val(localStorage.strobdim);
	$("#strobdimval").html('dim '+localStorage.strobdim+'%');

	localStorage.strobospeed = 90;
	$("#strobospeed").val(localStorage.strobospeed); 
	$("#strobospeedval").html('speed '+localStorage.strobospeed+'%');
}
function dmxwrite(data){
	$.ajax({
		  type: "POST",
	      url: "/live/write",
	      data: JSON.stringify(data),
	      contentType: 'application/json',
	      cache: false
		});
}
function strob(){
	var btn = $('#strob');
	if (btn.hasClass("active")) {
		btn.removeClass("active");
		dmxwrite({24:0,25:0}); 
	} else {
		btn.addClass("active");
		var speed = $("#strobospeed").val() * 255 / 100;
		var dim = $("#strobdim").val() * 255 / 100;
		dmxwrite({24:speed,25:dim}); 
	}
}

function switchall(fire){
	if(fire){
		$('#sw1').addClass("active");
		$('#sw2').addClass("active");
		$('#sw3').addClass("active");
		$('#sw4').addClass("active");
		dmxwrite({20:255,21:255,22:255,23:255}); 
	} else {
		$('#sw1').removeClass("active");
		$('#sw2').removeClass("active");
		$('#sw3').removeClass("active");
		$('#sw4').removeClass("active");
		dmxwrite({20:0,21:0,22:0,23:0}); 
	}
}
function switchx(vx){
	var btn = $('#sw'+vx);
	var dmx = vx + 19; 
	var data = {};
	if (btn.hasClass("active")) {
		btn.removeClass("active");
		 data[dmx]=0;
	} else {
		btn.addClass("active");
		data[dmx]=255;
	}
	dmxwrite(data);
}
function strobospeed(){
	localStorage.strobospeed = $("#strobospeed").val(); 
	$("#strobospeedval").html('speed '+localStorage.strobospeed+'%');
}
function strobdim(){
	localStorage.strobdim = $("#strobdim").val();
	$("#strobdimval").html('dim '+localStorage.strobdim+'%');
}
/*
 * AUTO
 */
function autocolor(enable){
	var nbactive = $("button.active[id^=auto]").length;
	if(enable&&nbactive==4) return;
	if(!enable&&nbactive==0) return;
	
	if(enable){
		$('#auto1').addClass("active");
		$('#auto2').addClass("active");
		$('#auto3').addClass("active");
		$('#auto4').addClass("active");
	} else {
		$('#auto1').removeClass("active");
		$('#auto2').removeClass("active");
		$('#auto3').removeClass("active");
		$('#auto4').removeClass("active");
	}
	sendautocolor();
}

function autocolorbtn(id){
	var btn = $('#auto'+id);
	if (btn.hasClass("active")) {
		btn.removeClass("active");
	} else {
		btn.addClass("active");
	}
	sendautocolor();
}
function sendautocolor(){
	var macro = 255;
	var speed=255;
	
	var data = {0:0,4:0,5:0,9:0,10:0,14:0,15:0,19:0};
	if($('#auto1').hasClass("active")) {
		data['0']=macro; data['4']=speed;
	} 
	if($('#auto2').hasClass("active")) {
		data['5']=macro; data['9']=speed;
	} 
	if($('#auto3').hasClass("active")) {
		data['10']=macro; data['14']=speed;
	} 
	if($('#auto4').hasClass("active")) {
		data['15']=macro; data['19']=speed;
	} 
	dmxwrite(data);
}
function slow(){
	autocolor(false);
	
	var btn = $("button.active[id^=sw]")
	for (var i = 0; i < btn.length; i++) {
		$('#'+btn[i].id).removeClass('active');
	}
	
	$('#sw3').addClass("active");
	$('#strob').removeClass('active');
	
	var data = {"fixtures":[{"id":0,"dim":10,"r":0,"g":0,"b":255,"strob":false},
		{"id":1,"dim":10,"r":0,"g":0,"b":255,"strob":false},
		{"id":2,"dim":10,"r":0,"g":0,"b":255,"strob":false},
		{"id":3,"dim":10,"r":0,"g":0,"b":255,"strob":false}],"fade":500};
	
	$.ajax({
		  type: "POST",
	      url: "/live/scene",
	      data: JSON.stringify(data),
	      contentType: 'application/json',
	      cache: false
		});
	dmxwrite({20:0,21:255,22:0,23:0}); 
}
