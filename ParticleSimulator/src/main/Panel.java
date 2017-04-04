package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable{
	static Grid grid = new Grid();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	int size = 2;
	//MULTITHREAD PAINTING AND CALCULATIONS?
	public static void main(String[] args){
		Thread t1 = new Thread(grid);
		t1.start();
		JFrame frame = new JFrame("PixelSimulator");
		Panel panel = new Panel();
		frame.add(panel);
		Thread t2 = new Thread(panel);
		t2.start();
		frame.setVisible(true);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public Panel(){
	}
	public void doMouse(){
		try{
		int pX = (int)(MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX());
		int pY = (int)(MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY());
		int x = (pX - 50) / size;
		int y = (pY - 60) / size;
		System.out.println(grid.getParticle(x, y));
		}catch(java.awt.IllegalComponentStateException e){
			System.out.println("Illegal");
		}
	}
	public void paint(Graphics g){
		super.paint(g);
		Color[][] a = grid.colors;
		if(a == null){
			return;
		}
		int x = 50;
		int y = 50;
		int w = grid.width;
		int h = grid.height;
		Future<BufferedImage> f = executor.submit(new GraphicsWorker(a,size,0,0,w / 2,h));
		Future<BufferedImage> f2 = executor.submit(new GraphicsWorker(a,size,w / 2,0,w / 2,h));
		BufferedImage b = null;
		BufferedImage b2 = null;
		try {
			b = f.get();
			b2 = f2.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(b);
		g.drawImage(b, 0, 0, this);
		g.drawImage(b2, w / 2, 0,this);
		//TODO MORE CORESSS
//		Future<BufferedImage>[] futures = new Future<BufferedImage>[];
//		for(int c = 0; c < w;c++){
//			for(int r = 0; r < h;r++){
//				y += size;
//				Color p = a[c][r];
//				g.setColor(p);
//				g.fillRect(x, y, size, size);
//			}
//			x += size;
//			y = 50;
//		}
	}

	@Override
	public void run() {
		while(true){
			this.repaint();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
