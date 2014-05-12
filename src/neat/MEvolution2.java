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
import config.Config;

import paint.PaintProgram;

import jneat.*;

public class MEvolution2 {
	
	int numOutputs;
	int numInputs;
	int popSize;
	int maxNodes;
	EvolutionPanel panel;
	private int imgWidth;
	private int imgHeight;
	private BufferedImage goalImage;
	private List<Population> pops;
	private int nextId = 90000;
	private List<BufferedImage> pictures;
	private double fitness = 1;

	public MEvolution2(int numInputs, int numOutputs, int popSize, int maxNodes, int picWidth, int picHeight, BufferedImage goal){
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
		pictures = new ArrayList<BufferedImage>();
		pops = new ArrayList<Population>();
	}
	
	public Painter evolvePainter(int paintTime, int iterations, boolean goal){
		
		Neat.initbase();
		
		// Generate populations
		while(pops.size() < popSize){
			pops.add(new Population(	
					3 /* population size */, 
					numInputs /* network inputs */ , 
					numOutputs /* network outputs */, 
					maxNodes /* max index of nodes */, 
					true /* recurrent */, 
					0.5 /* probability of connecting two nodes */ ));
		}
		
		// Run evolution
		System.out.println("Epoch\tFitness");
		for(int i = 1; i <= iterations; i++){
			
			if (i == iterations)
				break;
			
			pictures = paint(pops, imgWidth, imgHeight, paintTime, true);
			
			pickBest(goal, i, paintTime);
			//System.out.println(i + "\t" + fitness);
			
			mutate(pops.get(0), i);
			
		}
		
		// Return best painter
		Organism best = ((Organism)pops.get(0).getOrganisms().get(0));
		return organismToPainter(best);
		
	}

	private void mutate(Population best, int generation) {
		
		Organism org = ((Organism)best.getOrganisms().get(0));
		
		best.print_to_filename("best.pop");
		
		while(pops.size() < popSize){
			
			Population clone = new Population("best.pop");
			pops.add(clone);
			
			//Organism org = ((Organism)clone.getOrganisms().get(0));
			//Genome g = ((Organism)clone.getOrganisms().get(0)).getGenome().duplicate(nextId++);
			//Organism org = new Organism(0.5, g, generation);
			//org.genome.setGenome_id(nextId++);
			
			mutate(clone);
			
		}
	}

	private void mutate(Population pop) {
		
		Organism org = ((Organism)pop.getOrganisms().get(0));
		
		try{
			if (Math.random() > 0.50){
				org.getGenome().mutate_add_node(pop);
			}
			if (Math.random() > 0.50){
				org.getGenome().mutate_add_link(pop,(int) (Math.random() * Config.mutationRate));
			}
			//if (Math.random() > 0.5){
				org.getGenome().mutate_link_weight(Math.random()*Config.mutationRate, Math.random()*Config.mutationRate, NeatConstant.GAUSSIAN);
			//}
			if (Math.random() > 0.5){
				org.getGenome().mutate_link_trait((int) (Math.random() * Config.mutationRate));
			}
			if (Math.random() > 0.5){
				org.getGenome().mutate_node_trait((int) (Math.random() * Config.mutationRate));
			}
			if (Math.random() > 0.5){
				org.getGenome().mutate_random_trait();
			}
		} catch (Exception e){
			System.out.println(e);
		}
		
		//pop.epochMutateOnly(0);
		//pop.d_pop_size = "1.0";
		pop.epoch(0);
	}

	private void pickBest(boolean goal, int i, int paintTime) {
		
		panel.clearPictures();
		List<Integer> ids = new ArrayList<Integer>();
		for(Population pop : pops)
			ids.add(pops.indexOf(pop));
		
		panel.addPictures(pictures, ids);
		
		BufferedImage bestPic = null;
		
		if(goal){
			double lowestDiff = Double.MAX_VALUE;
			int idx = 0;
			for(BufferedImage picture : pictures){
				double diff = ImageComparer.difference(goalImage, picture);
				if (diff < lowestDiff){
					idx = pictures.indexOf(picture);
					lowestDiff = diff;
				}
			}
			fitness = 1-lowestDiff;
			bestPic = pictures.get(idx);
		} else {
			// While champions not selected keep showing paintings
			while(panel.getSelected().isEmpty()){
				panel.repaint();
				
				if (panel.shiftSelected != -1) {
					List<Population> p = new ArrayList<Population>();
					p.add(pops.get(panel.shiftSelected));
					boolean si = Config.soundInput;
					Config.soundInput = true;
					paint(p, Config.pictureWidth, Config.pictureHeight, paintTime, false);
					Config.soundInput = si;
					panel.shiftSelected = -1;
				}
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Wait 1000 ms
			panel.repaint();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Return best
			bestPic = panel.getSelected().get(0);
			
			// Save image
			File outputfile = new File(Config.outputFolder + "best_" + i + ".png");
			try {
				ImageIO.write(bestPic, "png", outputfile);
			} catch (IOException e) {
				System.out.println("Unable to save file to " + outputfile.getName());
				e.printStackTrace();
			}
			
			
		}
		
		
		int idx = panel.getPictures().indexOf(bestPic);
		
		Population best = pops.get(idx);
		pops.clear();
		pops.add(best);
		if (best.getOrganisms().size()>1)
			best.getOrganisms().remove(1);
		
		pictures.clear();
		pictures.add(bestPic);
		
	}

	private List<BufferedImage> paint(List<Population> populations, int width, int height, int paintTime, boolean autoClose) {
		
		PaintProgram program = new PaintProgram(true, width, height); 
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		//for(int i=0;i<popSize;i++){
		for(Population pop : populations){
			// Extract the neural network from the jNEAT organism.
			program = new PaintProgram(true, width, height); 
			Organism org = ((Organism)pop.getOrganisms().get(0));
			Painter painter = organismToPainter(org);
			images.add(program.paintPicture(painter, paintTime, autoClose));
		}
		
		return images;
		
	}
	
	private Painter organismToPainter(Organism organism) {
		
		return new Painter(organism.getNet(), organism.getGenome().getGenome_id());
		
	}

}
