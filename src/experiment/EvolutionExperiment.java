package experiment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.security.auth.login.Configuration;

import neat.MEvolution;
import neat.MEvolution2;
import neat.NEvolution;
import neat.Painter;
import neat.Evolution;
import config.Config;
import config.EvolutionMethod;

public class EvolutionExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Config.load(args[0]);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		BufferedImage goal = null;
		try {
			//goal = ImageIO.read(new File("monalisa_small.jpg"));
			if (Config.goalImage != null){
				goal = ImageIO.read(new File(Config.goalImage));
				Config.pictureHeight = goal.getHeight();
				Config.pictureWidth = goal.getWidth();
			}
			//goal = ImageIO.read(new File("blacktest.jpg"));
		} catch (IOException e) {
			System.out.println("Unable to find " + goal);
			e.printStackTrace();
		}
		
		if (Config.evolution == EvolutionMethod.MUTATION){
			MEvolution2 evolution = new MEvolution2(
					10, 						/* Number of inputs */
					10, 						/* Number of outputs */
					Config.populationSize, 		/* Population size */
					Config.maxNodes,			/* Max. number of nodes */
					Config.pictureWidth,		/* Picture width */
					Config.pictureHeight,		/* Picture height */
					goal); 
			
			Painter painter = evolution.evolvePainter(
					Config.paintTime,	/* Paint time */
					Integer.MAX_VALUE,/* Iterations in evolution */
					(goal != null));	
		} else if (Config.evolution == EvolutionMethod.MATING){
			NEvolution evolution = new NEvolution(
					10, 						/* Number of inputs */
					10, 						/* Number of outputs */
					Config.populationSize, 		/* Population size */
					Config.maxNodes,			/* Max. number of nodes */
					Config.pictureWidth,		/* Picture width */
					Config.pictureHeight,		/* Picture height */
					Config.champions,			/* Number of pictures selected (champions) */
					Config.novel,				/* Number novel artists created each epoch */
					goal); 
			
			Painter painter = evolution.evolvePainter(
					Config.paintTime,	/* Paint time */
					Integer.MAX_VALUE,/* Iterations in evolution */
					(goal != null));	
		} else if (Config.evolution == EvolutionMethod.NEAT){
			
			Evolution evolution = new Evolution(
					10, 						/* Number of inputs */
					10, 						/* Number of outputs */
					Config.populationSize, 		/* Population size */
					Config.maxNodes,			/* Max. number of nodes */
					Config.pictureWidth,		/* Picture width */
					Config.pictureHeight,		/* Picture height */
					Config.champions, 			/* Number of pictures selected (champions) */
					Config.novel, 				/* Number novel artists created each epoch */
					goal);
			
			Painter painter = evolution.evolvePainter(
					Config.paintTime,	/* Paint time */
					Integer.MAX_VALUE,/* Iterations in evolution */
					(goal != null));	
		}
	}

}
