package main;

import java.awt.Color;

public class Particle implements Cloneable{
	public int heat;
	public Element element;
	public int x = 0;
	public int y = 0;
	boolean isNew = true;
	public enum Element{
		SPACE(Type.SPACE,Color.BLACK),
		WATER(Type.LIQUID,Color.BLUE),
		SAND(Type.POWDER,Color.YELLOW),
		CLAY(Type.POWDER,Color.LIGHT_GRAY),
		STONE(Type.SOLID,Color.GRAY);
		Type type;
		Color c;
		private Element(Type t, Color c){
			this.type = t;
			this.c = c;
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
