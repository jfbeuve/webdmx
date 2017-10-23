/**
 * POST scene change
 */
function scene(){
	
	var strob = $("#colorstrob").hasClass("active");
	var w = colormatrix.wit();
	var fade = settings.fadems();
	
	var sc = {
		"fixtures":[
		{"id":0,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
		{"id":1,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
		{"id":2,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
		{"id":3,"dim":settings.dfmin,"r":w.fcol.r,"g":w.fcol.g,"b":w.fcol.b,"strob":false},
		{"id":4,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob},
		{"id":5,"dim":settings.ddrum,"r":w.drev.r,"g":w.drev.g,"b":w.drev.b,"strob":strob}],
		"fade":fade
	};
	
	// HTTP POST
	$.ajax({
		  type: "POST",
	      url: "/live/scene",
	      data: JSON.stringify(sc),
	      contentType: 'application/json',
	      cache: false
		});
}
settings.scene=function(){scene();};