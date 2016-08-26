package fr.jfbeuve.webdmx.fixture;


public enum PARFixture{
	PAR1(5), PAR2(6), 
	PAR3(7), PAR4(8);
	
	private int channel;
	
	private PARFixture(int channel){
		this.channel = channel;
	}
	public int channel(){return channel;}
}
