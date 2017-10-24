
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
		'colors':[],
		'col':{'r':0,'g':0,'b':0,'hex':'000000'},
		'rev':{'r':0,'g':0,'b':0,'hex':'000000'}
};

colormatrix.reset = function(){
	this.colors = ['FFFF00','FF0000','00FFFF','FF00FF','00FFFF','0000FF','000000','FFFFFF'];
};

colormatrix.reset();


if (typeof(localStorage.colormatrix) !== "undefined") colormatrix = JSON.parse(localStorage.colormatrix);

console.log(colormatrix);

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
	var c = {'r':this.col.r,'g':this.col.g,'b':this.col.b};
	
	if(c.r+c.g+c.b==0)return c;

	var val = Math.round(w*255/100);
	if(c.r==0) c.r=val; 
	if(c.g==0) c.g=val; 
	if(c.b==0) c.b=val;

	return c;
}

//returns reverse color after white filter
colormatrix.wrev = function (w){
	var c = {'r':this.rev.r,'g':this.rev.g,'b':this.rev.b};
	
	if(c.r+c.g+c.b==0)return c;
	
	var val = Math.round(w*255/100);
	if(c.r==0) c.r=val; 
	if(c.g==0) c.g=val; 
	if(c.b==0) c.b=val;
	
	return c;
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
		"wfront":15,"wback":0,"wdrum":15
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
	} else {
		wit.fcol = colormatrix.wcol(settings.wfront); // f=front
		wit.frev = colormatrix.wrev(settings.wfront); // f=front
		wit.dcol = colormatrix.wcol(settings.wdrum); // d=drum
		wit.drev = colormatrix.wrev(settings.wdrum); // d=drum
		wit.bcol = colormatrix.wcol(settings.wback); // b=back
		wit.brev = colormatrix.wrev(settings.wback); // b=back
	}
	
	return wit;
}

function witmatrix(color){
	
	var c = hexToRgb(color);
	
	var wf = Math.round(settings.wfront*255/100);
	var wd = Math.round(settings.wdrum*255/100);
	var wb = Math.round(settings.wback*255/100);
	
	var wit = {'front':{'r':(c.r==0?wf:c.r),'g':(c.g==0?wf:c.g),'b':(c.b==0?wf:c.b)},
			'drum':{'r':(c.r==0?wd:c.r),'g':(c.g==0?wd:c.g),'b':(c.b==0?wd:c.b)},
			'back':{'r':(c.r==0?wb:c.r),'g':(c.g==0?wb:c.g),'b':(c.b==0?wb:c.b)}
	};
	
	return wit;
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
	$('#dfminrange').val(this.dfmin);
	$('#dfminrangelabel').html('front D'+this.dfmin+'%');
	$('#wfrontrange').val(this.wfront);
	$('#wfrontrangelabel').html('front W'+this.wfront+'%');
	
	$('#ddrumrange').val(this.ddrum);
	$('#ddrumrangelabel').html('drum D'+this.ddrum+'%');
	$('#wdrumrange').val(this.wdrum);
	$('#wdrumrangelabel').html('drum W'+this.wdrum+'%');
	
	$('#dbackrange').val(this.dback);
	$('#dbackrangelabel').html('back D'+this.dback+'%');
	$('#wbackrange').val(this.wback);
	$('#wbackrangelabel').html('back W'+this.wback+'%');
	
	$('#dfmaxrange').val(this.dfmax);
	$('#dfmaxrangelabel').html('solo D'+this.dfmax+'%');
	
	$("#faderange").val(this.fade);
	$("#faderangelabel").html('fade '+this.fadeprint());
};

// apply slider change
settings.range = function(id){
	// set
	this[id]=$("#"+id+"range").val();
	
	//display
	this.display();
	
	// store
	localStorage.settings = JSON.stringify(this);

	// propagate 
	if(id!='fade'){
		this.scene();
	}
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
if(localStorage.solo!='') $('#'+localStorage.solo).addClass("active");

if (typeof(localStorage.speed) === "undefined") localStorage.speed = 300;

var factorycolors = ['ff0000','ff8000','ffff00','00ff00','00ffff','0000ff','ff00ff','ffffff', '000000'];
var customcolors = [];
if (typeof(localStorage.colors) === "undefined") localStorage.colors = JSON.stringify(customcolors);
customcolors = JSON.parse(localStorage.colors);

initdiv('colordiv');
initdiv('seqdiv');
initdiv('setdiv');
initdiv('soldiv');
initdiv('colrevdiv');
