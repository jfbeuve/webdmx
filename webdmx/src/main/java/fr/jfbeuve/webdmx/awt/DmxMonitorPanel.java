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
		g.clearRect(0, 0, 500, 240);
	    g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    
	    // FRONT 1 GAUCHE
	    RGBD c = new RGB(data[1],data[2],data[3]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 80, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(50, 20, 100, 100);
	    
	    // FRONT 2 GAUCHE
	    c = new RGB(data[6],data[7],data[8]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 180, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(150, 20, 100, 100);
	    
	    // FRONT 3 DROITE
	    c = new RGB(data[11],data[12],data[13]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 280, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(250, 20, 100, 100);
	    
	    // FRONT4 DROITE
	    c = new RGB(data[16],data[17],data[18]).eval();
	    g.setColor(Color.black);
	    g.drawString(c.d+"%", 380, 17);
	    g.setColor(new Color(c.r,c.g,c.b));
	    g.fillRect(350, 20, 100, 100);
	    
	    // REAR GAUCHE
	    g.setColor(Color.black);
	    g.drawString((data[59]>0?"S":"")+data[58]*100/255+"%", 30, 137);
	    g.setColor(new Color(data[55],data[56],data[57]));
	    g.fillRect(0, 140, 100, 100);
	    
	    // DRUM GAUCHE
	    g.setColor(Color.black);
	    g.drawString((data[34]>0?"S":"")+data[36]*100/255+"%", 130, 137);
	    g.setColor(new Color(data[30],data[31],data[32]));
	    g.fillRect(100, 140, 100, 100);
	    
	    // REAR CENTRE
	    g.setColor(Color.black);
	    g.drawString((data[64]>0?"S":"")+data[63]*100/255+"%", 230, 137);
	    g.setColor(new Color(data[60],data[61],data[62]));
	    g.fillRect(200, 140, 100, 100);
	    
	    // DRUM DROITE
	    g.setColor(Color.black);
	    g.drawString((data[52]>0?"S":"")+data[54]*100/255+"%", 330, 137);
	    g.setColor(new Color(data[48],data[49],data[50]));
	    g.fillRect(300, 140, 100, 100);
	    
	    // REAR DROITE
	    g.setColor(Color.black);
	    g.drawString((data[69]>0?"S":"")+data[68]*100/255+"%", 430, 137);
	    g.setColor(new Color(data[65],data[66],data[67]));
	    g.fillRect(400, 140, 100, 100);
	  }
}
