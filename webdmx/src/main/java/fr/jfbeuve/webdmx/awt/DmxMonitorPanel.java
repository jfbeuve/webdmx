package fr.jfbeuve.webdmx.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DmxMonitorPanel extends JPanel {
	public DmxMonitorPanel(int[] _data){
		data = _data;
	}
	int[] data;
	public void paint(Graphics g) {
		g.clearRect(0, 0, 600, 120);
	    g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    
	    RGBD c = new RGB(data[1],data[2],data[3]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 30, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(0, 20, 100, 100);
	    
	    c = new RGB(data[6],data[7],data[8]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 130, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(100, 20, 100, 100);
	    
	    c = new RGB(data[11],data[12],data[13]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 230, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(200, 20, 100, 100);
	    
	    c = new RGB(data[16],data[17],data[18]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 330, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(300, 20, 100, 100);
	    
	    g.setColor(Color.black);
	    g.drawString((data[34]>0?"S":"")+data[36]*100/255+"%", 430, 17);
	    g.setColor(new Color(data[30],data[31],data[32]));
	    g.fillRect(400, 20, 100, 100);
	    
	    g.setColor(Color.black);
	    g.drawString((data[52]>0?"S":"")+data[54]*100/255+"%", 530, 17);
	    g.setColor(new Color(data[48],data[49],data[50]));
	    g.fillRect(500, 20, 100, 100);
	  }
}
