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
if (typeof(localStorage.pause) === "undefined") localStorage.pause = false;
function next() {
	if(localStorage.preset == '') return;
	get("/live/speed/0");
	localStorage.pause = true;
}

function stop() {
	get("/live/speed/-1");
	localStorage.pause = false;
	
	if(localStorage.preset=='solo'){
		localStorage.preset = '';
		$('#solochase').removeClass('active');
		override();
	} else {
		localStorage.preset = '';
	}
	
}

function blackout() {
	get("/live/blackout/"+settings.fadems());
	localStorage.preset = '';
	localStorage.pause = false;
	
	// RESET COLOR
	document.getElementById('color').jscolor.fromString('000000');
	colormatrix.setcol('000000');
	
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
		localStorage.pause = false;
		var speed = Date.now() - timestamp;
		if(speed<20000){
			localStorage.speed = speed;
			$('#speed').html(localStorage.speed+'ms');
			get("/live/speed/"+localStorage.speed);
		}
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
	var strob = $("#solostrob").hasClass("active");
	var solosnap = $("#solosnap").hasClass("active");
	var o = {"override":[],"reset":[],"fade":0,"layer":2};
	var w = witmatrix(localStorage.solocolor);
	var solochase = $('#solochase').hasClass('active');
	var scncfg = settings.scncfg;
		
	// SOLO ID
	var fsel = [false,false,false,false,false,false,false,false,false];
	var soloset = false;
	if($('#PAR1').hasClass("active")){
		fsel[0]=true;
		soloset=true;
	}
	if($('#PAR2').hasClass("active")){
		if(scncfg!='duo') fsel[1]=true;
		if(scncfg!='front') fsel[2]=true;
		soloset=true;
	}
	if($('#PAR3').hasClass("active")){
		if(scncfg=='all') fsel[4]=true;
		if(scncfg=='all') fsel[5]=true;
		if(scncfg=='front') fsel[2]=true;
		soloset=true;
	}
	if($('#PAR4').hasClass("active")){
		fsel[3]=true;
		soloset=true;
	}
	var val = $('#solsel').val();
	if(val.length>0){
		fsel[val]=true;
	}
	for (var i = 0; i < fsel.length; i++) {
		
		// FRONT COLOR
		var r = w.front.r;
		var g = w.front.g;
		var b = w.front.b;
		var d = settings.dfmax;
		
		// DRUM COLOR
		if(i>3){
			r = w.drum.r;
			g = w.drum.g;
			b = w.drum.b;
			d = settings.ddrum;
		}
		
		// REAR COLOR
		if(i>5){
			r = w.back.r;
			g = w.back.g;
			b = w.back.b;
			d = settings.dback;
		}
			
		if(fsel[i]){
			o.override.push({'id':i,'dim':d,'r':r,'g':g,'b':b,'strob':strob});
		} else {
			o.reset.push(i);
		}
	}
	
	if(!solosnap){
		o.fade=settings.fadems();
	}
	
	if(solochase&&soloset) {
		o = {"override":[],"reset":[0,1,2,3,4,5,6,7,8],"fade":0,"layer":2};
	} else {
		if(localStorage.preset=='solo') stop();
	}
	
	// HTTP POST
	$.ajax({
		  type: "POST",
	      url: "/live/override",
	      data: JSON.stringify(o),
	      contentType: 'application/json',
	      cache: false
		});
	
	if(solochase&&soloset) {
		presets.play('solo');
	} else if (localStorage.preset!= ''){
		presets.play(localStorage.preset);
	}
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
function setsolocolor(c){
	localStorage.solocolor = c;
	$('#solochase').removeClass('active');
	override();
}
function solochase(){
	override();
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
	html = '';
	for (var i = 0; i < factorycolors.length; i++) {
	    html = html + '<div style="background-color:#'+factorycolors[i]+'" onclick="setsolocolor(\''+factorycolors[i]+'\')">'+factorycolors[i]+'</div>';
	}
	for (var i = 0; i < customcolors.length; i++) {
	    html = html + '<div style="background-color:#'+customcolors[i]+'" onclick="setsolocolor(\''+customcolors[i]+'\')">'+customcolors[i]+'</div>';
	}
	$("#solocolor").html(html);
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
	
	// clear custom colors
	customcolors = [];
	localStorage.colors = JSON.stringify(customcolors);
	colorpresets();
	
	// clear custom reverse colors
	console.log(colormatrix);
	colormatrix.reset();
	colormatrix.setcol(colormatrix.col.hex);
	
	scene();
	
	// clear solo color
	localStorage.solocolor = 'FFFFFF';
	override();
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
bindhold('reversebtn');
bindhold('solochase');
bindhold('drscn');
bindhold('arscn');

bindsolo('PAR1');
bindsolo('PAR2');
bindsolo('PAR3');
bindsolo('PAR4');

function reversebtn(){
	scene();
}
function yellowbtn(){
	colormatrix.setcol(colormatrix.col.hex);
	scene();
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
