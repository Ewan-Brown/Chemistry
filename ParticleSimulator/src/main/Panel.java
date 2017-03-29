package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel implements ActionListener{
	Grid grid = new Grid();
	int size = 3;
	public static void main(String[] args){
		JFrame frame = new JFrame("PixelSimulator");
		Panel panel = new Panel();
		frame.add(panel);
		frame.setVisible(true);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while(true){
			panel.repaint();
			panel.update();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public Panel(){
		Timer t = new Timer(10, this);
		t.start();
	}
	public void update(){
		grid.update();
	}
	public void doMouse(){
		try{
		int pX = (int)(MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX());
		int pY = (int)(MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY());
		int x = (pX - 50) / size;
		int y = (pY - 60) / size;
		System.out.println(grid.getParticle(x, y));
		}catch(java.awt.IllegalComponentStateException e){
			//do nothing
			System.out.println("Illegal");
		}
	}
	public void paint(Graphics g){
		super.paint(g);

		ArrayList<ArrayList<ParticleAbstract>> a = grid.particles;
		int x = 50;
		int y = 50;
		for(int c = 0; c < a.size();c++){
			for(int r = 0; r < a.get(c).size();r++){
				y += size;
				g.setColor(a.get(c).get(r).color);
				g.fillRect(x, y, size, size);
//				g.setColor(Color.WHITE);
//				g.drawRect(x, y, size, size);
			}
			x += size;
			y = 50;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		doMouse();
	}
	
}
