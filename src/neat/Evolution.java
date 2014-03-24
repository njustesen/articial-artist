package neat;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
		
//		Population neatPop = new Population(	
//									popSize /* population size */, 
//									numInputs /* network inputs */ , 
//									numOutputs /* network outputs */, 
//									maxNodes /* max index of nodes */, 
//									true /* recurrent */, 
//									0.5 /* probability of connecting two nodes */ );
//		
		Population neatPop = new Population(30 /* population size */, 9 /* network inputs */ , 2 /* network outputs */, 5 /* max index of nodes */, true /* recurrent */, 0.5 /* probability of connecting two nodes */ );

		for(int i = 0; i < iterations; i++){
			train(neatPop, paintTime);
			assignFitness(neatPop, i);
		}
		
		Organism best = bestOrganism(neatPop);
		
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
		this.panel.repaint();
		
	}

	private void assignFitness(Population neatPop, int generation) {
		
		Vector neatOrgs = neatPop.getOrganisms();
		 
		for(int i=0;i<neatOrgs.size();i++)
		{
			// Assign each organism a "fitness". A measure of how well the organism performed since the last evolution.
			double fitness = Math.random();
			((Organism)neatOrgs.get(i)).setFitness(fitness);
		}
		 
		neatPop.epoch(generation + 1); // Evolve the population and increment the generation.
		
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
