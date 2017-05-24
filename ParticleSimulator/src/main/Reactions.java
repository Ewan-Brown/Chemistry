package main;

public class Reactions {
	//Instead of just ints, each element of this array is an object which gives both elements, but that would allow extra function (Chance of failed reaction)
	//And things like change in temperature.
	static final int reactionNum = Particle.Element.values().length;
	static final int[][] reactionTable = new int[reactionNum][reactionNum];
	public static void init(){
		for(int i = 0; i < reactionNum;i++){
			for(int j = 0; j < reactionNum;j++){
				reactionTable[i][j] = -1;
			}
		}
		reactionTable[1][2] = 3;
		reactionTable[2][1] = 3;
		printOutputs();


	}
	public static void printOutputs(){
		for(int h = 0; h < reactionNum;h++){
			for(int j = 0; j < reactionNum;j++){
				System.out.print(reactionTable[h][j] + " ");
			}
			System.out.println("");
		}
	}
}
