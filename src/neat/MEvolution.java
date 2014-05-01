package neat;
import jNeatCommon.NeatConstant;

import java.awt.font.ImageGraphicAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;

import comparer.ImageComparer;

import paint.PaintProgram;

import jneat.*;

public class MEvolution {
	
	int numOutputs;
	int numInputs;
	int popSize;
	int maxNodes;
	EvolutionPanel panel;
	private int imgWidth;
	private int imgHeight;
	private BufferedImage goalImage;
	private Population population;
	private int nextId = 90000;

	public MEvolution(int numInputs, int numOutputs, int popSize, int maxNodes, int picWidth, int picHeight, BufferedImage goal){
		super();
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.popSize = popSize;
		this.maxNodes = maxNodes;
		this.imgWidth = picWidth;
		this.imgHeight = picHeight;
		this.goalImage = goal;
		if (this.goalImage != null){
			imgWidth = goalImage.getWidth();
			imgHeight = goalImage.getHeight();
		}
		panel = new EvolutionPanel();
	}
	
	public Painter evolvePainter(int paintTime, int iterations, boolean goal){
		
		Neat.initbase();
		
		// Generate population
		population = new Population(	
									popSize /* population size */, 
									numInputs /* network inputs */ , 
									numOutputs /* network outputs */, 
									maxNodes /* max index of nodes */, 
									true /* recurrent */, 
									0.5 /* probability of connecting two nodes */ );
		
		// Run evolution
		for(int i = 1; i <= iterations; i++){
			
			// Paint pictures
			List<BufferedImage> pictures = paint(population, imgWidth, imgHeight, paintTime);
			
			// Show pictures and wait for feedback
			if (goal)
				presentAndCompare(population, pictures);
			else
				presentAndRate(population, pictures);
			// Assign random fitness
			//assignRandomFitness(neatPop, i);
			
			
			if (i == iterations)
				break;
			
			// Evolutionize
			//neatPop.epoch(i); // Evolve the population and increment the generation.
			//if (!goal)
			epoch(population, i);
			
			//for(int e = 0; e<10; e++){
			//pop.epoch(i);
			//System.out.println(neatPop.getOrganisms().size() + " organisms. ");
			//}
			
		}
		
		// Return best painter
		Organism best = bestOrganism(population);
		return organismToPainter(best);
		
	}

	private void presentAndCompare(Population pop, List<BufferedImage> pictures) {
		
		panel.clearPictures();
		List<Integer> ids = new ArrayList<Integer>();
		for(Object obj : pop.getOrganisms())
			ids.add(((Organism)obj).getGenome().getGenome_id());
		panel.addPictures(pictures, ids);
		
		Vector<Organism> organisms = pop.getOrganisms();
		
		// Reset fitness
		for(Organism org : organisms)
			org.setFitness(0);
		
		// Give fitness to selected pictures
		List<BufferedImage> rated = new ArrayList<BufferedImage>();
		
		double lowestFitness = Double.MAX_VALUE;
		double bestFitness = Double.MIN_VALUE;
		int lowestIdx = 0;
		int bestIdx = 0;
		
		while(rated.size()<organisms.size()){
			for(BufferedImage picture : pictures){
				
				if (rated.contains(picture))
					continue;
				
				double diff = ImageComparer.difference(goalImage, picture);
				int idx = pictures.indexOf(picture);
				double fitness = 1-diff;
				if (fitness < lowestFitness){
					lowestFitness = fitness;
					lowestIdx = idx;
				}
				if (fitness > bestFitness){
					bestFitness = fitness;
					bestIdx = idx;
				}
			}
			double rating = (double)rated.size() / (double)organisms.size();
			organisms.get(lowestIdx).setFitness(rating);
			rated.add(pictures.get(lowestIdx));
			lowestFitness = Double.MAX_VALUE;
		}
		
		System.out.println("Best fitness: " + bestFitness + "\tby: " + bestIdx + "\tnodes: " + organisms.get(bestIdx).getNet().getAllnodes().size());
		
	}

	private void epoch(Population pop, int generation) {
		
		// Kill bad artists
		Vector<Organism> organisms = pop.getOrganisms();
		List<Organism> killed = new ArrayList<Organism>();
		for(Organism org : organisms){
			if (org.getFitness() < 0.5)
				killed.add(org);
		}
		
		for(Organism kill : killed){
			//kill.getSpecies().remove_org(kill);
			organisms.remove(kill);
			//kill.setEliminate(true);
		}
		
		Organism parent = organisms.get(0);
		Organism lastOrg = parent;
		while(organisms.size() < popSize){
			
			Genome g = lastOrg.getGenome().duplicate(nextId++);
			Organism org = new Organism(0.5, g, generation);
			try{
				if (Math.random() > 0.66)
					org.getGenome().mutate_add_node(pop);
				if (Math.random() > 0.66)
					org.getGenome().mutate_add_link(pop,(int) (Math.random() * 10));
				if (Math.random() > 0.66)
					org.getGenome().mutate_link_weight(Math.random()*2, Math.random()*2, NeatConstant.GAUSSIAN);
				if (Math.random() > 0.66)
					org.getGenome().mutate_link_trait((int) (Math.random() * 10));
				if (Math.random() > 0.66)
					org.getGenome().mutate_node_trait((int) (Math.random() * 10));
				if (Math.random() > 0.66)
					org.getGenome().mutate_random_trait();
			} catch (Exception e){
				System.out.println(e);
			}
			
			organisms.add(org);
			lastOrg = org;
			
		}
		//organisms.remove(parent);
		
		/*
		for(Organism org : organisms){
			
			if (org.getGenome().getGenome_id() == parent.getGenome().getGenome_id())
				continue;
			
			for(int i = 0; i < 1; i++){
				try{
					if (Math.random() > 0.43)
						org.getGenome().mutate_add_node(pop);
					
					if (Math.random() > 0.43)
						org.getGenome().mutate_add_link(pop,(int) (Math.random() * 10));
					//else if (r == 3)
						//org.getGenome().mutate_gene_reenable();
					if (Math.random() > 0.43)
						org.getGenome().mutate_link_weight(Math.random()*2, Math.random()*2, NeatConstant.GAUSSIAN);
					if (Math.random() > 0.43)
						org.getGenome().mutate_link_trait((int) (Math.random() * 10));
					if (Math.random() > 0.43)
						org.getGenome().mutate_node_trait((int) (Math.random() * 10));
					if (Math.random() > 0.43)
						org.getGenome().mutate_random_trait();
					//else if (r < 24)
						//org.getGenome().mutate_toggle_enable((int) (Math.random() * 10));
				} catch (Exception e){
					System.out.println(e);
				}
			}
			
			//break;
			
		}
		*/
		//pop.epoch(generation);
		
		//pop.epochMutateOnly(generation);
	}

	private Organism mutate(Organism org) {
		
		// Generate population
		Population newPop = new Population(	
							1 /* population size */, 
							numInputs /* network inputs */ , 
							numOutputs /* network outputs */, 
							maxNodes /* max index of nodes */, 
							true /* recurrent */, 
							0.5 /* probability of connecting two nodes */ );
		
		Organism other = (Organism) newPop.getOrganisms().get(0);
		
		
		Genome genome = org.getGenome().mate_multipoint(other.getGenome(), nextId++, org.getFitness(), other.getFitness());
		Organism child = new Organism(0.5, genome, org.getGeneration());
		
		
		return child;
		
	}

	private void presentAndRate(Population pop, List<BufferedImage> pictures) {
		
		panel.clearPictures();
		List<Integer> ids = new ArrayList<Integer>();
		for(Object obj : pop.getOrganisms())
			ids.add(((Organism)obj).getGenome().getGenome_id());
		panel.addPictures(pictures, ids);
		
		Vector<Organism> organisms = pop.getOrganisms();
		
		// Reset fitness
		for(Organism org : organisms)
			((Organism)org).setFitness(0);
		
		// While champions not selected keep showing paintings
		while(panel.getSelected().isEmpty()){
			
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
			organisms.get(idx).setFitness(1);
			
		}
		
	}

	private List<BufferedImage> paint(Population neatPop, int width, int height, int paintTime) {
		
		PaintProgram program = new PaintProgram(true, width, height); 
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		Vector<Organism> organisms = neatPop.getOrganisms();
		
		//for(int i=0;i<popSize;i++){
		for(Organism org : organisms){
			// Extract the neural network from the jNEAT organism.
			program = new PaintProgram(true, width, height); 
			Painter painter = organismToPainter(org);
			images.add(program.paintPicture(painter, paintTime, true));
			
		}
		
		return images;
		
	}
	
	private void showImages(List<BufferedImage> pictures) {
		
		this.panel.clearPictures();
		List<Integer> ids = new ArrayList<Integer>();
		//for(Object obj : pop.getOrganisms())
		//	ids.add(((Organism)obj).getGenome().getGenome_id());
		this.panel.addPictures(pictures, ids);
		
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
		
		return new Painter(organism.getNet(), organism.getGenome().getGenome_id());
		
	}

	private Organism bestOrganism(Population neatPop) {
		
		double bestFitness = Double.MIN_VALUE;
		Organism mostFit = null;
		
		Vector<Organism> organisms = neatPop.getOrganisms();
		
		for(Organism org : organisms){
			
			if (org.getFitness() > bestFitness){
				bestFitness = org.getFitness();
				mostFit = org;
			}
		}
		return mostFit;
		
	}
	
}
