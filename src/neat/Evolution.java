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
	private int imgWidth;
	private int imgHeight;
	private int champions;
	private int novelty;

	public Evolution(int numInputs, int numOutputs, int popSize, int maxNodes, int picWidth, int picHeight, int champions, int novelty){
		super();
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.popSize = popSize;
		this.maxNodes = maxNodes;
		this.imgWidth = picWidth;
		this.imgHeight = picHeight;
		this.champions = champions;
		this.novelty = novelty;
		panel = new EvolutionPanel();
	}
	
	public Painter evolvePainter(int paintTime, int iterations){
		
		Neat.initbase();
		// Set NEAT parameters..
		Neat.d_mate_singlepoint_prob = "1000";
		Neat.d_mutate_random_trait_prob = "1000";
		Neat.d_interspecies_mate_rate = "1000";
		Neat.d_mutate_add_link_prob = "1000";
		Neat.d_mate_multipoint_avg_prob = "1000";
		Neat.d_mutate_add_node_prob = "1000";
		Neat.d_mutate_link_trait_prob = "1000";
		Neat.d_mutate_gene_reenable_prob = "1000";
		Neat.d_mutate_random_trait_prob = "1000";
		Neat.d_mutate_toggle_enable_prob = "1000";
		Neat.d_mutdiff_coeff = "1000";
		Neat.d_mate_multipoint_avg_prob = "1000";
		Neat.d_linktrait_mut_sig = "1000";
		//Neat.d_age_significance = "10";
		//Neat.d_babies_stolen = "10";
		Neat.d_newlink_tries = "1000";
		
		// Generate population
		Population neatPop = new Population(	
									popSize /* population size */, 
									numInputs /* network inputs */ , 
									numOutputs /* network outputs */, 
									maxNodes /* max index of nodes */, 
									true /* recurrent */, 
									100 /* probability of connecting two nodes */ );
		
		// Run evolution
		for(int i = 1; i <= iterations; i++){
			
			// Paint pictures
			List<BufferedImage> pictures = paint(neatPop, imgWidth, imgHeight, paintTime);
			
			// Show pictures and wait for feedback
			presentAndRate(neatPop, pictures);
			
			// Assign random fitness
			//assignRandomFitness(neatPop, i);
			
			if (i == iterations)
				break;
			
			// Evolutionize
			//neatPop.epoch(i); // Evolve the population and increment the generation.
			neatPop = epoch(neatPop, i);
			
			//for(int e = 0; e<10; e++){
				neatPop.epoch(1);
			//}
			
		}
		
		// Return best painter
		Organism best = bestOrganism(neatPop);
		return organismToPainter(best);
		
	}

	private Population epoch(Population neatPop, int generation) {
		
		// Kill bad artists
		Vector neatOrgs = neatPop.getOrganisms();
		for(int i=0;i<neatOrgs.size();i++){
			
			Organism org = (Organism)neatOrgs.get(i);
			if (org.getFitness() != 1){
				org.setEliminate(true);
			}
		}
		
		// Spawn novel artists
		/*
		Population newPop = new Population(novelty, 
				numInputs,
				numOutputs,
				maxNodes,
				true,
				0.9
				);
		newPop.epoch(1);
		Vector newOrgs = newPop.getOrganisms();
		for(int i=0;i<newOrgs.size();i++){
			
			Organism org = (Organism)newOrgs.get(i);
			org.setFitness(0.2);
		}
		neatPop.getSpecies().addAll(newPop.getSpecies());
		neatPop.getOrganisms().addAll(newPop.getOrganisms());
		*/
		return neatPop;
	}

	private void presentAndRate(Population neatPop, List<BufferedImage> pictures) {
		
		panel.clearPictures();
		panel.addPictures(pictures);
		Vector neatOrgs = neatPop.getOrganisms();
		
		// Reset fitness
		for(Object org : neatOrgs)
			((Organism)org).setFitness(0);
		
		// While champions not selected keep showing paintings
		while(panel.getSelected().size() < champions){
			
			panel.repaint();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		// Wait 1000 ms
		panel.repaint();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Give fitness to selected pictures
		for(BufferedImage goodPic : panel.getSelected()){
			
			int idx = panel.getPictures().indexOf(goodPic);
			((Organism)neatOrgs.get(idx)).setFitness(1);
			
		}
		
	}

	private List<BufferedImage> paint(Population neatPop, int width, int height, int paintTime) {
		
		Vector neatOrgs = neatPop.getOrganisms();
		PaintProgram program = new PaintProgram(true, width, height); 
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for(int i=0;i<popSize;i++){
			
			// Extract the neural network from the jNEAT organism.
			Painter painter = organismToPainter((Organism)neatOrgs.get(i));
			
			images.add(program.paintPicture(painter, paintTime));
				
		}
		
		return images;
		
	}
	
	private void showImages(List<BufferedImage> pictures) {
		
		this.panel.clearPictures();
		this.panel.addPictures(pictures);
		
	}

	private void assignRandomFitness(Population neatPop, int generation) {
		
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
