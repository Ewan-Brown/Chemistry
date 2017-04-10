package main;

public class Reactions {
	static int reactionNum = Particle.Element.values().length;
	static int[][] reactionTable = new int[reactionNum][reactionNum];
	{
		for(int i = 0; i < reactionNum;i++){
			reactionTable[i][0] = i;
			reactionTable[0][i] = i;
		}
	}
}
