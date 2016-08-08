function get(url){
	$.ajax({
      url: url,
      dataType: 'text',
      cache: false,
      success: function(data) {
      	console.log('GET '+url);
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
}

$('.btn').click(function(){
    if($(this).hasClass('active')){
        $(this).removeClass('active')
    } else {
        $(this).addClass('active')
    }
});

function show(){
	get('/show/run');
}
function strob(){
	get('/front/strob');
}
function blackout(){
	get('/show/blackout');
}
function tap(){
	get('/show/tap');
}

//{"fixtures":["PAR1"],"color":"ROUGE","dimmer":255,"fade":0}
var side = {
	fixtures: [],
	dimmer: 255,
	fade: 0
};

function sideColor(color){
	side.color = color;
	console.log(color);
	if(side.fixtures.length>0){
		//TODO Ajax call
	}
}

function sideFixture(fixture){
	console.log(fixture);
	
	if(side.fixtures.length>0){
		//TODO Ajax call
	}
}

