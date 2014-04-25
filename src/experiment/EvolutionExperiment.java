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
			goal = ImageIO.read(new File("tealtest.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		NEvolution evolution = new NEvolution(
				10, 	/* Number of inputs */
				10, 	/* Number of outputs */
				20, 	/* Population size */
				5,		/* Max. number of nodes */
				260,	/* Picture width */
				260,	/* Picture height */
				2, 		/* Number of pictures selected (champions) */
				2, 		/* Number novel artists created each epoch */
				goal); 
		
		Painter painter = evolution.evolvePainter(
				200,	/* Paint time */
				1000,
				true);	/* Iterations in evolution */
		
	}

}
