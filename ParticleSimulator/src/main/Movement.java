package main;

public class Movement {
	
	Direction d;
	int mult;
	public Movement(Direction d, int mult){
		this.d = d;
		this.mult = mult;
	}
	
	enum Direction{
		up(0,1),down(0,-1),left(-1,0),right(1,0),
		downLeft(-1,-1),downRight(1,-1),
		powderLeft(-1,-2),powderRight(1,-2);
		Direction(int x, int y){
			this.x = x;
			this.y = y;
		}
		int x;
		int y;

	}
	public int getX(){return d.x * mult;}
	public int getY(){return d.y * mult;}
}
