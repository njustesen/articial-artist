package neat;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import paint.PaintProgram;

import jneat.*;

public class Evolution {
	
	int numOutputs;
	int numInputs;
	int popSize;
	int maxNodes;
	EvolutionPanel panel;

	public Evolution(int numInputs, int numOutputs, int popSize, int maxNodes){
		super();
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.popSize = popSize;
		this.maxNodes = maxNodes;
		panel = new EvolutionPanel();
	}
	
	public Painter evolvePainter(int paintTime, int iterations){
		
		Neat.initbase();
		
		Population neatPop = new Population(	
									popSize /* population size */, 
									numInputs /* network inputs */ , 
									numOutputs /* network outputs */, 
									maxNodes /* max index of nodes */, 
									true /* recurrent */, 
									0.5 /* probability of connecting two nodes */ );
	
		for(int i = 1; i <= iterations; i++){
			System.out.println("\nTRAIN:");
			train(neatPop, paintTime);
			for(Object obj : neatPop.organisms)
				System.out.println(((Organism)obj).hashCode() + " \tf: " + ((Organism)obj).getFitness());
			System.out.println("\nASSIGN FITNESS:");
			assignFitness(neatPop, i);
			for(Object obj : neatPop.organisms)
				System.out.println(((Organism)obj).hashCode() + " \tf: " + ((Organism)obj).getFitness());
			
			if (i == iterations)
				break;
			
			System.out.println("\nREPRODUCE:");
			neatPop.epoch(i); // Evolve the population and increment the generation.
			for(Object obj : neatPop.organisms)
				System.out.println(((Organism)obj).hashCode() + " \tf: " + ((Organism)obj).getFitness());
		}
		
		Organism best = bestOrganism(neatPop);
		
		System.out.println("\nBEST ORGANISM:");
		System.out.println(best.hashCode() + "\tf: " + best.getFitness());
		
		return organismToPainter(best);
		
	}

	private void train(Population neatPop, int paintTime) {
		
		Vector neatOrgs = neatPop.getOrganisms();
		PaintProgram program = new PaintProgram(false); 
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for(int i=0;i<neatOrgs.size();i++){
			
			// Extract the neural network from the jNEAT organism.
			Painter painter = organismToPainter((Organism)neatOrgs.get(i));
			
			images.add(program.paintPicture(painter, paintTime));
				
		}
		
		showImages(images);
		
	}
	
	private void showImages(List<BufferedImage> pictures) {
		
		this.panel.clearPictures();
		this.panel.addPictures(pictures);
		
	}

	private void assignFitness(Population neatPop, int generation) {
		
		Vector neatOrgs = neatPop.getOrganisms();
		 
		for(int i=0;i<neatOrgs.size();i++)
		{
			// Assign each organism a "fitness". A measure of how well the organism performed since the last evolution.
			double fitness = Math.random();
			((Organism)neatOrgs.get(i)).setFitness(fitness);
		}
		 
	}
	
	private Painter organismToPainter(Organism organism) {
		
		return new Painter(organism.getNet());
		
	}

	private Organism bestOrganism(Population neatPop) {
		
		Vector neatOrgs = neatPop.getOrganisms();
		 
		double bestFitness = Double.MIN_VALUE;
		Organism mostFit = null;
		
		for(int i=0;i<neatOrgs.size();i++){
			
			Organism organism = (Organism)neatOrgs.get(i);
			if (organism.getFitness() > bestFitness){
				bestFitness = organism.getFitness();
				mostFit = organism;
			}
		}
		return mostFit;
		
	}
	
}
