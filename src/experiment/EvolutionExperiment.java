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
				6, 		/* Population size */
				56,		/* Max. number of nodes */
				260,	/* Picture width */
				260,	/* Picture height */
				2, 		/* Number of pictures selected (champions) */
				2 		/* Number novel artists created each epoch */
				); 
		Painter painter = evolution.evolvePainter(
				1000,	/* Paint time */
				1000);	/* Iterations in evolution */
		
	}

}
