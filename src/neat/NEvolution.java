package neat;
import java.awt.font.ImageGraphicAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;

import comparer.ImageComparer;

import paint.PaintProgram;

import jneat.*;

public class NEvolution {
	
	int numOutputs;
	int numInputs;
	int popSize;
	int maxNodes;
	EvolutionPanel panel;
	private int imgWidth;
	private int imgHeight;
	private int champions;
	private int novelty;
	private BufferedImage goalImage;
	private List<Organism> Npopulation;
	private int nextId = 90000;

	public NEvolution(int numInputs, int numOutputs, int popSize, int maxNodes, int picWidth, int picHeight, int champions, int novelty, BufferedImage goal){
		super();
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.popSize = popSize;
		this.maxNodes = maxNodes;
		this.imgWidth = picWidth;
		this.imgHeight = picHeight;
		this.champions = champions;
		this.novelty = novelty;
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
		Population pop = new Population(	
									popSize /* population size */, 
									numInputs /* network inputs */ , 
									numOutputs /* network outputs */, 
									maxNodes /* max index of nodes */, 
									true /* recurrent */, 
									0.5 /* probability of connecting two nodes */ );
		
		Npopulation = new ArrayList<Organism>(pop.organisms);
		
		// Run evolution
		for(int i = 1; i <= iterations; i++){
			
			// Paint pictures
			List<BufferedImage> pictures = paint(Npopulation, imgWidth, imgHeight, paintTime);
			
			// Show pictures and wait for feedback
			if (goal)
				presentAndCompare(Npopulation, pictures);
			else
				presentAndRate(Npopulation, pictures);
			// Assign random fitness
			//assignRandomFitness(neatPop, i);
			
			if (i == iterations)
				break;
			
			// Evolutionize
			//neatPop.epoch(i); // Evolve the population and increment the generation.
			//if (!goal)
			epoch(Npopulation, i);
			
			//for(int e = 0; e<10; e++){
			//pop.epoch(i);
			//System.out.println(neatPop.getOrganisms().size() + " organisms. ");
			//}
			
		}
		
		// Return best painter
		Organism best = bestOrganism(Npopulation);
		return organismToPainter(best);
		
	}

	private void presentAndCompare(List<Organism> pop,
			List<BufferedImage> pictures) {
		
		panel.clearPictures();
		panel.addPictures(pictures);
		
		// Reset fitness
		for(Organism org : pop)
			org.setFitness(0);
		
		// Give fitness to selected pictures
		List<BufferedImage> rated = new ArrayList<BufferedImage>();
		
		double lowestFitness = Double.MAX_VALUE;
		double bestFitness = Double.MIN_VALUE;
		int lowestIdx = 0;
		int bestIdx = 0;
		
		while(rated.size()<pop.size()){
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
			double rating = (double)rated.size() / (double)pop.size();
			pop.get(lowestIdx).setFitness(rating);
			rated.add(pictures.get(lowestIdx));
			lowestFitness = Double.MAX_VALUE;
		}
		
		System.out.println("Best fitness: " + bestFitness + "\tby: " + bestIdx);
		
	}

	private List<Organism> epoch(List<Organism> pop, int generation) {
		
		// Kill bad artists
		List<Organism> killed = new ArrayList<Organism>();
		for(Organism org : pop){
			if (org.getFitness() < 0.5){
				org.setEliminate(true);
				killed.add(org);
			}
		}
		
		for(Organism kill : killed){
			pop.remove(kill);
		}
		
		// Reproduce
		List<Organism> babies = new ArrayList<Organism>();
		for(Organism org : pop){
			
			int idx = -1;
			while(idx == -1 || idx == pop.indexOf(org)){
				idx = (int) Math.floor(Math.random() * pop.size());
			}
			
			Organism other = pop.get(idx);
			
			Genome genome = org.getGenome().mate_multipoint(other.getGenome(), nextId++, org.getFitness(), other.getFitness());
			Organism child = new Organism(0.5, genome, org.getGeneration()+1);
			babies.add(child);
		}
		
		for(Organism baby : babies){
			pop.add(baby);
		}
		
		return pop;
	}

	private void presentAndRate(List<Organism> pop, List<BufferedImage> pictures) {
		
		panel.clearPictures();
		panel.addPictures(pictures);
		
		// Reset fitness
		for(Organism org : pop)
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
			pop.get(idx).setFitness(1);
			
		}
		
	}

	private List<BufferedImage> paint(List<Organism> neatPop, int width, int height, int paintTime) {
		
		PaintProgram program = new PaintProgram(false, width, height); 
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		//for(int i=0;i<popSize;i++){
		for(Organism org : neatPop){
			// Extract the neural network from the jNEAT organism.
			Painter painter = organismToPainter(org);
			
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

	private Organism bestOrganism(List<Organism> neatPop) {
		
		double bestFitness = Double.MIN_VALUE;
		Organism mostFit = null;
		
		for(Organism org : neatPop){
			
			if (org.getFitness() > bestFitness){
				bestFitness = org.getFitness();
				mostFit = org;
			}
		}
		return mostFit;
		
	}
	
}
