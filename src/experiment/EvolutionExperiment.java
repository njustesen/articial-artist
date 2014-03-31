package experiment;

import neat.Painter;
import neat.Evolution;

public class EvolutionExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Evolution evolution = new Evolution(7,6,8,6,250,250,3);
		Painter painter = evolution.evolvePainter(20000,1000);
		
	}

}
