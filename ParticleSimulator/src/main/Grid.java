package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

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
				if(r > 1 && c > 100){
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
		for(int i = 0; i < liquids.size();i++){
			ParticleAbstract p1 = liquids.get(i);
			Point p = getLocation(p1);
			if(canMove(p.x, p.y, Direction.down)){
				move(p.x,p.y,Direction.down);
			}
			else{
				if(rand.nextDouble() < 1){
					Direction d = Direction.left;
					if(rand.nextBoolean()){
						d = Direction.right;
					}
					if(canMove(p.x,p.y,d)){
						move(p.x,p.y,d);
					}
				}
			}

		}
	}
	public boolean canMove(int col, int row, Direction d){
		//		ParticleAbstract moving = getParticle(col,row);
		int targetCol = col + d.x;
		int targetRow = row - d.y;
		ParticleAbstract target = getParticle(targetCol,targetRow);
		if(target instanceof ParticleSpace){
			return true;
		}
		else{
			return false;
		}

	}
	public void move(int col,int row,Direction d){
		ParticleAbstract moving = getParticle(col,row);
		int targetCol = col + d.x;
		int targetRow = row - d.y;
		//		ParticleAbstract target = getParticle(targetCol,targetRow);
		setParticle(targetCol,targetRow,moving);
		setParticle(col,row,new ParticleSpace());
	}
	public void setParticle(int x, int y, ParticleAbstract p){
		particles.get(x).set(y, p);
	}
	public ParticleAbstract getParticle(int x, int y){
		if(x < 0 || x >= width || y < 0 || y >= height){
			//		System.err.println("THAT PARTICLE DONT EXIST! X, Y :"+x+ " " +y );
			return null;
		}
		else{
			return particles.get(x).get(y);
		}
	}
	enum Direction{
		up(0,1),down(0,-1),left(-1,0),right(1,0);
		Direction(int x, int y){
			this.x = x;
			this.y = y;
		}
		int x;
		int y;
	}
}
