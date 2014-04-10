package experiment;

import neat.Painter;
import neat.Evolution;

public class EvolutionExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Evolution evolution = new Evolution(
				10, 	/* Number of inputs */
				8, 	/* Number of outputs */
				3, 	/* Population size */
				10,	/* Max. number of nodes */
				400,/* Picture width */
				400,/* Picture height */
				1 	/* Number of pictures selected (champions) */
				); 
		Painter painter = evolution.evolvePainter(
				10000,	/* Paint time */
				1000);	/* Iterations in evolution */
		
	}

}
