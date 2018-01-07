var wizard={};

/**
 *  change gobo
 **/
wizard.gobo = function(id){
	settings.wiz.gobo = id;
	if(settings.wiz.fx=='music') $("#wizfx").val('move');
	this.update();
}

/**
 *  change color
 **/
wizard.color = function(id){
	settings.wiz.color = id;
	if(settings.wiz.fx=='music') $("#wizfx").val('move');
	this.update();
}

/**
 *  update dmx values for wizard 
 **/
wizard.update = function(){
	// record ui values
	settings.wiz.speed=parseInt($("#wizspeed").val());
	settings.wiz.position=parseInt($("#wizpos").val());
	settings.wiz.fx=$("#wizfx").val();
	if(settings.wiz.position>0&&(settings.wiz.fx=='music'||settings.wiz.fx=='move')){
		$("#wizfx").val('rotate');
		settings.wiz.fx = 'rotate';
	}
	localStorage.settings = JSON.stringify(settings);
	
	// dmx values
	var shutter = 0, dimmer=255, color=0, gobo=0, drum=0, position=0, rotation=0, fxset=147, fx=0, fxspeed=0;
	if($("#wizon").hasClass('active')) {
		shutter=10;
		color=settings.wiz.color;
		gobo=settings.wiz.gobo;
		position=settings.wiz.position;
		fxspeed=Math.round(255*settings.wiz.speed/100);
		
		if(settings.wiz.fx=='music'){
			fx=133;
			position=0;
			color=0;
			gobo=0;
		}else if(settings.wiz.fx=='move'){
			fx=138;
			position=0;
		}else if(settings.wiz.fx=='strob'){
			shutter=110;
		}else if(settings.wiz.fx=='rotate'){
			rotation=Math.round(128+62*settings.wiz.speed/100);
		}else if(settings.wiz.fx=='drum'){
			drum=Math.round(193+62*settings.wiz.speed/100);
		}else if(settings.wiz.fx=='shake'){
			drum=Math.round(91+29*settings.wiz.speed/100);
		}else if(settings.wiz.fx=='wshake'){
			position=Math.round(192+63*settings.wiz.speed/100);
		}else if(settings.wiz.fx=='pulse'){
			shutter=180;
		}
	}
	
	//fire dmx
	dmxwrite({37:shutter, 38:dimmer, 39:color, 40:gobo, 41:drum, 42:position, 43:rotation, 44:fxset, 45:fx, 46:fxspeed});
}

/**
 *  init wizard ui components
 **/
wizard.init = function(){
	$("#wizspeed").val(settings.wiz.speed);
	$("#wizpos").val(settings.wiz.position);
	$("#wizfx").val(settings.wiz.fx);
}
wizard.init();

function wizon(){
	wizard.update();
}