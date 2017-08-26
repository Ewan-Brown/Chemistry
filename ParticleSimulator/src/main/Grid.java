package main;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import main.Particle.Element;

public class Grid implements Runnable{
	int width = 300;
	int height = 300;
	Color[][] colors = new Color[width][height];
	Particle[][] particleArray = new Particle[width][height];
	ArrayList<Clicky> clicks = new ArrayList<Clicky>();
	ArrayList<ArrayList<Particle>> allParticles = new ArrayList<ArrayList<Particle>>();
	//TODO Special heat checker, after physics and chemistry. Each element has its own melting/vaporizing points etc...
	Random rand = new Random();
	public Grid(){
		for(int i = 0; i < Element.Type.values().length;i++){
			allParticles.add(new ArrayList<Particle>());
		}
		for(int x = 0; x < width;x++){
			for(int y = 0; y < height;y++){
				Particle a = new Particle(Element.SPACE);
//				if(Math.random() < 0.9){
//					if( x > 120){
//						a = new Particle(Element.WATER);
//					}
//					if (y < 100 && x > 200){
//						a = new Particle(Element.SAND);
//					}
//				}
				addParticle(x, y, a);
			}
		}
	}
	public Point getLocation(Particle p1){
		for(int x = 0; x < width;x++){
			for(int y = 0; y < height;y++){
				Particle p2 = particleArray[x][y];
				if(p2 == p1){
					return new Point(x,y);
				}
			}
		}
		return null;
	}
	public void addParticle(int x, int y, Particle p){
		if(x > width - 1 || x < 0 || y > height - 1 || y < 0){
			return;
		}
		p.x = x;
		p.y = y;
		Particle p1 = particleArray[x][y];
		if(p1!=null){
			removeParticle(p1);
		}
		particleArray[x][y] = p;
		int t = p.element.type.ordinal();
		allParticles.get(t).add(p);
	}
	public void update(){
		updateColor();
		updateClicks();
		if(Panel.paused){
			return;
		}
		updateLiquid();
		updatePowder();
		updateReactions();

	}
	public void updateReactions(){
		ArrayList<Particle> pa = new ArrayList<Particle>();
		for(int i = 0; i < allParticles.size();i++){
			pa.addAll(allParticles.get(i));
		}
		for(int i = 0; i < pa.size();i++){
			Particle p = pa.get(i);
			int pI = p.element.ordinal();
			for(int x = -1; x < 2;x++){
				for(int y = -1; y < 2;y++){
					int tX = x + p.x;
					int tY = y + p.y;
					if(tX >= width || tY >= height){
						continue;
					}
					if(tX < 0 || tY < 0){
						continue;
					}
					Particle p2 = particleArray[tX][tY];
					int tI = p2.element.ordinal();
					int f = Reactions.reactionTable[pI][tI];
					if(f == -1){
						continue;
					}
					else{
						/*TODO Instead of comparing objects just give each particle a unique incrementing int ID(starting at 0)
						 *	   and then just cycle through ints. ( Maybe speed this up even more using some logic?)
						 */
						removeParticle(p);
						removeParticle(p2);
						Particle pN1 = new Particle(Element.values()[f]);
						Particle pN2 = new Particle(Element.values()[f]);
						addParticle(p.x, p.y, pN1);
						addParticle(p.x + x, p.y + y, pN2);
					}
				}
			}
		}
	}
	public void removeParticle(Particle p){
		int f = p.element.type.ordinal();
		allParticles.get(f).remove(p);
	}
	public void updateClicks(){
		for(int i = 0; i < clicks.size();i++){
			Clicky click = clicks.get(i);
			Point p = click.p;
			int x = (int)p.getX() / Panel.size;
			int y = (int)p.getY() / Panel.size;
			if(x > width || x < 0 || y > height || y < 0){
				continue;
			}
			Element e = click.e;
			addParticle(x, y, new Particle(e));
			addParticle(x - 1, y, new Particle(e));
			addParticle(x + 1, y, new Particle(e));
			addParticle(x, y + 1, new Particle(e));
			addParticle(x, y - 1, new Particle(e));
			clicks.remove(i);
		}
	}
	public void updateColor(){
		Color[][] colorsTemp = new Color[width][height];
		for(int x = 0; x < width;x++){
			for(int y = 0; y < height;y++){
				colorsTemp[x][y] = particleArray[x][y].element.c;
			}
		}
		colors = colorsTemp;
	}
	public void updateGas(){
		ArrayList<Particle> g = allParticles.get(Element.Type.GAS.ordinal());
		for(int i = 0 ;i < g.size();i++){
			Particle p = g.get(i);
			p.isNew = false;
			for(int x = 0; x < 2; x++){
				for(int y = 0; y < 2; y++){

				}
			}
		}
	}
	public void updatePowder(){
//		ArrayList<Particle> a = allParticles.get(Element.Type.POWDER.ordinal());
//		firstLoop:
//			for(int i = 0; i < a.size();i++){
//				Particle p = a.get(i);
//				p.isNew = false;
//				Movement down = new Movement(0,-1);
//
//				boolean d = canMove(p.x, p.y,down);
//				if(d){
//					move(p.x,p.y,down);
//					if(rand.nextDouble() < 0.3){
//						continue;
//					}
//					if(rand.nextDouble() < 0.2){
//						Movement m = new Movement(-1,0);
//						if(rand.nextBoolean()){
//							m = new Movement(1,0);
//						}
//						if(canMove(p.x,p.y,m)){
//							move(p.x,p.y,m);
//						}
//					}
//				}
//				else{
//					if(rand.nextDouble() < 0.1){
//						continue firstLoop;
//					}
//					Movement left1 = new Movement(-1,0);
//					Movement right1 = new Movement(1,0);
//					Movement dleft1 = new Movement(-1,-1);
//					Movement dright1 = new Movement(1,-1);
//					Movement dleft2 = new Movement(-1,-2);
//					Movement dright2 = new Movement(1,-2);
//					Movement[][] moves = {{left1,dleft1,dleft2},{right1,dright1,dright2}};
//					int m = rand.nextInt(2);
//					boolean flag1 = true;
//					for(int j = 0; j < 3;j++){
//						if(!canMove(p.x,p.y,moves[m][j])){
//							flag1 = false;
//						}
//					}
//					if(flag1){
//						move(p.x,p.y,moves[m][2]);
//						continue firstLoop;
//					}
//					for(int j = 0; j < 3;j++){
//						if(!canMove(p.x,p.y,moves[1 - m][j])){
//							flag1 = false;
//						}
//					}
//					if(flag1){
//						move(p.x,p.y,moves[m][2]);
//						continue firstLoop;
//					}
//				}
//			}
	}
	public void updateLiquid(){
		ArrayList<Particle> l = allParticles.get(Element.Type.LIQUID.ordinal());
		liquidLoop:
			for(int i = 0; i < l.size();i++){
				Particle p = l.get(i);
				p.isNew = false;
				int w = p.element.weight;
				int max = 0;
				//Check if particle can fall down
				for(int j = 1; j < w + 1; j++){
					if(canMove(p.x,p.y,0,-j)){
						max = -j;
					}
					else{
						break;
					}
				}
				//If particle can fall down, then fall and finish
				if(max != 0){
					move(p.x, p.y, 0, max);
					continue;
				}
				//Otherwise...
//				int d = (rand.nextBoolean()) ? 1 : -1; //Direction, >1 = right --- <1 = left
				
				//If d direction doesn't work at all redo this with d negafied ?
//				int finalX = 0;
//				int finalY = 0;
//				for(int j = 1; j < w + 1;j++){
//					if(canMove(p.x, p.y, j * d, 0)){
//						finalX = j * d;
//						int xMax = 0;
//						for(int k = 1; k < w + 1 - j;k++){
//							if(canMove(p.x, p.y, j * d, 0)){
//								
//							}
//						}
					}
//					else break;
//					else if (j == 1){
						//Flip d and restart
						//Or end???
//					}
//				}
//			}
	}
	public boolean canMove(int col, int row, int x, int y){
		Particle moving = getParticle(col,row);
		int targetCol = col + x;
		int targetRow = row - y;
		Particle target = getParticle(targetCol,targetRow);
		
		if(target == null) return false;
		
		Element.Type mT = moving.element.type;
		Element.Type tT = target.element.type;
		
		if(mT == Element.Type.SOLID || target.element.type == Element.Type.SOLID){
			return false;
		}
		if(tT == Element.Type.SPACE){
			return true;
		}
		if(mT == Element.Type.POWDER && tT == Element.Type.LIQUID){
			return true;
		}
		if(mT == tT && moving.element.weight < target.element.weight){
			return true;
		}
		else{
			return false;
		}

	}
	public void move(int col,int row,int x, int y){
		Particle moving = getParticle(col,row);
		int targetCol = col + x;
		int targetRow = row - y;
		Particle target = getParticle(targetCol,targetRow);
		setParticle(targetCol,targetRow,moving);
		moving.x = targetCol;
		moving.y = targetRow;
		target.x = col;
		target.y = row;
		setParticle(col,row,target);
	}
	public void setParticle(int x, int y, Particle p){
		particleArray[x][y] = p;
	}
	public Particle getParticle(int x, int y){
		if(x < 0 || x >= width || y < 0 || y >= height){
			return null;
		}
		else{
			return particleArray[x][y];
		}
	}
	public void run() {
		while(true){
			try {
				update();
				Thread.sleep(Panel.delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
