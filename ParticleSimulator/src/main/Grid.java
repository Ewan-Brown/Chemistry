package main;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import main.Movement.Direction;
import main.ParticleAbstract.Element;

public class Grid implements Runnable{
	int width = 300;
	int height = 400;
	Color[][] colors = new Color[width][height];
	ArrayList<ArrayList<ParticleAbstract>> particles = new ArrayList<ArrayList<ParticleAbstract>>();
	ArrayList<ParticleAbstract> liquids = new ArrayList<ParticleAbstract>();
	ArrayList<ParticleAbstract> powders = new ArrayList<ParticleAbstract>();
	Random rand = new Random();
	public Grid(){
		for(int c = 0; c < width;c++){
			particles.add(new ArrayList<ParticleAbstract>());
			for(int r = 0; r < height;r++){
				//
				ParticleAbstract a = new ParticleSpace();
				particles.get(c).add(r, a);
				//				if(c > 100 && c < 150){
				if(rand.nextDouble() < 0.6){
					if( r > 350){
						a = new ParticleWater();
					}
					if (r < 100 && c > 200){
						a = new ParticlePowder();
					}
					addParticle(c, r, a);
				}
				//				}

			}
		}
	}

	public Point getLocation(ParticleAbstract p1){
		for(int c = 0; c < particles.size();c++){
			for(int r = 0; r < particles.get(c).size();r++){
				ParticleAbstract p2 = particles.get(c).get(r);
				if(p2 == p1){
					return new Point(c,r);
				}
			}
		}
		return null;
	}
	public void addParticle(int x, int y, ParticleAbstract p){
		p.x = x;
		p.y = y;
		particles.get(x).set(y, p);
		if(p.element == Element.LIQUID){
			liquids.add(p);
		}
		if(p.element == Element.POWDER){
			powders.add(p);
		}
	}
	public void update(){
		updateColor();
		updateLiquid();
		updatePowder();

	}
	public void updateColor(){
		Color[][] colorsTemp = new Color[width][height];
		for(int i = 0; i < width;i++){
			for(int j = 0; j < height;j++){
				colorsTemp[i][j] = particles.get(i).get(j).color;
			}
		}
		colors = colorsTemp;
	}
	public void updatePowder(){
		firstLoop:
			for(int i = 0; i < powders.size();i++){
				ParticleAbstract p = powders.get(i);
				Movement down = new Movement(Direction.down,1);

				boolean d = canMove(p.x, p.y,down);
				if(d){
					move(p.x,p.y,down);
					if(rand.nextDouble() < 0.3){
						continue;
					}
					if(rand.nextDouble() < 0.2){
						Movement m = new Movement(Direction.left,1);
						if(rand.nextBoolean()){
							m = new Movement(Direction.right,1);
						}
						if(canMove(p.x,p.y,m)){
							move(p.x,p.y,m);
						}
					}
				}
				else{
					Movement left1 = new Movement(Direction.left,1);
					Movement right1 = new Movement(Direction.right,1);
					Movement dleft1 = new Movement(Direction.downLeft,1);
					Movement dright1 = new Movement(Direction.downRight,1);
					Movement dleft2 = new Movement(Direction.powderLeft,1);
					Movement dright2 = new Movement(Direction.powderRight,1);
					Movement[][] moves = {{left1,dleft1,dleft2},{right1,dright1,dright2}};
					int m = rand.nextInt(2);
					boolean flag1 = true;
					for(int j = 0; j < 3;j++){
						if(!canMove(p.x,p.y,moves[m][j])){
							flag1 = false;
						}
					}
					if(flag1){
						move(p.x,p.y,moves[m][2]);
						continue firstLoop;
					}
					for(int j = 0; j < 3;j++){
						if(!canMove(p.x,p.y,moves[1 - m][j])){
							flag1 = false;
						}
					}
					if(flag1){
						move(p.x,p.y,moves[m][2]);
						continue firstLoop;
					}
				}
			}
	}
	public void updateLiquid(){
		liquidLoop:
			for(int i = 0; i < liquids.size();i++){
				ParticleAbstract p = liquids.get(i);
				Movement down = new Movement(Direction.down,1);
				boolean d = canMove(p.x, p.y,down);
				if(d){
					move(p.x,p.y,down);
					if(rand.nextDouble() < 0.3){
						continue;
					}
				}
				Movement left1 = new Movement(Direction.left,1);
				Movement left2 = new Movement(Direction.left,2);
				Movement right1 = new Movement(Direction.right,1);
				Movement right2 = new Movement(Direction.right,2);
				Movement[][] moves = {{left1,left2},{right1,right2}};
				//1 is left 2 is right

				int m = rand.nextInt(2);
				int max = 2;
				if (d) {
					max = 1;
				}
				for(int j = 0;j < max;j++){
					if(canMove(p.x,p.y,moves[m][j])){
						move(p.x,p.y,moves[m][j]);
						continue liquidLoop;

					}
				}
				for(int j = 0;j < max;j++){
					if(canMove(p.x,p.y,moves[1 - m][j])){
						move(p.x,p.y,moves[1 - m][j]);
						continue liquidLoop;
					}
				}
			}
	}
	public boolean canMove(int col, int row, Movement m){
		ParticleAbstract moving = getParticle(col,row);
		int targetCol = col + m.getX();
		int targetRow = row - m.getY();
		ParticleAbstract target = getParticle(targetCol,targetRow);
		if(target == null){
			return false;
		}
		if(moving.element == Element.POWDER && target.element == Element.LIQUID){
			return true;
		}
		if(target instanceof ParticleSpace){
			return true;
		}
		else{
			return false;
		}

	}
	public void move(int col,int row,Movement m){
		ParticleAbstract moving = getParticle(col,row);
		int targetCol = col +  m.getX();
		int targetRow = row - m.getY();
		ParticleAbstract target = getParticle(targetCol,targetRow);
		setParticle(targetCol,targetRow,moving);
		moving.x = targetCol;
		moving.y = targetRow;
		target.x = col;
		target.y = row;
		setParticle(col,row,target);
	}
	public void setParticle(int x, int y, ParticleAbstract p){
		particles.get(x).set(y, p);
	}
	public ParticleAbstract getParticle(int x, int y){
		if(x < 0 || x >= width || y < 0 || y >= height){
			return null;
		}
		else{
			return particles.get(x).get(y);
		}
	}
	public void run() {
		while(true){
			update();
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
