/**
 * POST scene change
 */

var scn = {};

scn.all = function(){
	
	var strob = $("#colorstrob").hasClass("active");
	var w = colormatrix.wit();
	var fade = settings.fadems();
	
	var dr = $('#drscn').hasClass('active');
	var ar = $('#arscn').hasClass('active');
	
	var sc = {"fixtures":[],"fade":fade};
	
	// FRONT
	var dim = settings.dfmin;
	if(ar||dr) dim = 0;
	sc.fixtures.push({"id":0,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":3,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	if(dim > 0) dim = settings.dfmax;
	sc.fixtures.push({"id":9,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":12,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	
	// LEAD
	sc.fixtures.push({"id":1,"dim":dim*settings.dlead/100,"r":w.lcol.r,"g":w.lcol.g,"b":w.lcol.b,"strob":false});
	sc.fixtures.push({"id":2,"dim":dim*settings.dlead/100,"r":w.lcol.r,"g":w.lcol.g,"b":w.lcol.b,"strob":false});

	// DRUMS
	if(dr) dim = Math.round(settings.ddrum * settings.scn / 100)
	else if(ar) dim = 0;
	else dim = settings.ddrum;
	if(dr){
		sc.fixtures.push({"id":4,"dim":dim,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":strob});
		sc.fixtures.push({"id":5,"dim":dim,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":strob});
		sc.fixtures.push({"id":10,"dim":dim,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":strob});
		sc.fixtures.push({"id":11,"dim":dim,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":strob});
	}else{
		sc.fixtures.push({"id":4,"dim":dim,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob});
		sc.fixtures.push({"id":5,"dim":dim,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob});
		sc.fixtures.push({"id":10,"dim":dim,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob});
		sc.fixtures.push({"id":11,"dim":dim,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob});
	}
	// BACK
	if(ar) dim = Math.round(settings.dback * settings.scn / 100)
	else if(dr) dim = 0;
	else dim = settings.dback;
	sc.fixtures.push({"id":6,"dim":dim,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":strob});
	sc.fixtures.push({"id":7,"dim":dim,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":strob});
	sc.fixtures.push({"id":8,"dim":dim,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":strob});	
	
	return sc;
}

scn.front = function(){
	
	var strob = $("#colorstrob").hasClass("active");
	var w = colormatrix.wit();
	var fade = settings.fadems();
	
	var dr = $('#drscn').hasClass('active');
	var ar = $('#arscn').hasClass('active');
	
	var sc = {"fixtures":[],"fade":fade};
	
	// FRONT
	var dim = settings.dfmin;
	if(ar||dr) dim = 0;
	sc.fixtures.push({"id":0,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":strob});
	sc.fixtures.push({"id":3,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":strob});
	if(dim > 0) dim = settings.dfmax;
	sc.fixtures.push({"id":9,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":strob});
	sc.fixtures.push({"id":12,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":strob});
	
	// LEAD
	sc.fixtures.push({"id":1,"dim":dim,"r":w.lcol.r,"g":w.lcol.g,"b":w.lcol.b,"strob":strob});
	sc.fixtures.push({"id":10,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":strob});
	
	// DRUMS
	if(ar||dr)  dim = Math.round(settings.ddrum * settings.scn / 100)
	else dim = settings.ddrum;
	if(ar||dr){
		sc.fixtures.push({"id":2,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":strob});
		sc.fixtures.push({"id":11,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":strob});
	}else{
		sc.fixtures.push({"id":2,"dim":dim,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":strob});
		sc.fixtures.push({"id":11,"dim":dim,"r":w.frev.r,"g":w.frev.g,"b":w.frev.b,"strob":strob});
	}
	sc.fixtures.push({"id":4,"dim":0,"r":0,"g":0,"b":0,"strob":false});
	sc.fixtures.push({"id":5,"dim":0,"r":0,"g":0,"b":0,"strob":false});

	// BACK
	sc.fixtures.push({"id":6,"dim":0,"r":0,"g":0,"b":0,"strob":false});
	sc.fixtures.push({"id":7,"dim":0,"r":0,"g":0,"b":0,"strob":false});
	sc.fixtures.push({"id":8,"dim":0,"r":0,"g":0,"b":0,"strob":false});	
	
	return sc;
}

scn.duo = function(){
	
	var strob = $("#colorstrob").hasClass("active");
	var w = colormatrix.wit();
	var fade = settings.fadems();
	
	var dr = $('#drscn').hasClass('active');
	var ar = $('#arscn').hasClass('active');
	
	var sc = {"fixtures":[],"fade":fade};
	
	// FRONT
	var dim = settings.dfmin;
	sc.fixtures.push({"id":0,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":1,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}); // COPY #2
	sc.fixtures.push({"id":2,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":3,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false}); // COPY #0

	// RGBWAUV
	dim = settings.dfmax;
	sc.fixtures.push({"id":9,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":10,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":11,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":12,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});

	// DRUMS
	sc.fixtures.push({"id":4,"dim":0,"r":0,"g":0,"b":0,"strob":false});
	sc.fixtures.push({"id":5,"dim":0,"r":0,"g":0,"b":0,"strob":false});
	
	// BACK
    dim = settings.dback;
	sc.fixtures.push({"id":6,"dim":dim,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":strob});
	sc.fixtures.push({"id":7,"dim":dim,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":strob});
	sc.fixtures.push({"id":8,"dim":dim,"r":w.brev.r,"g":w.brev.g,"b":w.brev.b,"strob":strob});
	
	return sc;
}

function scene(){
	
	var sc = scn[settings.scncfg]();
	
	// HTTP POST
	$.ajax({
		  type: "POST",
	      url: "/live/scene",
	      data: JSON.stringify(sc),
	      contentType: 'application/json',
	      cache: false
		});
	
	// STROBO 
	if($("#colorstrob").hasClass("active")){
		dmxwrite({24:Math.round(255*settings.strbspeed/100), 25:Math.round(255*settings.strbdim/100)});
	}else{
		dmxwrite({24:0,25:0});
	}
	
	presets.refresh();
}
settings.scene=function(){scene();};

function drscn(){
	scene();
}

function arscn(){
	scene();
}


