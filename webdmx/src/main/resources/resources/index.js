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
	localStorage.preset = '';
}
function stop() {
	get("/live/speed/-1");
	localStorage.preset = '';
}
function blackout() {
	get("/live/blackout/"+settings.fadems());
	localStorage.preset = '';
	
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
function solosnap(on) {
	console.log('solosnap '+on);
}

/**
 * POST override change
 */
function override(){
	var dim = settings.dfmax;
	var strob = $("#solostrob").hasClass("active");
	var solosnap = $("#solosnap").hasClass("active");
	var o = {"override":[],"reset":[],"fade":0,"layer":2};
		
	// SOLO ID
	var fsel = [false,false,false,false,false,false];
	if($('#PAR1').hasClass("active")){
		fsel[0]=true;
	}
	if($('#PAR2').hasClass("active")){
		fsel[1]=true;
		fsel[2]=true;
	}
	if($('#PAR3').hasClass("active")){
		fsel[4]=true;
		fsel[5]=true;
	}
	if($('#PAR4').hasClass("active")){
		fsel[3]=true;
	}
	var val = $('#solsel').val();
	if(val.length>0){
		fsel[val]=true;
	}
	for (var i = 0; i < fsel.length; i++) {
		if(fsel[i]){
			o.override.push({'id':i,'dim':dim,'r':255,'g':255,'b':255,'strob':strob});
		} else {
			o.reset.push(i);
		}
	}
	
	if(!solosnap){
		o.fade=settings.fadems();
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
 * COLOR EVENTS
 */
function color(){
	var c = $('#color').val();
	colormatrix.setcol(c);
	colormatrix.init();
	scene();
}
function setcolor(c){
	document.getElementById('color').jscolor.fromString(c);
	colormatrix.setcol(c);
	colormatrix.init();
	scene();	
}
function setrevcolor(c){
	colormatrix.setrev(c);
	colormatrix.init();
	scene();	
}

function colorstrob(on){
	scene();
}

/*
 * OVERLAYS
 */

function togglediv(name){
	
	if(localStorage.togglediv==''){
		$('#'+name).toggle();
		localStorage.togglediv=name;
		$('#'+name+'btn').addClass('active');
	}else if(localStorage.togglediv==name){
		$('#'+name).toggle();
		localStorage.togglediv='';
		$('#'+name+'btn').removeClass('active');
	}else{
		$('#'+localStorage.togglediv).toggle();
		$('#'+localStorage.togglediv+'btn').removeClass('active');
		$('#'+name).toggle();
		$('#'+name+'btn').addClass('active');
		localStorage.togglediv=name;
	}
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
	html = '';
	for (var i = 0; i < factorycolors.length; i++) {
	    html = html + '<div style="background-color:#'+factorycolors[i]+'" onclick="setrevcolor(\''+factorycolors[i]+'\')">'+factorycolors[i]+'</div>';
	}
	for (var i = 0; i < customcolors.length; i++) {
	    html = html + '<div style="background-color:#'+customcolors[i]+'" onclick="setrevcolor(\''+customcolors[i]+'\')">'+customcolors[i]+'</div>';
	}
	$("#colrevpicker").html(html);
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
		if(typeof(window[name]) === 'function') window[name](false);
	}else{
		// SWITCH ON
		btn.addClass("active");
		localStorage[name]=true;
		if(typeof(window[name]) === 'function') window[name](true);
	}
}

bind('next');
bind('stop');
bind('tap');
bind('blackout');
bindhold('solostrob');
bindhold('colorstrob');
bindhold('solosnap');
bindhold('yellowbtn');

bindsolo('PAR1');
bindsolo('PAR2');
bindsolo('PAR3');
bindsolo('PAR4');

function dmxwrite(data){
	$.ajax({
		  type: "POST",
	      url: "/live/write",
	      data: JSON.stringify(data),
	      contentType: 'application/json',
	      cache: false
		});
}
