package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.BitSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Particle.Element;

public class Panel extends JPanel implements Runnable, KeyListener, MouseListener{
	static Grid grid = new Grid();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	public static int size = 2;
	public BitSet keySet = new BitSet(256);
	static long time = 0;
	boolean isClicked = false;
	int yOffset = 0;
	int xOffset = 0;
	int selectedElement = 1;
	int[] cooldowns = new int[10];
	static int delay = 10;
	public static void main(String[] args){
		Reactions.init();
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
	public Panel(){}
	public void doMouse(){
		try{
			int pX = (int)(MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX());
			int pY = (int)(MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY());
			int x = (pX - 50) / size;
			int y = (pY - 60) / size;
		}catch(java.awt.IllegalComponentStateException e){
			System.out.println("Illegal");
		}
	}
	public void paint(Graphics g){
		super.paint(g);
		long t0 = System.nanoTime();
		Color[][] a = grid.colors;
		int w = grid.width;
		int h = grid.height;
		if(a == null){
			return;
		}
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
			e.printStackTrace();
		}
		g.drawImage(b, 0 + xOffset, 0 + yOffset, this);
		g.drawImage(b2, (w / 2) * size + xOffset, 0 + yOffset,this);
		g.drawImage(b3, 0 + xOffset, (h / 2) * size + yOffset,this);
		g.drawImage(b4, (w / 2) * size + xOffset, (h / 2) * size + yOffset,this);
		time = System.nanoTime() - t0;
		g.setColor(Color.RED);
		g.drawString(delay+"", (getWidth() / 2), (getHeight() / 2));
	}
	public void updateKeys(){
		for(int i = 0; i < cooldowns.length;i++){
			cooldowns[i]--;
		}
		if(keySet.get(KeyEvent.VK_W)){
			yOffset += size;
		}
		if(keySet.get(KeyEvent.VK_A)){
			xOffset += size;
		}		
		if(keySet.get(KeyEvent.VK_S)){
			yOffset -= size;
		}		
		if(keySet.get(KeyEvent.VK_D)){
			xOffset -= size;
		}
		if(keySet.get(KeyEvent.VK_1)){
			selectedElement = 0;
		}
		if(keySet.get(KeyEvent.VK_2)){
			selectedElement = 1;
		}
		if(keySet.get(KeyEvent.VK_3)){
			selectedElement = 2;
		}
		if(keySet.get(KeyEvent.VK_4)){
			selectedElement = 3;
		}
		if(cooldowns[0] < 0){
			cooldowns[0] = 10;
			if(keySet.get(KeyEvent.VK_COMMA)){
				if(delay > 150){
					delay -= 100;
				}
				else if(delay > 10){
					delay -= 5;
				}
				else{
					delay--;
				}
				if(delay < 1){
					delay = 1;
				}
			}

		}
		if(cooldowns[1] < 0){
			cooldowns[1] = 10;
			if(keySet.get(KeyEvent.VK_PERIOD)){
				if(delay > 150){
					delay += 100;
				}
				else if(delay > 10){
					delay += 5;
				}
				else{
					delay++;
				}
				if(delay > 1000){
					delay = 1000;
				}
			}
		}
	}
	@Override
	public void run() {
		addKeyListener(this);
		setFocusable(true);
		addMouseListener(this);
		while(true){
			this.repaint();
			updateKeys();
			if(isClicked){
				Point p = MouseInfo.getPointerInfo().getLocation();
				Point p2 = new Point();
				p2.x = p.x - this.getLocationOnScreen().x - xOffset;
				p2.y = p.y - this.getLocationOnScreen().y - yOffset;
				Element e = Element.values()[selectedElement];
				grid.clicks.add(new Clicky(p2, e));
			}
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		keySet.set(e.getKeyCode(),true);
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			size++;
			if (size > 6){
				size = 6;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			size--;
			if(size < 1){
				size = 1;
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		keySet.set(e.getKeyCode(),false);
	}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		isClicked = true;
	}
	public void mouseReleased(MouseEvent e) {
		isClicked = false;
	}
	public void mouseEntered(MouseEvent e) {
		isClicked = false;
	}
	public void mouseExited(MouseEvent e) {
		isClicked = false;
	}

}
