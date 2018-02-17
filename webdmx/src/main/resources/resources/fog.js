var mftimer;
function togglemf(){
	if(localStorage.togglediv!='mfdiv') {
		mfget('');
		mftimer=window.setInterval("mfget('')",5000);
	}else{
		window.clearInterval(mftimer);
	}
	togglediv('mfdiv');
}
function mffog(){
	if($('#mffog').hasClass('active'))
		mfget('?fog=false');
	else
		mfget('?fog=true');
}
function mfauto(){
	if($('#mfauto').hasClass('active'))
		mfget('?auto=false');
	else
		mfget('?auto=true');
}
function mfget(qrystr){
	$.ajax({
		  type: "GET",
	      url: "/live/fog"+qrystr,
	      contentType: 'application/json',
	      cache: false,
	      success : function(data) {
	    	  console.log(data);
	    	  if(data.fog) $('#mffog').addClass('active');
	    	  if(!data.fog) $('#mffog').removeClass('active');
	    	  if(data.auto) $('#mfauto').addClass('active');
	    	  if(!data.auto) $('#mfauto').removeClass('active');
	    	  var ready = data.ready ? '*ready*' : '-busy-';
	    	  var fogtime = Math.round(data.fogtime / 100) /10;
	    	  var sleepunit = 's';
	    	  var sleeptime = Math.round(data.sleeptime / 100) /10;
	    	  if(data.sleeptime>60000){
	    		  sleepunit = 'm';
		    	  sleeptime = Math.round(data.sleeptime / 6000) /10;
	    	  }
	    	  if(data.sleeptime>3600000){
	    		  sleepunit = 'h';
		    	  sleeptime = Math.round(data.sleeptime / 360000) /10;
	    	  }
	    	  $('#mflabel').html(ready+' fog '+fogtime+'s / sleep '+sleeptime+sleepunit);
	      }
		});
}
if(localStorage.togglediv=='mfdiv') {
	mfget('');
	mftimer=window.setInterval("mfget('')",5000);
}