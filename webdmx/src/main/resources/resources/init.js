
function initdiv(name){
	if (typeof(localStorage.togglediv) === "undefined") localStorage.togglediv = '';
	if(localStorage.togglediv==name) {
		$('#'+name).show();
		$('#'+name+'btn').addClass('active');
	} else 
		$('#'+name).hide();
}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

function rgbToHex(r, g, b) {
    return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
}

/*
 * COLOR 
 */
var colormatrix = {
		'colors':['FFFF00','FF0000','00FFFF','FF00FF','00FFFF','0000FF','000000','FFFFFF'],
		'col':{'r':0,'g':0,'b':0,'hex':'000000'},
		'rev':{'r':0,'g':0,'b':0,'hex':'000000'}
};

if (typeof(localStorage.colormatrix) !== "undefined") colormatrix = JSON.parse(localStorage.colormatrix);

console.log(colormatrix);

colormatrix.reset = function(){
	this.colors = ['FFFF00','FF0000','00FFFF','FF00FF','00FFFF','0000FF','000000','FFFFFF'];
};

// gets reverse color
colormatrix.get = function(){
	var revcolval = this.col.hex;
	var c = this.col;
	
	if(c.r>127&&c.g<128&&c.b<128) revcolval = this.colors[0]; 
	if(c.r>127&&c.g>127&&c.b<128) revcolval = this.colors[1]; // JAUNE/ORANGE
	if(c.r>127&&c.g<128&&c.b>127) revcolval = this.colors[2]; 
	if(c.r<128&&c.g>127&&c.b>127) revcolval = this.colors[3]; 
	if(c.r<128&&c.g<128&&c.b>127) revcolval = this.colors[4]; // BLEU
	if(c.r<128&&c.g>127&&c.b<128) revcolval = this.colors[5]; // VERT
	if(c.r<128&&c.g<128&&c.b<128) revcolval = this.colors[6];  
	if(c.r>127&&c.g>127&&c.b>127) revcolval = this.colors[7];

	// YELLOW OVERRIDE REV BLUE/GREEN/ORANGE
	if($('#yellowbtn').hasClass('active')){
		if(c.r<128&&c.g<128&&c.b>127) revcolval = 'FFFF00';
		if(c.r<128&&c.g>127&&c.b<128) revcolval = 'FFFF00';
		if(c.r>127&&c.g<192&&c.b<128) revcolval = 'FFFF00';
	}
	
	return revcolval;
};

// registers a different reverse color
colormatrix.setrev = function(rev){
	var c = this.col;
	
	if(c.r>127&&c.g<128&&c.b<128) this.colors[0] = rev; 
	if(c.r>127&&c.g>127&&c.b<128) this.colors[1] = rev;
	if(c.r>127&&c.g<128&&c.b>127) this.colors[2] = rev; 
	if(c.r<128&&c.g>127&&c.b>127) this.colors[3] = rev;
	if(c.r<128&&c.g<128&&c.b>127) this.colors[4] = rev;
	if(c.r<128&&c.g>127&&c.b<128) this.colors[5] = rev;
	if(c.r<128&&c.g<128&&c.b<128) this.colors[6] = rev;
	if(c.r>127&&c.g>127&&c.b>127) this.colors[7] = rev;
	
	this.rev.hex = rev;
	c = hexToRgb(rev);
	this.rev.r = c.r;
	this.rev.g = c.g;
	this.rev.b = c.b;
	
	localStorage.colormatrix = JSON.stringify(this);
};

// sets a different main color
colormatrix.setcol = function(col){
	
	this.col.hex = col;
	var c = hexToRgb(col);
	this.col.r = c.r;
	this.col.g = c.g;
	this.col.b = c.b;
	
	this.rev.hex = this.get();
	c = hexToRgb(this.rev.hex);
	this.rev.r = c.r;
	this.rev.g = c.g;
	this.rev.b = c.b;
	
	localStorage.colormatrix = JSON.stringify(this);
}

// returns main color after white filter
colormatrix.wcol = function (w){
	return witcalc({'r':this.col.r,'g':this.col.g,'b':this.col.b},w);
}

//returns reverse color after white filter
colormatrix.wrev = function (w){	
	return witcalc({'r':this.rev.r,'g':this.rev.g,'b':this.rev.b},w);
}

colormatrix.init = function(){
	$('#color').val(this.col.hex)
	$('#revcol').css('background-color','#'+this.rev.hex);
}

colormatrix.init();

/*
 * SETTINGS
 */	
settings = {
		"fade":40,
		"dfmin":20,"dfmax":100,"dback":100,"ddrum":100,
		"wfront":0,"wback":0,"wdrum":0,"scn":50,"scncfg":"all",
		"dlead":50,"wlead":20,"strbdim":100,"strbspeed":90,"sel":"dfmin",
		"wiz":{"color":0,"gobo":0,"speed":50,"fx":"music","position":60,"drum":45}
};

if (typeof(localStorage.settings) !== "undefined") settings = JSON.parse(localStorage.settings);

colormatrix.wit = function(){
	var wit = {};
	
	if($('#reversebtn').hasClass('active')){
		wit.frev = colormatrix.wcol(settings.wfront); // f=front
		wit.fcol = colormatrix.wrev(settings.wfront); // f=front
		wit.drev = colormatrix.wcol(settings.wdrum); // d=drum
		wit.dcol = colormatrix.wrev(settings.wdrum); // d=drum
		wit.brev = colormatrix.wcol(settings.wback); // b=back
		wit.bcol = colormatrix.wrev(settings.wback); // b=back
		wit.lrev = colormatrix.wcol(settings.wlead); // l=lead
		wit.lcol = colormatrix.wrev(settings.wlead); // l=lead
	} else {
		wit.fcol = colormatrix.wcol(settings.wfront); // f=front
		wit.frev = colormatrix.wrev(settings.wfront); // f=front
		wit.dcol = colormatrix.wcol(settings.wdrum); // d=drum
		wit.drev = colormatrix.wrev(settings.wdrum); // d=drum
		wit.bcol = colormatrix.wcol(settings.wback); // b=back
		wit.brev = colormatrix.wrev(settings.wback); // b=back
		wit.lrev = colormatrix.wrev(settings.wlead); // l=lead
		wit.lcol = colormatrix.wcol(settings.wlead); // l=lead
	}
	
	return wit;
}

function witmatrix(color){	
	var c = hexToRgb(color);

	return {'front':witcalc(c,settings.wfront),
			'drum':witcalc(c,settings.wdrum),
			'back':witcalc(c,settings.wback),
			'lead':witcalc(c,settings.wlead),
	};
}

/**
 * returns RGB color with white level applied 
 **/
function witcalc(rgb, w){
	if(rgb.r+rgb.g+rgb.b==0) return rgb;
	return {
		'r':Math.round((255*w/100)+(rgb.r*(100-w)/100)),
		'g':Math.round((255*w/100)+(rgb.g*(100-w)/100)),
		'b':Math.round((255*w/100)+(rgb.b*(100-w)/100))
	};
}

settings.fadeprint = function(){
		var v = 5000 * this.fade / 100;
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
		return v+u;
};

settings.fadems = function(){
	return 5000 * settings.fade / 100;
}

settings.scene = function(){};

// print settings 
settings.display = function(){
	var id = this.sel;
	$('#rangesel').val(id);
	$('#range').val(this[id]);
	
	if(id=='fade'){
		$('#rangelabel').html(this.fadeprint());
	}else{
		$('#rangelabel').html(this[id]+'%');
	}

	$("#scnrange").val(this.scn);
	$("#scnrangelabel").html(this.scn+'%');
	
	$("#scncfg").val(this.scncfg);
};

settings.select = function(){
	var id = $('#rangesel').val();
	this.sel=id;
	localStorage.settings = JSON.stringify(this);
	this.display();
};

// apply slider change
settings.range = function(id){
	//console.log('settings.range('+id+')');
	
	if (typeof(id) === "undefined"){
		id = this.sel;
		this[id]=$("#range").val();
	}else{
		this[id]=$("#"+id+"range").val()
	}
	
	//display
	this.display();
	
	// store
	localStorage.settings = JSON.stringify(this);
	//console.log(localStorage.settings);

	// propagate 
	if(id!='fade'){
		this.scene();
	}
};
settings.scnrange = function(){
	
	this[id]=$("#scnrange").val();
	
	//display
	this.display();
	
	// store
	localStorage.settings = JSON.stringify(this);

	// propagate 
	if(id!='fade'){
		this.scene();
	}
};

// set value from scncfg drop down list
settings.set = function(id){
	// set
	this[id]=$("#"+id).val();
	
	// store
	localStorage.settings = JSON.stringify(this);

	// propagate 
	this.scene();
};

settings.reset = function(){
	localStorage.clear();
	location.reload();
};

console.log(settings);
settings.display(); // init slider value

/*
 * INIT
 */

if (typeof(localStorage.solo) === "undefined") localStorage.solo = '';
if (typeof(localStorage.solocolor) === "undefined") localStorage.solocolor = 'FFFFFF';
if(localStorage.solo!='') $('#'+localStorage.solo).addClass("active");

if (typeof(localStorage.speed) === "undefined") localStorage.speed = 300;
$('#speed').html(localStorage.speed+'ms');

var factorycolors = ['ff0000','ff8000','ffff00','00ff00','00ffff','0000ff','ff00ff','ffffff', '000000'];
var customcolors = [];
if (typeof(localStorage.colors) === "undefined") localStorage.colors = JSON.stringify(customcolors);
customcolors = JSON.parse(localStorage.colors);

initdiv('colordiv');
initdiv('seqdiv');
initdiv('setdiv');
initdiv('soldiv');
initdiv('colrevdiv');
initdiv('wizdiv');
initdiv('mfdiv');
