var shutter=37, dimmer=38, color=39, gobo=40, rotation=41, swivel=42, drum=43, fx=45, fxspeed=46, speed=25, depth=26;

function dmxwritten(){}

function dmxwrite(data){
	console.log(data);
	$.ajax({
		  type: "POST",
	      url: "/live/write",
	      data: JSON.stringify(data),
	      contentType: 'application/json',
	      cache: false,
	      success : function() {
	    	  dmxwritten();
	      }
		});
}


var wizard = {};

wizard.shutter = function(){
	var id = parseInt($("#shutterselect").val());
	var val = $("#shutterrange").val();
	$("#shutterrangelabel").html(val);
	
	if(id==16){
		id += Math.round(val * 115 / 100);
	}
	if(id==240){
		id += Math.round(val * 7/ 100);
	}
	
	var data={}; data[shutter]=id;
	dmxwrite(data); 
}

wizard.dimmer = function(){
	var val = $("#dimmerrange").val();
	$("#dimmerrangelabel").html(val);
	
	var data={}; data[dimmer]=Math.round(val*255/100);
	dmxwrite(data); 
}

wizard.color = function(id){
	var val = 0;
	if (typeof(id) === "undefined") {
		id = parseInt($("#colorselect").val());
		val = $("#colorrange").val();
	}else{
		$("#colorselect").val(id)
	}
	$("#colorrangelabel").html(val);
	
	if(id==193){
		id += Math.round(val * 25 / 100);
	}else if(id==220){
		id += Math.round(val * 23 / 100);
	}
	
	var data={}; data[color]=id;
	dmxwrite(data); 
}

wizard.gobo = function(id){
	var val = 0;
	if (typeof(id) === "undefined") {
		id = parseInt($("#goboselect").val());
		val = $("#goborange").val();
	}else{
		$("#goboselect").val(id)
	}
	$("#goborangelabel").html(val);
	
	if(id==191){
		id += Math.round(val * 52 / 100);
	}
	
	var data={}; data[gobo]=id;
	dmxwrite(data); 
}

wizard.rotation = function(){
	var id = parseInt($("#rotationselect").val());
	var val = $("#rotationrange").val();
	$("#rotationrangelabel").html(val);
	
	if(id==0){
		id += Math.round(val * 90 / 100);
	}else if(id==91){
		id += Math.round(val * 29 / 100);
	}else if(id==121){
		id += Math.round(val * 6 / 100);
	}else if(id==128){
		id += Math.round(val * 62 / 100);
	}else if(id==193){
		id += Math.round(val * 62 / 100);
	}
	
	var data={}; data[rotation]=id;
	dmxwrite(data);
}

wizard.swivel = function(){
	var id = parseInt($("#swivelselect").val());
	var val = $("#swivelrange").val();
	$("#swivelrangelabel").html(val);
	
	if(id==0){
		id += Math.round(val * 120 / 100);
	}else if(id==121){
		id += Math.round(val * 6 / 100);
	}else if(id==128){
		id += Math.round(val * 62 / 100);
	}else if(id==192){
		id += Math.round(val * 63 / 100);
	}
	
	var data={}; data[swivel]=id;
	dmxwrite(data);
}

wizard.drum = function(){
	var id = parseInt($("#drumselect").val());
	var val = $("#drumrange").val();
	$("#drumrangelabel").html(val);
	
	if(id==0){
		id += Math.round(val * 90 / 100);
	}else if(id==91){
		id += Math.round(val * 29 / 100);
	}else if(id==121){
		id += Math.round(val * 6 / 100);
	}else if(id==128){
		id += Math.round(val * 62 / 100);
	}else if(id==193){
		id += Math.round(val * 62 / 100);
	}
	
	var data={}; data[drum]=id;
	dmxwrite(data);
}

wizard.fx = function(){
	var id = $("#fxselect").val();
	var val = $("#fxrange").val();
	$("#fxrangelabel").html(val);
	var s = Math.round(10 + val * 245 / 100);
	if(s<11) s=0;
	var data={}; data[fx]=id; data[fxspeed]=s; dmxwrite(data); 
}

var strob = {};

strob.fire = function(){
	// send dmx signal
	s = $("#strobspeedrange").val() * 255 / 100;
	d = $("#strobdepthrange").val() * 255 / 100;
	dmxwrite({speed:s,depth:d}); 
	var data={}; data[speed]=s; data[depth]=d; dmxwrite(data); 
}

strob.init = function(speed, depth){
	// init sliders and button from dmx data
}

strob.reset = function(speed, depth){
	// reset sliders position
}

strob.toggle = function(speed, depth){
	// set/remove class active
}

/*
 * INIT
 */
var init = {};
init.done = function(){}
init.reset = function(){
	var data={}; 
	data[shutter]=0;
	data[dimmer]=0;
	data[color]=0;
	data[gobo]=0;
	data[rotation]=0;
	data[swivel]=0;
	data[drum]=0;
	data[fx]=0;
	data[fxspeed]=0;
	dmxwrite(data); 
}
init.start = function(){
	$.ajax({
		type: "POST",
	    url: "/live/read",
	    data: JSON.stringify([shutter,dimmer,color,gobo,rotation,swivel,drum,fx,fxspeed,speed,depth]), 
	    contentType: 'application/json',
		dataType : 'text',
		cache : false,
		success : function(data) {
			var o = JSON.parse(data);
			//console.log(o);
			
			// SHUTTER
			var id = o[shutter];
			var val = 0;
			if(id>16&&id<132){
				val = (id - 16) * 100 / 115;
				id = 16;
			}else if(id>240&&id<248){
				val = (id - 240) * 100 / 7;
				id = 240;
			}
			$("#shutterselect").val(id);
			$("#shutterrange").val(val);
			$("#shutterrangelabel").html(val);
			
			// DIMMER
			val = o[dimmer] * 100 / 255;
			$("#dimmerrange").val(val);
			$("#dimmerrangelabel").html(val);
			
			// COLOR
			var id = o[color];
			var val = 0;
			if(id>193&&id<219){
				val = (id - 193) * 100 / 25;
				id = 193;
			}else if(id>220&&id<244){
				val = (id - 220) * 100 / 23;
				id = 220;
			}
			$("#colorselect").val(id);
			$("#colorrange").val(val);
			$("#colorrangelabel").html(val);
			
			// GOBO
			var id = o[gobo];
			var val = 0;
			if(id>191&&id<244){
				val = (id - 191) * 100 / 52;
				id = 191;
			}
			$("#goboselect").val(id);
			$("#goborange").val(val);
			$("#goborangelabel").html(val);
			
			// ROTATION
			var id = o[rotation];
			var val = 0;
			if(id>0&&id<91){
				val = id * 100 / 90;
				id = 0;
			}else if(id>91&&id<121){
				val = (id - 91) * 100 / 29;
				id = 91;
			}else if(id>121&&id<128){
				val = (id - 121) * 100 / 6;
				id = 121;
			}else if(id>128&&id<193){
				val = (id - 128) * 100 / 62;
				id = 128;
			}else if(id>193){
				val = (id - 193) * 100 / 62;
				id = 193;
			}
			$("#rotationselect").val(id);
			$("#rotationrange").val(val);
			$("#rotationrangelabel").html(val);
			
			// SWIVEL
			var id = o[swivel];
			var val = 0;
			if(id>0&&id<121){
				val = id * 100 / 120;
				id = 0;
			}else if(id>121&&id<128){
				val = (id - 121) * 100 / 6;
				id = 121;
			}else if(id>128&&id<192){
				val = (id - 128) * 100 / 62;
				id = 128;
			}else if(id>192){
				val = (id - 192) * 100 / 63;
				id = 192;
			}
			$("#swivelselect").val(id);
			$("#swivelrange").val(val);
			$("#swivelrangelabel").html(val);
			
			// DRUM
			var id = o[drum];
			var val = 0;
			if(id>0&&id<91){
				val = id * 100 / 90;
				id = 0;
			}else if(id>91&&id<121){
				val = (id - 91) * 100 / 29;
				id = 91;
			}else if(id>121&&id<128){
				val = (id - 121) * 100 / 6;
				id = 121;
			}else if(id>128&&id<193){
				val = (id - 128) * 100 / 62;
				id = 128;
			}else if(id>193){
				val = (id - 193) * 100 / 62;
				id = 193;
			}
			$("#drumselect").val(id);
			$("#drumrange").val(val);
			$("#drumrangelabel").html(val);
			
			// FX
			$("#fxselect").val(o[fx]);
			var val = Math.round(((o[fxspeed]>10?o[fxspeed]-10:0)*100/245));
			$("#fxrange").val(val);
			$("#fxrangelabel").html(val);
			
			// STROB
			strob.init(o[speed],o[depth]);
			
			init.done();
		}.bind(this)
	});
}
/*
 * UNIT TEST
 */
var test = {id:0};

test.assert = function (name, expected){
	$.ajax({
		type: "POST",
	    url: "/live/read",
	    data: JSON.stringify([shutter,dimmer,color,gobo,rotation,swivel,drum,fx,fxspeed]), 
	    contentType: 'application/json',
		dataType : 'text',
		cache : false,
		success : function(str) {
			var actual = JSON.parse(str);
			var success=true;
			
			for(key in expected){
				if(expected[key]!=actual[key]) success=false;
			}
			
			if(success){
				console.log('TEST '+name+' OK');
			}else{
				console.log('TEST '+name+' KO');
				console.log(actual);
			}
			if (test.id<test.fn.length) test.id++;
			test.fn[test.id]();
		}.bind(this)
	});
}
test.assertui = function(id, val){
	if($("#"+id).val()!=val) console.log('#'+id+' = '+$("#"+id).val()+' <> '+val);
}

test.fn = [
	function(){
		dmxwritten=function(){
			init.done = function(){
				var data={}; data[shutter]=8;
				test.assert('shutter open',data);
				test.assertui('shutterselect',8);
				test.assertui('shutterrange',0);
			}
			init.start();
		}
		$("#shutterselect").val(8);
		$("#shutterrange").val(50);
		wizard.shutter();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[shutter]=16;
			test.assert('shutter 16 low',data);
		};
		$("#shutterselect").val(16);
		$("#shutterrange").val(0);
		wizard.shutter();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[shutter]=74;
			test.assert('shutter 16 mid',data);
		};
		$("#shutterselect").val(16);
		$("#shutterrange").val(50);
		wizard.shutter();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[shutter]=131;
			test.assert('shutter 16 high',data);
		};
		$("#shutterselect").val(16);
		$("#shutterrange").val(100);
		wizard.shutter();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[shutter]=240;
			test.assert('shutter 240 low',data);
		};
		$("#shutterselect").val(240);
		$("#shutterrange").val(0);
		wizard.shutter();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[shutter]=247;
			test.assert('shutter 240 high',data);
		};
		$("#shutterselect").val(240);
		$("#shutterrange").val(100);
		wizard.shutter();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[fxspeed]=0;
			test.assert('fx speed 0',data);
		};
		$("#fxrange").val(0);
		wizard.fx();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[fxspeed]=12;
			test.assert('fx speed 12',data);
		};
		$("#fxrange").val(1);
		wizard.fx();
	},
	function(){
		dmxwritten=function(){
			var data={}; data[fxspeed]=255;
			test.assert('fx speed 255',data);
		};
		$("#fxrange").val(100);
		wizard.fx();
	},
	function(){
		dmxwritten=function(){
			dmxwritten=function(){};
			init.done = function(){};
			init.start();
		}
		init.reset();
	}
];

test.run = function(){
	this.id=-1;
	
	dmxwritten=function(){
		var data={}; 
		data[shutter]=0;
		data[dimmer]=0;
		data[color]=0;
		data[gobo]=0;
		data[rotation]=0;
		data[swivel]=0;
		data[drum]=0;
		data[fx]=0;
		data[fxspeed]=0;
		test.assert('init',data);
	};
	init.reset(); 
}

if(location.hostname=='localhost') 
	test.run();
else
	init.start();


