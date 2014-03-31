package neat;
import java.awt.Dimension;
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

	public Evolution(int numInputs, int numOutputs, int popSize, int maxNodes, int picWidth, int picHeight, int champions){
		super();
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.popSize = popSize;
		this.maxNodes = maxNodes;
		this.imgWidth = picWidth;
		this.imgHeight = picHeight;
		this.champions = champions;
		panel = new EvolutionPanel();
	}
	
	public Painter evolvePainter(int paintTime, int iterations){
		
		Neat.initbase();
		Neat.d_mate_singlepoint_prob = "0.9";
		Neat.d_mutate_random_trait_prob = "0.9";
		Neat.d_interspecies_mate_rate = "0.9";
		Neat.d_mutate_add_link_prob = "0.9";
		Neat.d_mate_multipoint_avg_prob = "0.9";
		Neat.d_mutate_add_node_prob = "0.9";
		Neat.d_mutate_link_trait_prob = "0.9";
		Neat.d_mutate_gene_reenable_prob = "0.9";
		Neat.d_mutate_random_trait_prob = "0.9";
		Neat.d_mutate_toggle_enable_prob = "0.9";
		Neat.d_mutdiff_coeff = "0.9";
		Neat.d_mate_multipoint_avg_prob = "0.9";
		Neat.d_linktrait_mut_sig = "7";
		Neat.d_age_significance = "0.7";
		Neat.d_babies_stolen = "0.6";
		Neat.d_newlink_tries = "4";
		
		Population neatPop = new Population(	
									popSize /* population size */, 
									numInputs /* network inputs */ , 
									numOutputs /* network outputs */, 
									maxNodes /* max index of nodes */, 
									true /* recurrent */, 
									0.9 /* probability of connecting two nodes */ );
	
		for(int i = 1; i <= iterations; i++){
			
			System.out.println("\nTRAIN:");
			List<BufferedImage> pictures = paint(neatPop, imgWidth, imgHeight, paintTime);
			for(Object obj : neatPop.organisms)
				System.out.println(((Organism)obj).hashCode() + " \tf: " + ((Organism)obj).getFitness());
			
			presentAndRate(neatPop, pictures);
			
			System.out.println("\nASSIGN FITNESS:");
			//assignFitness(neatPop, i);
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

	private void presentAndRate(Population neatPop, List<BufferedImage> pictures) {
		
		panel.clearPictures();
		panel.addPictures(pictures);
		Vector neatOrgs = neatPop.getOrganisms();
		
		for(Object org : neatOrgs){
			
			((Organism)org).setFitness(0);
			
		}
		
		while(panel.getSelected().size() < champions){
			
			panel.repaint();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		panel.repaint();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(BufferedImage goodPic : panel.getSelected()){
			
			int idx = panel.getPictures().indexOf(goodPic);
			((Organism)neatOrgs.get(idx)).setFitness(1);
			
		}
		
		
		
	}

	private List<BufferedImage> paint(Population neatPop, int width, int height, int paintTime) {
		
		Vector neatOrgs = neatPop.getOrganisms();
		PaintProgram program = new PaintProgram(true, width, height); 
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for(int i=0;i<neatOrgs.size();i++){
			
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
