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
				10, 	/* Number of outputs */
				8, 		/* Population size */
				64,		/* Max. number of nodes */
				200,	/* Picture width */
				200,	/* Picture height */
				2 		/* Number of pictures selected (champions) */
				); 
		Painter painter = evolution.evolvePainter(
				1000,	/* Paint time */
				1000);	/* Iterations in evolution */
		
	}

}
