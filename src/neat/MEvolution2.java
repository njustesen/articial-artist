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
					1 /* population size */, 
					numInputs /* network inputs */ , 
					numOutputs /* network outputs */, 
					maxNodes /* max index of nodes */, 
					true /* recurrent */, 
					0.5 /* probability of connecting two nodes */ ));
		}
		
		// Run evolution
		for(int i = 1; i <= iterations; i++){
			
			if (i == iterations)
				break;
			
			pictures = paint(imgWidth, imgHeight, paintTime);
			
			pickBest();
			
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
				org.getGenome().mutate_add_link(pop,(int) (Math.random() * 1));
			}
			//if (Math.random() > 0.5){
				org.getGenome().mutate_link_weight(Math.random()*1, Math.random()*1, NeatConstant.GAUSSIAN);
			//}
			if (Math.random() > 0.5){
				org.getGenome().mutate_link_trait((int) (Math.random() * 1));
			}
			if (Math.random() > 0.5){
				org.getGenome().mutate_node_trait((int) (Math.random() * 1));
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

	private void pickBest() {
		
		panel.clearPictures();
		List<Integer> ids = new ArrayList<Integer>();
		for(Population pop : pops)
			ids.add(pops.indexOf(pop));
		
		panel.addPictures(pictures, ids);
		
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
		
		// Return best
		BufferedImage bestPic = panel.getSelected().get(0);
		int idx = panel.getPictures().indexOf(bestPic);
		
		Population best = pops.get(idx);
		pops.clear();
		pops.add(best);
		if (best.getOrganisms().size()>1)
			best.getOrganisms().remove(1);
		
		pictures.clear();
		pictures.add(bestPic);
		
	}
	
	private Population presentAndRate(List<Population> pops) {
		
		panel.clearPictures();
		List<Integer> ids = new ArrayList<Integer>();
		for(Population pop : pops){
			for(Object obj : pop.getOrganisms()){
				ids.add(((Organism)obj).getGenome().getGenome_id());
				((Organism)obj).setFitness(0);
			}
		}
		panel.addPictures(pictures, ids);
		
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
			return pops.get(idx);
			
		}
		return null;
		
	}

	private List<BufferedImage> paint(int width, int height, int paintTime) {
		
		PaintProgram program = new PaintProgram(true, width, height); 
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		//for(int i=0;i<popSize;i++){
		for(Population pop : pops){
			// Extract the neural network from the jNEAT organism.
			program = new PaintProgram(true, width, height); 
			Organism org = ((Organism)pop.getOrganisms().get(0));
			Painter painter = organismToPainter(org);
			images.add(program.paintPicture(painter, paintTime, true));
		}
		
		return images;
		
	}
	
	private Painter organismToPainter(Organism organism) {
		
		return new Painter(organism.getNet(), organism.getGenome().getGenome_id());
		
	}

}
