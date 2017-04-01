package main;

import java.awt.Color;

public abstract class ParticleAbstract implements Cloneable{
	public int heat;
	public String name = "BaseParticle";
	public Color color;
	public Element element;
	public int x = 0;
	public int y = 0;
	public enum Element{
		SOLID, GAS, LIQUID, SPACE
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
