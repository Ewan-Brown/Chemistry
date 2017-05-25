package main;

import main.Particle.Element;

public class Reaction {
	//Elements made from reaction
	Element e1; //new "X" element
	Element e2; //new "Y" element
	//Ignore temperatures or not
	boolean tempRequired;
	//Temperature required for it to happen
	int tempNeeded;
	//Temperature added or taken from system
	int tempChange;

	public Reaction(Element e1, Element e2,boolean tR,int tN,int tC){
		this.e1 = e1;
		this.e2 = e2;
		tempRequired = tR;
		tempNeeded = tN;
		tempChange = tC;
	}
	public Particle[] tryReaction(Particle p1, Particle p2){
		if(tempRequired){
			int t = p1.heat + p2.heat;
			if(t < tempNeeded){
				return null;
			}
		}
		Particle pN1 = new Particle(e1);
		Particle pN2 = new Particle(e2);
		int t = tempChange / 2;
		pN1.heat = p1.heat + t;
		pN2.heat = p2.heat + t;

		return null;
	}
}
