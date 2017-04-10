package main;

import java.awt.Color;

public class Particle implements Cloneable{
	public int heat;
	public String name = "Particle";
	public Element element;
	public int x = 0;
	public int y = 0;
	public enum Element{
		WATER(Type.LIQUID,Color.BLUE),
		SAND(Type.POWDER,Color.YELLOW),
		SPACE(Type.SPACE,Color.BLACK);
		Type t;
		Color c;
		private Element(Type t, Color c){
			this.t = t;
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
