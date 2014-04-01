package experiment;

import neat.Painter;
import neat.Evolution;

public class EvolutionExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Evolution evolution = new Evolution(
				8, 	/* Number of inputs */
				7, 	/* Number of outputs */
				4, 	/* Population size */
				20,	/* Max. number of nodes */
				200,/* Picture width */
				200,/* Picture height */
				0 	/* Number of pictures selected (champions) */
				); 
		Painter painter = evolution.evolvePainter(
				20000,	/* Paint time */
				1000);	/* Iterations in evolution */
		
	}

}
