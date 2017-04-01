package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import main.Movement.Direction;
import main.ParticleAbstract.Element;

public class Grid {
	int width = 150;
	int height = 150;
	ArrayList<ArrayList<ParticleAbstract>> particles = new ArrayList<ArrayList<ParticleAbstract>>();
	ArrayList<ParticleAbstract> liquids = new ArrayList<ParticleAbstract>();
	Random rand = new Random();
	public Grid(){
		for(int c = 0; c < width;c++){
			particles.add(new ArrayList<ParticleAbstract>());
			for(int r = 0; r < height;r++){
				//
				ParticleAbstract a = new ParticleSpace();
				particles.get(c).add(r, a);
				if(c > 100){
					if(rand.nextDouble() < 0.2){
						a = new ParticleWater();
						addParticle(c, r, a);
					}
				}

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
		particles.get(x).set(y, p);
		if(p.element == Element.LIQUID){
			liquids.add(p);
		}
	}
	public void update(){
		firstloop:
			for(int i = 0; i < liquids.size();i++){
				ParticleAbstract p1 = liquids.get(i);
				Point p = getLocation(p1);
				Movement down = new Movement(Direction.down,1);
				if(canMove(p.x, p.y,down)){
					move(p.x,p.y,down);
				}
				Movement left1 = new Movement(Direction.left,1);
				Movement left2 = new Movement(Direction.left,2);
				Movement right1 = new Movement(Direction.right,1);
				Movement right2 = new Movement(Direction.right,2);
				Movement[][] moves = {{left1,left2},{right1,right2}};
				//1 is left 2 is right
				int m = rand.nextInt(2);
				for(int j = 0;j < 2;j++){
					if(canMove(p.x,p.y,moves[m][j])){
						move(p.x,p.y,moves[m][j]);
						continue firstloop;

					}
				}
				for(int j = 0;j < 2;j++){
					if(canMove(p.x,p.y,moves[1 - m][j])){
						move(p.x,p.y,moves[1 - m][j]);
						continue firstloop;
					}
				}
			}

	}
	public boolean canMove(int col, int row, Movement m){
		//		ParticleAbstract moving = getParticle(col,row);
		int targetCol = col + m.getX();
		int targetRow = row - m.getY();
		ParticleAbstract target = getParticle(targetCol,targetRow);
		if(target == null){
			return false;
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
		//		ParticleAbstract target = getParticle(targetCol,targetRow);
		setParticle(targetCol,targetRow,moving);
		setParticle(col,row,new ParticleSpace());
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

}
