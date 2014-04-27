package experiment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import neat.NEvolution;
import neat.Painter;
import neat.Evolution;

public class EvolutionExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BufferedImage goal = null;
		try {
			//goal = ImageIO.read(new File("monalisa_small.jpg"));
			goal = ImageIO.read(new File("face.jpg"));
			//goal = ImageIO.read(new File("blacktest.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		NEvolution evolution = new NEvolution(
				10, 	/* Number of inputs */
				10, 	/* Number of outputs */
				16, 		/* Population size */
				40,		/* Max. number of nodes */
				300,	/* Picture width */
				300,	/* Picture height */
				2, 		/* Number of pictures selected (champions) */
				2, 		/* Number novel artists created each epoch */
				goal); 
		
//		Evolution evolution = new Evolution(
//				10, 	/* Number of inputs */
//				10, 	/* Number of outputs */
//				16, 		/* Population size */
//				5,		/* Max. number of nodes */
//				300,	/* Picture width */
//				300,	/* Picture height */
//				2, 		/* Number of pictures selected (champions) */
//				2, 		/* Number novel artists created each epoch */
//				null);
		
		Painter painter = evolution.evolvePainter(
				5000,	/* Paint time */
				1000,/* Iterations in evolution */
				true);	
		
	}

}
