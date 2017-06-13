package fr.jfbeuve.webdmx.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DmxMonitor {
	private JFrame frame = null;
	private JPanel panel = null;
	public DmxMonitor(int[] data){
		System.setProperty("java.awt.headless", "false"); 
		frame = new JFrame();
	    frame.setTitle("webdmx");
	    frame.setSize(620, 160);
	    frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    panel = new JPanel(){
	    	public void paint(Graphics g) {
	    		g.clearRect(0, 0, 600, 120);
	    	    g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    	    
	    	    RGBD c = eval(new RGB(data[1],data[2],data[3]));
	    	    g.setColor(Color.black);
	    	    g.drawString(c.d+"%", 30, 17);
	    	    g.setColor(new Color(c.r,c.g,c.b));
	    	    g.fillRect(0, 20, 100, 100);
	    	    
	    	    c = eval(new RGB(data[6],data[7],data[8]));
	    	    g.setColor(Color.black);
	    	    g.drawString(c.d+"%", 130, 17);
	    	    g.setColor(new Color(c.r,c.g,c.b));
	    	    g.fillRect(100, 20, 100, 100);
	    	    
	    	    c = eval(new RGB(data[11],data[12],data[13]));
	    	    g.setColor(Color.black);
	    	    g.drawString(c.d+"%", 230, 17);
	    	    g.setColor(new Color(c.r,c.g,c.b));
	    	    g.fillRect(200, 20, 100, 100);
	    	    
	    	    c = eval(new RGB(data[16],data[17],data[18]));
	    	    g.setColor(Color.black);
	    	    g.drawString(c.d+"%", 330, 17);
	    	    g.setColor(new Color(c.r,c.g,c.b));
	    	    g.fillRect(300, 20, 100, 100);
	    	    
	    	    g.setColor(Color.black);
	    	    g.drawString(data[36]*100/255+"%", 430, 17);
	    	    g.setColor(new Color(data[30],data[31],data[32]));
	    	    g.fillRect(400, 20, 100, 100);
	    	    
	    	    g.setColor(Color.black);
	    	    g.drawString(data[54]*100/255+"%", 530, 17);
	    	    g.setColor(new Color(data[48],data[49],data[50]));
	    	    g.fillRect(500, 20, 100, 100);
	    	  }               
	    };
	    frame.setContentPane(panel); 
		frame.setVisible(true);
	}
	public void refresh(int[] data){
		panel.repaint();
	}

	public RGBD eval(RGB rgb){
		RGBD rgbd = new RGBD();
		
		int max = rgb.r;
		if(max<rgb.g)max=rgb.g;
		if(max<rgb.b)max=rgb.b;
		
		rgbd.r = rgb.r * 255 / max;
		rgbd.g = rgb.g * 255 / max;
		rgbd.b = rgb.b * 255 / max;
		rgbd.d = max * 100 / 255;
		
		return rgbd; 
	}
	
	public static void main(String args[]) throws Exception{
		int[] data = new int[512];
		
		data[1]=255;
		
		data[7]=255;
		
		data[13]=255;
		
		data[16]=255;
		data[17]=255;
		
		data[30]=255;
		data[32]=255;
		data[36]=255;
		
		data[49]=255;
		data[50]=255;
		data[54]=255;
		
		DmxMonitor m = new DmxMonitor(data);
		System.out.println("INIT");
		System.in.read();
		
		data[1]=128;
		m.refresh(data);
		System.out.println("RED #1 50%");
	}
}
