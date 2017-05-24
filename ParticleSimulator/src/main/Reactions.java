package main;

public class Reactions {
	static int reactionNum = Particle.Element.values().length;
	static int[][] reactionTable = new int[reactionNum][reactionNum];
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
