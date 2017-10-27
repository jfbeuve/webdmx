/**
 * POST scene change
 */
function scene(){
	
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
	sc.fixtures.push({"id":1,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":2,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	sc.fixtures.push({"id":3,"dim":dim,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false});
	
	// DRUMS
	
	if(dr) dim = Math.round(settings.ddrum * settings.scn / 100)
	else if(ar) dim = 0;
	else dim = settings.ddrum;
	if(dr){
		sc.fixtures.push({"id":4,"dim":dim,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":strob});
		sc.fixtures.push({"id":5,"dim":dim,"r":w.dcol.r,"g":w.dcol.g,"b":w.dcol.b,"strob":strob});
	}else{
		sc.fixtures.push({"id":4,"dim":dim,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob});
		sc.fixtures.push({"id":5,"dim":dim,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob});		
	}
	// BACK
	if(ar) dim = Math.round(settings.dback * settings.scn / 100)
	else if(dr) dim = 0;
	else dim = settings.dback;
	sc.fixtures.push({"id":6,"dim":dim,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":strob});
	sc.fixtures.push({"id":7,"dim":dim,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":strob});
	sc.fixtures.push({"id":8,"dim":dim,"r":w.bcol.r,"g":w.bcol.g,"b":w.bcol.b,"strob":strob});	
	
	// HTTP POST
	$.ajax({
		  type: "POST",
	      url: "/live/scene",
	      data: JSON.stringify(sc),
	      contentType: 'application/json',
	      cache: false
		});
	
	presets.refresh();
}
settings.scene=function(){scene();};

function drscn(){
	scene();
}

function arscn(){
	scene();
}
