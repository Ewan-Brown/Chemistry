package main;

import java.awt.Color;

public class Particle implements Cloneable{
	public int heat;
	public Element element;
	public int x = 0;
	public int y = 0;
	//TODO GET RID OF THIS SOMEHOW IT IS NOT GOOD
	boolean isNew = true;
	public enum Element{
		
		SPACE(Type.SPACE,Color.BLACK,0,0),
		WATER(Type.LIQUID,Color.BLUE,1,2),
		SAND(Type.LIQUID,Color.YELLOW,2,2),
		CLAY(Type.LIQUID,Color.LIGHT_GRAY,3,1),
		STONE(Type.SOLID,Color.DARK_GRAY,1,0);
		Type type;
		int weight;
		int viscosity;
		Color c;
		int meltingPoint;
		int vaporizingPoint;
		private Element(Type t, Color c, int w, int v){
			this.type = t;
			this.c = c;
			this.weight = w;
			this.viscosity = v;
		}
		public enum Type{
			SOLID, GAS, LIQUID, SPACE, POWDER;
		}
	}
	public Particle(Element e){
		this.element = e;
	}
	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
}
