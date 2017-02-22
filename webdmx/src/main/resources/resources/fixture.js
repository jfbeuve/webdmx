function SwitchFixture (channel) {
    this.ch = channel;
    this.val = new Array(0,0,0,0);
    this.slow = function() {
        this.val = new Array(0,255,0,0);
    };
    this.all = function() {
        this.val = new Array(255,255,255,255);
    };
    this.none = function() {
        this.val = new Array(0,0,0,0);
    };
    this.set = function(a,b,c,d) {
        this.val = new Array(a,b,c,d);
    };
    this.dmx = function(data) {
        data[ch]=val[0];
        data[ch+1]=val[0+1];
        data[ch+2]=val[0+2];
        data[ch+3]=val[0+3];
    };
}
function StrobFixture (channel) {
    this.ch = channel;
    this.val = new Array(0,0);
    this.speed = function(v) {
        this.val[0]=v;
    };
    this.dimmer = function(v) {
        this.val[1]=v;
    };
    this.fire = function() {
        this.speed(225);
        this.dimmer(255);
    };
    this.disable = function() {
        this.val = new Array(0,0);
    };
    this.dmx = function(data) {
        data[ch]=val[0];
        data[ch+1]=val[0+1];
    };
}
function LedFixture (channel) {
    this.ch = channel;
    this.val = new Array(0,0,0,0,0);
    this.red = function(v) {
        this.val[1]=v;
    };
    this.green = function(v) {
        this.val[2]=v;
    };
    this.blue = function(v) {
        this.val[3]=v;
    };
    this.music = function(enable) {
        if(enable){
        	this.val[0]=255;
        	this.val[5]=255;
        }else{
        	this.val[0]=0;
        	this.val[5]=0;
        }
    };
    this.dmx = function(data) {
        data[ch]=val[0];
        data[ch+1]=val[0+1];
        data[ch+2]=val[0+2];
        data[ch+3]=val[0+3];
        data[ch+4]=val[0+4];
    };
}
var fxtled1 = LedFixture(0);
var fxtled2 = LedFixture(5);
var fxtled3 = LedFixture(10);
var fxtled4 = LedFixture(15);
var fxtsw = SwitchFixture(20);
var fxtstrob = StrobFixture(24); 
