package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.BitSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable, KeyListener{
	static Grid grid = new Grid();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	int size = 1;
	BitSet keySet = new BitSet(256);
	static long time = 0;
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
		g.setColor(Color.BLACK);
		long t0 = System.nanoTime();
		Color[][] a = grid.colors;
		int w = grid.width;
		int h = grid.height;
		g.fillRect(0, 0, w, h);
		if(a == null){
			return;
		}
//		if(keySet.get(KeyEvent.VK_SPACE)){



			int wh = w / 2;
			int hh = h / 2;
			Future<BufferedImage> f1 = executor.submit(new GraphicsWorker(a,size,0,0,wh,hh));
			Future<BufferedImage> f2 = executor.submit(new GraphicsWorker(a,size,w / 2,0,wh,hh));
			Future<BufferedImage> f3 = executor.submit(new GraphicsWorker(a,size,0,h / 2,wh,hh));
			Future<BufferedImage> f4 = executor.submit(new GraphicsWorker(a,size,w / 2,h / 2,wh,hh));

			BufferedImage b = null;
			BufferedImage b2 = null;
			BufferedImage b3 = null;
			BufferedImage b4 = null;

			try {
				b = f1.get();
				b2 = f2.get();
				b3 = f3.get();
				b4 = f4.get();

			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//		System.out.println(b);
			g.drawImage(b, 0, 0, this);
			g.drawImage(b2, w / 2, 0,this);
			g.drawImage(b3, 0, h / 2,this);
			g.drawImage(b4, w / 2, h / 2,this);
		/*}
		else{
			int x = 0;
			int y = 0;
					for(int c = 0; c < w;c++){
						for(int r = 0; r < h;r++){
							y += size;
							Color p = a[c][r];
							if(p == Color.BLACK){
								continue;
							}
							g.setColor(p);
							g.fillRect(x, y, size, size);
						}
						x += size;
						y = 0;
					}
		}*/
		time = System.nanoTime() - t0;
		g.setColor(Color.RED);
		g.drawString(time + " ", w / 2, h / 2);
	}

	@Override
	public void run() {
		addKeyListener(this);
		setFocusable(true);
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
	@Override
	public void keyTyped(KeyEvent e) {

	}
	@Override
	public void keyPressed(KeyEvent e) {
		keySet.set(e.getKeyCode(),true);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		keySet.set(e.getKeyCode(),false);
	}

}
