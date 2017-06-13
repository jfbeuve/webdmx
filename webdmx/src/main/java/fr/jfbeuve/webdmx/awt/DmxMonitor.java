package fr.jfbeuve.webdmx.awt;

import javax.swing.JFrame;

public class DmxMonitor {
	private JFrame frame = null;
	private DmxMonitorPanel panel = null;
	public DmxMonitor(int[] data){
		System.setProperty("java.awt.headless", "false"); 
		frame = new JFrame();
	    frame.setTitle("webdmx");
	    frame.setSize(620, 160);
	    frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    panel = new DmxMonitorPanel(data);
	    frame.setContentPane(panel); 
		frame.setVisible(true);
	}
	public void refresh(int[] data){
		panel.data = data;
		panel.repaint();
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
