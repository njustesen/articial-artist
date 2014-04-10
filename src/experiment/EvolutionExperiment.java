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
				4, 		/* Population size */
				32,		/* Max. number of nodes */
				400,	/* Picture width */
				400,	/* Picture height */
				2 		/* Number of pictures selected (champions) */
				); 
		Painter painter = evolution.evolvePainter(
				3000,	/* Paint time */
				1000);	/* Iterations in evolution */
		
	}

}
