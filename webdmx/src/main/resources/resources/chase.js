/*
 * PRESETS
 */

if (typeof(localStorage.preset) === "undefined") localStorage.preset = '';

var presets = {};

presets.print = function(){
	var html = '';
	
	var fnames = Object.getOwnPropertyNames(this).filter(function (p) {
		if (typeof presets[p] !== 'function') return false;
		var ignore = ['play','print','solo','refresh','apply'];
		for(var i=0;i<ignore.length;i++){
			if(p==ignore[i]) return false;
		}
	    return true;
	});
	
	for(var i = 0; i < fnames.length; i++){
		html = html + '<button onclick="presets.play(\''+fnames[i]+'\')">'+fnames[i]+'</button>';
	}
	
	$("#presets").html(html);
}

// calling from scene to refresh scene without changing speed
presets.refresh = function() {
	if(localStorage.preset!='') this.apply(localStorage.preset);
}

// calling presets resuming speed
presets.play = function(name){
	localStorage.pause = false;
	this.apply(name);
}

presets.apply = function(name){
	console.log(name);
	
	if(localStorage.preset=='solo'&&name!='solo'){
		$('#PAR1').removeClass("active");
		localStorage['PAR1']=false;
		$('#PAR2').removeClass("active");
		localStorage['PAR2']=false;
		$('#PAR3').removeClass("active");
		localStorage['PAR3']=false;
		$('#PAR4').removeClass("active");
		localStorage['PAR4']=false;
		$('#solochase').removeClass("active");
		localStorage.solochase=false;
	}
	
	localStorage.preset = name;
	
	var p = this[name]();
	
	if(localStorage.pause=='true') p.speed=0
	
	$.ajax({
		  type: "POST",
	      url: "/live/play",
	      data: JSON.stringify(p),
	      contentType: 'application/json',
	      cache: false
		});
}

presets.dim = function(){
	var p = {"scenes":[],"speed":localStorage.speed};
	var w = colormatrix.wit();
	
	//if(!$('#PAR1').hasClass("active")){
		p.scenes.push({"fixtures":[
					{"id":0,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
					{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
					{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
					{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
					{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
					{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
					{"id":6,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
					{"id":7,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
					{"id":8,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
				],"fade":0});
	//}
	//if(!$('#PAR2').hasClass("active")){
		p.scenes.push({"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
			{"id":6,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0});
	//}
	//if(!$('#PAR4').hasClass("active")){
		p.scenes.push({"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
			{"id":6,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0});
	//}	
	return p;
}

presets.col = function(){
	var w = colormatrix.wit();
	
	var p = {"scenes":[
		{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false}
		],"fade":0}],"speed":localStorage.speed};
	
	return p;
}

presets.swap = function(){
	var w = colormatrix.wit();
	
	var p = {"scenes":[
		{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":true},
			{"id":6,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":true},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":true},
			{"id":6,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false}
		],"fade":0}],"speed":localStorage.speed};
	
	return p;
}

presets.flash = function(){
	var w = colormatrix.wit();
	
	var p = {"scenes":[
		{"fixtures":[
			{"id":0,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":6,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmax,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":6,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":0,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":settings.dfmax,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":1,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":2,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":3,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":7,"dim":0,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":8,"dim":0,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":1,"dim":settings.dfmax,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":2,"dim":settings.dfmax,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":3,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":6,"dim":0,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":8,"dim":0,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":1,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":2,"dim":0,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":3,"dim":settings.dfmax,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":6,"dim":0,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":7,"dim":0,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false}
		],"fade":0}],"speed":80};
	
	return p;
}

presets.wave = function(){
	var w = colormatrix.wit();
	
	var p = {"scenes":[
		{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin/2,"r":255,"g":255,"b":255,"strob":false},
			{"id":2,"dim":settings.dfmin/2,"r":255,"g":255,"b":255,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":false}
		],"fade":4000},{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":1,"dim":settings.dfmin/2,"r":255,"g":255,"b":255,"strob":false},
			{"id":2,"dim":settings.dfmin/2,"r":255,"g":255,"b":255,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":false},
			{"id":4,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":5,"dim":settings.ddrum,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":false},
			{"id":6,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":7,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false},
			{"id":8,"dim":settings.dback,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":false}
		],"fade":4000}],"speed":16000};
	
	return p;
}

presets.fade = function(){
	var w = colormatrix.wit();
	
	var p = {"scenes":[
		{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}
		],"fade":400},{"fixtures":[
			{"id":0,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}
		],"fade":400}],"speed":400};
	
	return p;
}

presets.chase = function(){
	var w = colormatrix.wit();
	
	var p = {"scenes":[
		{"fixtures":[
			{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}
		],"fade":0},{"fixtures":[
			{"id":0,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":1,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":2,"dim":0,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
			{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}
		],"fade":0}],"speed":localStorage.speed};
	
	return p;
}

function rainbstep(p, c1, c2, c3){
	var w = witmatrix(c1);
	var i = p.scenes.length;
	
	p.scenes.push({"fixtures":[],"fade":4000});
	p.scenes[i].fixtures.push({"id":0,"dim":settings.dfmin,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
	p.scenes[i].fixtures.push({"id":4,"dim":settings.ddrum,"r":w.drum.r,"g":w.drum.g,"b":w.drum.b,"strob":false});
	p.scenes[i].fixtures.push({"id":6,"dim":settings.dback,"r":w.back.r,"g":w.back.g,"b":w.back.b,"strob":false});

	w = witmatrix(c2); 
	p.scenes[i].fixtures.push({"id":1,"dim":settings.dfmin,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
	p.scenes[i].fixtures.push({"id":2,"dim":settings.dfmin,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
	p.scenes[i].fixtures.push({"id":7,"dim":settings.dback,"r":w.back.r,"g":w.back.g,"b":w.back.b,"strob":false});
	
	w = witmatrix(c3); 
	p.scenes[i].fixtures.push({"id":3,"dim":settings.dfmin,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
	p.scenes[i].fixtures.push({"id":5,"dim":settings.ddrum,"r":w.drum.r,"g":w.drum.g,"b":w.drum.b,"strob":false});
	p.scenes[i].fixtures.push({"id":8,"dim":settings.dback,"r":w.back.r,"g":w.back.g,"b":w.back.b,"strob":false});
};

presets.rainb = function(){
	var p = {"scenes":[],"speed":8000};
	
	rainbstep(p,'FF0000','FF8000','FFFF00'); // STEP 1 : ROUGE, ORANGE, JAUNE
	rainbstep(p,'FF8000','FFFF00','00FF00'); // STEP 2 : ORANGE, JAUNE, VERT
	rainbstep(p,'FFFF00','00FF00','00FFFF'); // STEP 3 : JAUNE, VERT, CYAN
	rainbstep(p,'00FF00','00FFFF','0000FF'); // STEP 4 : VERT, CYAN, BLEU
	rainbstep(p,'00FFFF','0000FF','FF00FF'); // STEP 5 : CYAN, BLEU, MAGENTA
	rainbstep(p,'0000FF','FF00FF','FF0000'); // STEP 6 : BLEU, MAGENTA, ROUGE
	rainbstep(p,'FF00FF','FF0000','FF8000'); // STEP 7 : MAGENTA, ROUGE, ORANGE
	
	return p;
}

function solostep(p, c){
	var w = witmatrix(c);
	var i = p.scenes.length;
	p.scenes.push({"fixtures":[],"fade":0});
	
	if($('#PAR1').hasClass("active")){
		p.scenes[i].fixtures.push({"id":0,"dim":settings.dfmax,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
		p.scenes[i].fixtures.push({"id":6,"dim":settings.dback,"r":w.back.r,"g":w.back.g,"b":w.back.b,"strob":false});
	}
	if($('#PAR2').hasClass("active")){
		p.scenes[i].fixtures.push({"id":1,"dim":settings.dfmax,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
		p.scenes[i].fixtures.push({"id":2,"dim":settings.dfmax,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
	}
	if($('#PAR3').hasClass("active")){
		p.scenes[i].fixtures.push({"id":4,"dim":settings.ddrum,"r":w.drum.r,"g":w.drum.g,"b":w.drum.b,"strob":false});
		p.scenes[i].fixtures.push({"id":5,"dim":settings.ddrum,"r":w.drum.r,"g":w.drum.g,"b":w.drum.b,"strob":false});
	}
	if($('#PAR4').hasClass("active")){
		p.scenes[i].fixtures.push({"id":3,"dim":settings.dfmax,"r":w.front.r,"g":w.front.g,"b":w.front.b,"strob":false});
		p.scenes[i].fixtures.push({"id":8,"dim":settings.dback,"r":w.back.r,"g":w.back.g,"b":w.back.b,"strob":false});
	}
}

presets.solo = function(){
	var p = {"scenes":[],"speed":80};
	
	solostep(p,'FF0000');
	solostep(p,'FF8000');
	solostep(p,'FFFF00');
	solostep(p,'00FF00');
	solostep(p,'00FFFF');
	solostep(p,'0000FF');
	solostep(p,'FF00FF');
	
	return p;
}

presets.print();


