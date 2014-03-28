package experiment;

import neat.Painter;
import neat.Evolution;

public class EvolutionExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Evolution evolution = new Evolution(2,2,20,6);
		Painter painter = evolution.evolvePainter(100,10);
		
		
		
	}

}
