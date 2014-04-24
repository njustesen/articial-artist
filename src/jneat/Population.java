/* Generated by Together */

   package jneat;

   import java.util.*;

   import java.text.*;
   import jNeatCommon.*;

/** A Population is a group of Organisms including their species */
	public class Population extends Neat {
   /** The organisms in the Population */
	  public Vector organisms;
   
   /** Species in the Population the species should comprise all the genomes */
	  public Vector species;
   
   /** For holding the genetic innovations of the newest generation */
	  Vector innovations = new Vector(1,0);
   
   /** Current label number available for nodes */
	  private int cur_node_id;
   
   /** Current  number of innovation */
	  private double cur_innov_num;
   
   /** The highest species number */
	  int last_species;
   
   /** The last generation played */
	  int final_gen;
   //Fitness Statistics
   
   /** the mean of fitness in current epoch */
	  double mean_fitness;
   
   /** current variance in this epoch */
	  double variance;
   
   /** Is a current standard deviation in current epoch */
	  double standard_deviation;
   
   /** An integer that when above zero tells when the first winner appeared; the number is epoch number. */
	  int winnergen;
   
   /** maximum fitness. (is used for delta code and stagnation detection) */
	  double highest_fitness;
   
   /** If  too high, leads to delta coding process. */
	  int highest_last_changed;

		public int inputNodes;
		
		public int outputNodes;
		
		public int nodesMax;
		
		public boolean recurrent;

		public double linkProp;
   
	   public Vector getOrganisms() {
		 return organisms;
	  }            
   
	   public void setOrganisms(Vector organisms) {
		 this.organisms = organisms;
	  }            
   
	   public Vector getSpecies() {
		 return species;
	  }            
   
	   public void setSpecies(Vector species) {
		 this.species = species;
	  }            
   
	   public Vector getInnovations() {
		 return innovations;
	  }            
   
	   public void setInnovations(Vector innovations) {
		 this.innovations = innovations;
	  }            
   
	   public int getCur_node_id() {
		 return cur_node_id;
	  }            
   
	   public void setCur_node_id(int cur_node_id) {
		 this.cur_node_id = cur_node_id;
	  }            
   
	   public double getCur_innov_num() {
		 return cur_innov_num;
	  }            
   
	   public void setCur_innov_num(double cur_innov_num) {
		 this.cur_innov_num = cur_innov_num;
	  }            
   
	   public int getLast_species() {
		 return last_species;
	  }            
   
	   public void setLast_species(int last_species) {
		 this.last_species = last_species;
	  }            
   
	   public int getFinal_gen() {
		 return final_gen;
	  }            
   
	   public void setFinal_gen(int final_gen) {
		 this.final_gen = final_gen;
	  }            
   
	   public double getMean_fitness() {
		 return mean_fitness;
	  }            
   
	   public void setMean_fitness(double mean_fitness) {
		 this.mean_fitness = mean_fitness;
	  }            
   
	   public double getVariance() {
		 return variance;
	  }            
   
	   public void setVariance(double variance) {
		 this.variance = variance;
	  }            
   
	   public double getStandard_deviation() {
		 return standard_deviation;
	  }            
   
	   public void setStandard_deviation(double standard_deviation) {
		 this.standard_deviation = standard_deviation;
	  }            
   
	   public int getWinnergen() {
		 return winnergen;
	  }            
   
	   public void setWinnergen(int winnergen) {
		 this.winnergen = winnergen;
	  }            
   
	   public double getHighest_fitness() {
		 return highest_fitness;
	  }            
   
	   public void setHighest_fitness(double highest_fitness) {
		 this.highest_fitness = highest_fitness;
	  }            
   
	   public int getHighest_last_changed() {
		 return highest_last_changed;
	  }            
   
	   public void setHighest_last_changed(int highest_last_changed) {
		 this.highest_last_changed = highest_last_changed;
	  }            
   
	   public Population(Genome g, int size) 
	  {
		 winnergen = 0;
		 highest_fitness = 0.0;
		 highest_last_changed = 0;
		 spawn(g, size);
	  }            
   
	   public void spawn(Genome g, int size) 
	  {
		 int count;
		 Genome newgenome = null;
		 Organism neworganism = null;
		 organisms = new Vector(size);
		 for (count = 1; count <= size; count++) 
		 {
		 //	  System.out.print("\n Creating organism -> " + count);
			newgenome = g.duplicate(count);
			newgenome.mutate_link_weight(1.0, 1.0, NeatConstant.GAUSSIAN);
			neworganism = new Organism(0.0, newgenome, 1);
			organisms.add(neworganism);
		 }
	  
	  //Keep a record of the innovation and node number we are on
		 cur_node_id = newgenome.get_last_node_id();
		 cur_innov_num = newgenome.get_last_gene_innovnum();
	  
	  //Separate the new Population into species
		 speciate();
	  
	  }                           
   
   /**
   *
   *
   */
	   public void viewtext() 
	  {
	  
		 System.out.print("\n\n\n\t\t *P O P U L A T I O N*");
		 System.out.print("\n\n\t This population has " + organisms.size() + " organisms, "); 
		 System.out.print(species.size() + " species :\n");
		 Iterator itr_organism = organisms.iterator();
		 itr_organism = organisms.iterator();
	  
		 while (itr_organism.hasNext()) 
		 {
			Organism _organism = ((Organism) itr_organism.next());
			_organism.viewtext();
		 }
	  
		 Iterator itr_specie = species.iterator();
		 itr_specie = species.iterator();
	  
		 while (itr_specie.hasNext()) 
		 {
			Species _specie = ((Species) itr_specie.next());
			_specie.viewtext();
		 }
	  
	  }
   /**
   *
   * epoch turns over a Population to
   * the next generation based on fitness
   *
   */
	   public void epoch(int generation) 
	  {
	  
		 Iterator itr_specie;
		 Iterator itr_organism;
		 double total = 0.0;
	  //double total_expected=0.0;
		 int orgcount = 0;
		 int max_expected;
		 int total_expected; //precision checking
		 int final_expected;
		 int half_pop = 0;
		 double overall_average = 0.0;
		 int total_organisms = 0;
		 double tmpd = 0.0;
		 double skim = 0.0;
		 int tmpi = 0;
		 int best_species_num = 0;
		 int stolen_babies = 0;
		 int one_fifth_stolen = 0;
		 int one_tenth_stolen = 0;
		 int size_of_curr_specie = 0;
		 int NUM_STOLEN = Neat.p_babies_stolen; //Number of babies to steal
	  // al momento NUM_STOLEN=1
	  
		 Species _specie = null;
		 Species curspecies = null;
		 Species best_specie = null;
		 Vector sorted_species = null;
	  
	  
	  // Use Species' ages to modify the objective fitness of organisms
	  // in other words, make it more fair for younger species
	  // so they have a chance to take hold
	  // Also penalize stagnant species
	  // Then adjust the fitness using the species size to "share" fitness
	  // within a species.
	  // Then, within each Species, mark for death 
	  // those below survival_thresh * average
	  
//		 itr_specie = species.iterator();
//		 while (itr_specie.hasNext()) 
//		 {
//			_specie = ((Species) itr_specie.next());
//			_specie.adjust_fitness();
//		 }
	  
	  //Go through the organisms and add up their fitnesses to compute the
	  //overall average
	  
		 itr_organism = organisms.iterator();
		 total = 0.0;
		 while (itr_organism.hasNext()) 
		 {
			Organism _organism = ((Organism) itr_organism.next());
			total += _organism.fitness;
		 }
	  
		 total_organisms = organisms.size();
		 overall_average = total / total_organisms;
	  
	  //Now compute expected number of offspring for each individual organism
	  //
		 itr_organism = organisms.iterator();
		 int orgnum = 0;
		 while (itr_organism.hasNext()) 
		 {
			Organism _organism = ((Organism) itr_organism.next());
			_organism.expected_offspring = _organism.fitness / overall_average;
			// EDIT: was fitness / average
		 }
	  
	  //Now add those offspring up within each Species to get the number of
	  //offspring per Species
		 skim = 0.0;
		 total_expected = 0;
		 int specount = 0;
		 itr_specie = species.iterator();
		 while (itr_specie.hasNext()) 
		 {
			_specie = ((Species) itr_specie.next());
			skim = _specie.count_offspring(skim);
			total_expected += _specie.expected_offspring;
		 }
	  
	  //Need to make up for lost foating point precision in offspring assignment
	  //If we lost precision, give an extra baby to the best Species
	  
		 if (total_expected < total_organisms) 
		 {
		 
		 //Find the Species expecting the most
			max_expected = 0;
			final_expected = 0;
			itr_specie = species.iterator();
		 
			while (itr_specie.hasNext()) 
			{
			   _specie = ((Species) itr_specie.next());
			   if (_specie.expected_offspring >= max_expected) 
			   {
				  max_expected = _specie.expected_offspring;
				  best_specie = _specie;
			   }
			   final_expected += _specie.expected_offspring;
			}
		 //Give the extra offspring to the best species
		 
			best_specie.expected_offspring++;
			final_expected++;
		 
		 //If we still arent at total, there is a problem
		 //Note that this can happen if a stagnant Species
		 //dominates the population and then gets killed off by its age
		 //Then the whole population plummets in fitness
		 //If the average fitness is allowed to hit 0, then we no longer have 
		 //an average we can use to assign offspring.
			if (final_expected < total_organisms) 
			{
			   System.out.print("\n Sorry : Population .has DIED +");
			   System.out.print("\n ------------------------------");
			   itr_specie = species.iterator();
			   while (itr_specie.hasNext()) 
			   {
				  _specie = ((Species) itr_specie.next());
				  _specie.expected_offspring = 0;
			   }
			   best_specie.expected_offspring = total_organisms;
			}
		 }
	  
	  
		 sorted_species = new Vector(species.size(), 0);
	  //copy the Species pointers into a new Species list for sorting
		 itr_specie = species.iterator();
		 while (itr_specie.hasNext()) 
		 {
			_specie = ((Species) itr_specie.next());
			sorted_species.add(_specie);
		 }
	  
	  //Sort the population and mark for death those after survival_thresh * pop_size
	  
		 Comparator cmp = new order_species();
		 Collections.sort(sorted_species, cmp);
	  
	  // sorted species has all species ordered : the species with orig_fitness maximum is first
	  
		 curspecies = (Species) sorted_species.firstElement();
		 best_species_num = curspecies.id;
	  
	  
	  
	  
		 StringBuffer rep1 = new StringBuffer("");
	  //   	System.out.print("\n  The BEST specie is #" + best_species_num);
		 rep1.append("\n  the BEST  specie is #" + best_species_num);
	  
	  
	  
	  // report current situation
	  
		 itr_specie = sorted_species.iterator();
		 while (itr_specie.hasNext()) 
		 {
			_specie = ((Species) itr_specie.next());
		 //	  	System.out.print("\n  orig fitness of Species #" + _specie.id);
			rep1.append("\n  orig fitness of Species #" + _specie.id);
		 
		 //	  	System.out.print(" (Size " + _specie.getOrganisms().size() + "): ");
			rep1.append(" (Size " + _specie.getOrganisms().size() + "): ");
		 
		 // 	  	System.out.print(" is " + ((Organism) (_specie.organisms.firstElement())).orig_fitness); 
			rep1.append(" is " + ((Organism) (_specie.organisms.firstElement())).orig_fitness); 
		 
		 // 	  	System.out.print(" last improved ");
			rep1.append(" last improved ");
		 
		 //	  	System.out.print(_specie.age - _specie.age_of_last_improvement);
			rep1.append(_specie.age - _specie.age_of_last_improvement);
		 
		 //	  	System.out.print(" offspring "+_specie.expected_offspring);
			rep1.append(" offspring "+_specie.expected_offspring);
		 
		 }
	  
	  
		 EnvConstant.REPORT_SPECIES_TESTA = rep1.toString();
		 rep1 = new StringBuffer("");
	  
	  
	  
	  
	  
		 curspecies = (Species) sorted_species.firstElement();
	  
	  //Check for Population-level stagnation
		 ((Organism) curspecies.getOrganisms().firstElement()).pop_champ = true;
	  
	  
		 Organism tmp = (Organism) curspecies.getOrganisms().firstElement();
	  
	  
		 if (((Organism) curspecies.organisms.firstElement()).orig_fitness > highest_fitness) 
		 {
			highest_fitness = ((Organism) curspecies.organisms.firstElement()).orig_fitness; 
			highest_last_changed = 0;
		 //	  	System.out.print("\n    Good! Population has reached a new *RECORD FITNESS* -> " + highest_fitness);
			rep1.append("\n    population has reached a new *RECORD FITNESS* -> " + highest_fitness);


			// 01.06.2002
						EnvConstant.CURR_ORGANISM_CHAMPION = tmp;




			
			EnvConstant.MIN_ERROR = ((Organism) curspecies.organisms.firstElement()).getError();
		 
		 
		 } 
		 else 
		 {
			++highest_last_changed;
			EnvConstant.REPORT_SPECIES_TESTA = "";
		 
		 //	  	System.out.print("\n  Are passed "+ highest_last_changed+ " generations from last population fitness record: "+ highest_fitness); 
			rep1.append("\n    are passed "+ highest_last_changed+ " generations from last population fitness record: "+ highest_fitness); 
		 }
	  
	  
	  
		 EnvConstant.REPORT_SPECIES_CORPO = rep1.toString();	
	  
	  //Check for stagnation- if there is stagnation, perform delta-coding
	  
		 if (highest_last_changed >= Neat.p_dropoff_age + 5) 
		 {
		 //------------------ block delta coding ---------------------------- 
			System.out.print("\n+  <PERFORMING DELTA CODING>");
			highest_last_changed = 0;
			half_pop = Neat.p_pop_size / 2;
			tmpi = Neat.p_pop_size - half_pop;
			System.out.print("\n  Pop size is " + Neat.p_pop_size);
			System.out.print(", half_pop=" + half_pop + ",   pop_size - halfpop=" + tmpi);
		 
			itr_specie = sorted_species.iterator();
			_specie = ((Species) itr_specie.next());
		 
		 // the first organism of first species can have  offspring = 1/2 pop size 
			((Organism) _specie.organisms.firstElement()).super_champ_offspring = half_pop; 
		 // the first species  can have offspring = 1/2 pop size
			_specie.expected_offspring = half_pop;
			_specie.age_of_last_improvement = _specie.age;
		 
			if (itr_specie.hasNext()) 
			{
			   _specie = ((Species) itr_specie.next());
			   ((Organism) _specie.organisms.firstElement()).super_champ_offspring = half_pop;
			// the second species  can have offspring = 1/2 pop size
			   _specie.expected_offspring = half_pop;
			   _specie.age_of_last_improvement = _specie.age;
			// at this moment the offpring is terminated : the remainder species has 0 offspring!
			   while (itr_specie.hasNext()) 
			   {
				  _specie = ((Species) itr_specie.next());
				  _specie.expected_offspring = 0;
			   }
			} 
			else
			{
			   ((Organism) _specie.organisms.firstElement()).super_champ_offspring += Neat.p_pop_size - half_pop;
			   _specie.expected_offspring += Neat.p_pop_size - half_pop;
			}
		 
		 } 
		 else
		 {
		 // --------------------------------- block baby stolen (if baby stolen > 0)  -------------------------
		 //		System.out.print("\n   Starting with NUM_STOLEN = "+NUM_STOLEN);
		 
			if (Neat.p_babies_stolen > 0) 
			{
			   _specie = null;
			//Take away a constant number of expected offspring from the worst few species
			   stolen_babies = 0;
			   for (int j = sorted_species.size() - 1; (j >= 0) && (stolen_babies < NUM_STOLEN); j--) 
			   {
				  _specie = (Species) sorted_species.elementAt(j);
			   //				System.out.print("\n Analisis SPECIE #"+j+" (size = "+_specie.organisms.size()+" )");
				  if ((_specie.age > 5) && (_specie.expected_offspring > 2)) 
				  {
				  //		System.out.print("\n ....STEALING!");
					 tmpi = NUM_STOLEN - stolen_babies;
					 if ((_specie.expected_offspring - 1) >= tmpi) 
					 {
						_specie.expected_offspring -= tmpi;
						stolen_babies = NUM_STOLEN;
					 } 
					 else
					 //Not enough here to complete the pool of stolen
					 {
						stolen_babies += _specie.expected_offspring - 1;
						_specie.expected_offspring = 1;
					 }
				  }
			   }
			
			
			
			
			//		 	System.out.print("\n stolen babies = "+ stolen_babies);
			//Mark the best champions of the top species to be the super champs
			//who will take on the extra offspring for cloning or mutant cloning
			//Determine the exact number that will be given to the top three
			//They get , in order, 1/5 1/5 and 1/10 of the stolen babies
			
			   int tb_four[] = new int[3];
			   tb_four[0] = Neat.p_babies_stolen / 5;
			   tb_four[1] = tb_four[0];
			   tb_four[2] = Neat.p_babies_stolen / 10;
			
			   boolean done = false;
			   itr_specie = sorted_species.iterator();
			   int i_block = 0;
			
			
			
			   while (!done && itr_specie.hasNext()) 
			   {
				  _specie = ((Species) itr_specie.next());
				  if (_specie.last_improved() <= Neat.p_dropoff_age) 
				  {
					 if (i_block < 3) 
					 {
						if (stolen_babies >= tb_four[i_block]) 
						{
						   ((Organism) _specie.organisms.firstElement()).super_champ_offspring = tb_four[i_block]; 
						   _specie.expected_offspring += tb_four[i_block];
						   stolen_babies -= tb_four[i_block];
						   System.out.print("\n  give "+tb_four[i_block]+" babies to specie #" +_specie.id); 
						}
						i_block++;
					 }
					 
					 
					 else if (i_block >= 3) 
					 {
						if (NeatRoutine.randfloat() > 0.1) 
						{
						   if (stolen_babies > 3) 
						   {
							  ((Organism) _specie.organisms.firstElement()).super_champ_offspring = 3;
							  _specie.expected_offspring += 3;
							  stolen_babies -= 3;
							  System.out.print("\n    Give 3 babies to Species " + _specie.id);
						   } 
						   else 
						   {
							  ((Organism) _specie.organisms.firstElement()).super_champ_offspring = stolen_babies; 
							  _specie.expected_offspring += stolen_babies;
							  System.out.print("\n    Give "+stolen_babies+" babies to Species " + _specie.id);
							  stolen_babies = 0;
						   }
						}
						if (stolen_babies == 0)
						   done = true;
					 }
				  }
			   }
			
			   if (stolen_babies > 0) 
			   {
				  System.out.print("\n Not all given back, giving to best Species");
				  itr_specie = sorted_species.iterator();
				  _specie = ((Species) itr_specie.next());
				  ((Organism) _specie.organisms.firstElement()).super_champ_offspring += stolen_babies; 
				  _specie.expected_offspring += stolen_babies;
				  System.out.print("\n    force +" + stolen_babies+" offspring to Species " + _specie.id);
				  stolen_babies = 0;
			   }
			} // end baby_stolen > 0
		 
		 }  	
	  // ---------- phase of elimination of organism with flag eliminate ------------
		 itr_organism = organisms.iterator();
		 Vector vdel = new Vector(organisms.size());
	  
		 while (itr_organism.hasNext()) 
		 {
			Organism _organism = ((Organism) itr_organism.next());
			if (_organism.eliminate) 
			{
			//Remove the organism from its Species
			   _specie = _organism.species;
			   _specie.remove_org(_organism);
			//store the organism can be elimanated;
			   vdel.add(_organism);
			}
		 }
	  //eliminate organism from master list
		 for (int i = 0; i < vdel.size(); i++) 
		 {
			Organism _organism = (Organism) vdel.elementAt(i);
		 //  		organisms.remove(_organism);
			organisms.removeElement(_organism);
		 }
	  
	  
	  
		 vdel.clear();
	  
	  
	  // ---------- phase of reproduction -----------
	  /*   	 System.out.print("\n ---- Reproduction at time " + generation+" ----");
	  System.out.print("\n    species   : "+ sorted_species.size());
	  System.out.print("\n    organisms : "+ organisms.size());
	  System.out.print("\n    cur innov num : "+cur_innov_num);
	  System.out.print("\n    cur node num  : "+cur_node_id);
	  System.out.print("\n ---------------------------------------------");
	  System.out.print("\n Start reproduction of species ....");
	  */   	
		 boolean rc = false;
	  
		 itr_specie = sorted_species.iterator();
	  // System.out.print("\n verifica");
	  //System.out.print("\n this species has "+sorted_species.size()+" elements");
		 while (itr_specie.hasNext()) 
		 {
			_specie = ((Species) itr_specie.next());
			rc = _specie.reproduce(generation, this, sorted_species);
		 }
	  
	  //   	System.out.print("\n Reproduction completed");
	  
	  
	  
	  //
	  //Destroy and remove the old generation from the organisms and species
	  // (because we have pointer to organisms , the new organisms created
	  //  are not in organisms and can't br eliminated;
	  // thus are elimate onlyu corrent organisms !)
	  
	  
	  
	  
	  //------prima---------------------------------------
	  /*   	
	  	
	  itr_organism = organisms.iterator();
	  vdel = new Vector(organisms.size());
	  
	  while (itr_organism.hasNext()) 
	  {
	  Organism _organism = ((Organism) itr_organism.next());
	  //Remove the organism from its Species
	  _specie = _organism.species;
	  _specie.remove_org(_organism);
	  //store the organism can be elimanated;
	  vdel.add(_organism);
	  }
	  
	  //eliminate organism from master list
	  for (int i = 0; i < vdel.size(); i++) 
	  {
	  Organism _organism = (Organism) vdel.elementAt(i);
	  //	  	organisms.remove(_organism);
	  organisms.removeElement(_organism);
	  }
	  
	  */
	  
	  //---------------------------------------------
	  
	  
	  
	  
	  
	  //------dopo---------------------------------------
		 itr_organism = organisms.iterator();
	  
	  
	  
	  
		 while (itr_organism.hasNext()) 
		 {
			Organism _organism = ((Organism) itr_organism.next());
		 //Remove the organism from its Species
			_specie = _organism.species;
			_specie.remove_org(_organism);
		 }
	  
		 organisms.clear();
	  
	  //Remove all empty Species and age ones that survive
	  //As this happens, create master organism list for the new generation
	  
		 itr_specie = species.iterator();
		 int i_specie = 0;
	  
		 vdel = new Vector(species.size());
		 orgcount = 0;
	  
		 while (itr_specie.hasNext()) 
		 {
			_specie = ((Species) itr_specie.next());
			size_of_curr_specie = _specie.organisms.size();
			i_specie++;
			if (size_of_curr_specie == 0)
			   vdel.add(_specie);
			else 
			{
			//Age any Species that is not newly created in this generation
			   if (_specie.novel)
				  _specie.novel = false;
			   else
				  _specie.age++;
			//from the current species  recostruct thge master list organisms
			   for (int j = 0; j < size_of_curr_specie; j++) 
			   {
				  Organism _organism = (Organism) _specie.organisms.elementAt(j);
				  _organism.genome.genome_id = orgcount++;
			   //add ugo ******************************************************************************************
				  _organism.genome.phenotype.net_id = _organism.genome.genome_id;
			   
				  organisms.add(_organism);
			   }
			}
		 }
	  
	  // System.out.print("\n the number of species can be eliminated is "+vdel.size());
	  //eliminate species marked from master list
		 for (int i = 0; i < vdel.size(); i++) 
		 {
			_specie = (Species) vdel.elementAt(i);
		 //	  	species.remove(_specie);
			species.removeElement(_specie);
		 
		 }
	  
	  //Remove the innovations of the current generation
	  
		 innovations.clear();
	  
	  /*   	
	  innovations.removeAllElements();
	  innovations.trimToSize();
	  */ 	
	  
	  //DEBUG: Check to see if the best species died somehow
	  // We don't want this to happen
	  
		 itr_specie = species.iterator();
		 boolean best_ok = false;
	  
		 while (itr_specie.hasNext()) 
		 {
			_specie = ((Species) itr_specie.next());
			if (_specie.id == best_species_num) 
			{
			   best_ok = true;
			   break;
			}
		 
		 }
	  
	  
	  
	  
	  
	  
	  /*   			
	  if (!best_ok)
	  System.out.print("\n  <ALERT>  THE BEST SPECIES DIED!");
	  else
	  System.out.print("\n  Good : the best Specie #" + best_species_num+" survived ");
	  */
	  
	  
		 if (!best_ok)
			EnvConstant.REPORT_SPECIES_CODA = "\n  <ALERT>  THE BEST SPECIES DIED!";
		 else
			EnvConstant.REPORT_SPECIES_CODA = "\n  Good : the best Specie #" + best_species_num+" survived ";
	  
	  
	  
	  
		 itr_organism = organisms.iterator();
		 vdel = new Vector(organisms.size());
	  
		 while (itr_organism.hasNext()) 
		 {
			Organism _organism = ((Organism) itr_organism.next());
			if (_organism.pop_champ_child) 
			{
			//		 	System.out.print("\n At end of reproduction cycle, the child of the pop champ is: "); 
			   break;
			}
		 }
	  
	  //   	System.out.print("\n Epoch complete");
	  
	  }
   
	   public void print_to_file_by_species(String xNameFile) {
	  //
	  // write to file genome in native format (for re-read)
	  //
		 IOseq xFile;
	  
		 xFile = new IOseq(xNameFile);
		 xFile.IOseqOpenW(false);
	  
	  
		 try {
		 
			Iterator itr_specie;
			itr_specie = species.iterator();
		 
			while (itr_specie.hasNext()) {
			   Species _specie = ((Species) itr_specie.next());
			   _specie.print_to_file(xFile);
			}
		 
		 } 
			 catch (Throwable e) {
			   System.err.println(e);
			}
	  
		 xFile.IOseqCloseW();
	  
	  
	  
	  }      
   
	   public void speciate() 
	  {
	  
		 Iterator itr_organism;
		 Iterator itr_specie;
	  
		 Organism compare_org = null; //Organism for comparison 
		 Species newspecies = null;
	  
		 species = new Vector(1, 0);
		 int counter = 0; //Species counter
	  
	  // for each organism.....
	  
		 itr_organism = organisms.iterator();
		 while (itr_organism.hasNext()) 
		 {
		 
			Organism _organism = ((Organism) itr_organism.next());
		 
		 // if list species is empty , create the first species!
			if (species.isEmpty()) 
			{
			   newspecies = new Species(++counter); // create a new specie
			   species.add(newspecies); // add this species to list of species
			   newspecies.add_Organism(_organism);
			// Add to new spoecies the current organism
			   _organism.setSpecies(newspecies); // Point organism to its species
			
			} 
			else 
			{
			// looop in all species.... (each species is a Vector of organism...)
			   itr_specie = species.iterator();
			   boolean done = false;
			
			   while (!done && itr_specie.hasNext()) 
			   {
			   
			   // point _species-esima
				  Species _specie = ((Species) itr_specie.next());
			   // point to first organism of this _specie-esima
				  compare_org = (Organism) _specie.getOrganisms().firstElement();
			   // compare _organism-esimo('_organism') with first organism in current specie('compare_org')
				  double curr_compat = 
				  _organism.getGenome().compatibility(compare_org.getGenome()); 
			   
				  if (curr_compat < Neat.p_compat_threshold) 
				  {
				  //Found compatible species, so add this organism to it
					 _specie.add_Organism(_organism);
				  //update in organism pointer to its species 
					 _organism.setSpecies(_specie);
				  //force exit from this block ...
					 done = true;
				  }
			   }
			
			// if no found species compatible , create specie 
			   if (!done) 
			   {
				  newspecies = new Species(++counter); // create a new specie
				  species.add(newspecies); // add this species to list of species
				  newspecies.add_Organism(_organism);
			   // Add to new species the current organism
				  _organism.setSpecies(newspecies); // Point organism to its species
			   
			   }
			
			}
		 
		 }
	  
		 last_species = counter; //Keep track of highest species
	  }               
   
   /**
   * the increment of cur_node_id must be
   * executed only from a method of population
   * for security reason
   */
	   public int  getCur_node_id_and_increment() {
		 return cur_node_id++;
	  }  
   /**
   * the increment of cur_innov_num must be
   * executed only from a method of population
   * for security reason
   */
   
	   public double getCurr_innov_num_and_increment()
	  {
		 return cur_innov_num++;
	  }
	   public void incrementCur_innov_num() {
		 cur_innov_num += 1;
	  }
	   public void incrementCur_node_id() {
		 cur_node_id += 1;
	  }/**
   * Insert the method's description here.
   * Creation date: (01/02/2002 9.48.44)
   */                        
	   public Population() {}  
   /**
   *	Special constructor to create a population of random 
   *	topologies uses 
   *	Genome (int i, int o, int n,int nmax, bool r, double linkprob) 
   *	See the Genome constructor for the argument specifications
   *    the parameter are :
   *  	size = number of organisms
   *  	i    = number of inputs
   *		o    = number of output
   *		nmax = max index of nodes
   *		r    = the net can be recurrent ?
   *	linkprob = probability of connecting two nodes.
   */ 
	   public Population(int size,int i,int o, int nmax, boolean r, double linkprob) 
	  {
	  
		   this.inputNodes = i;
		   this.outputNodes = o;
		   this.nodesMax = nmax;
		   this.recurrent = r;
		   this.linkProp = linkprob;
	  
		 String mask4 = "0000";
		 DecimalFormat fmt4 = new DecimalFormat(mask4);
		 String fname;
		 int count;
		 Genome new_genome = null;
	  
//		 System.out.print("\n  -Making a random population of "+size+" organisms ");
	  
		 winnergen=0;
		 highest_fitness=0.0;
		 highest_last_changed=0;
		 organisms = new Vector(size);
		 String fname_prefix = EnvRoutine.getJneatFileData(EnvConstant.PREFIX_GENOME_RANDOM);

 
	  
		 for(count=0; count < size; count++) 
		 {
			new_genome=new Genome(count,i,o,NeatRoutine.randint(0,nmax),nmax,r,linkprob);
		 
		 // backup genome primordial
			fname = fname_prefix + fmt4.format(count);
			new_genome.print_to_filename(fname);
			organisms.add(new Organism(0,new_genome,1));
		 }
	  
		 cur_node_id = i + o + nmax + 1;;
		 cur_innov_num = ( i + o + nmax) * (i + o + nmax) + 1;
	  
//		 System.out.print("\n  The first  node_id  available is "+cur_node_id);
//		 System.out.print("\n  The first innov_num available is "+cur_innov_num);
	  
		 speciate();
	  
	  // backup of population
		 fname_prefix = EnvRoutine.getJneatFileData(EnvConstant.NAME_CURR_POPULATION);
		 print_to_filename(fname_prefix);
	  
	  }
   /**
   *
   * Construct off of a file of Genomes
   */
	   public Population(String xFileName) 
	  {
		 StringTokenizer st;
		 String xline;
		 IOseq xFile;
	  
		 String curword;
		 int idcheck = 0;
	  
		 Genome new_genome;
	  
		 organisms = new Vector(10,0);
	  
		 winnergen=0;
		 highest_fitness=0.0;
		 highest_last_changed=0;
		 cur_node_id=0;
		 cur_innov_num=0.0;
	  
		 xFile = new IOseq(xFileName);
		 boolean ret =  xFile.IOseqOpenR();
		 if (ret)
		 {
		 
		 
		 
			StringBuffer tmp1 = new StringBuffer("");
			StringBuffer tmp2 = new StringBuffer("");
		 
			int status = 0;
		 
		 
			try 
			{
			
			//   System.out.println("  ..opened population file "+xFileName);
			
			   xline = xFile.IOseqRead();
			
			   while (xline != "EOF") 
			   {
				  st = new StringTokenizer(xline);
				  curword = st.nextToken();
			   
				  if (curword.equalsIgnoreCase("genomestart")) 
				  {
				  
					 if (status == 1) 
						status = 2;
				  
					 curword = st.nextToken();
					 idcheck = Integer.parseInt(curword);
				  
					 new_genome=new Genome(idcheck,xFile);
					 new_genome.notes = tmp2.toString();
				  
					 organisms.add(new Organism(0,new_genome,1));
					 if (cur_node_id < new_genome.get_last_node_id())
						cur_node_id = new_genome.get_last_node_id();
					 if (cur_innov_num < new_genome.get_last_gene_innovnum())
						cur_innov_num = new_genome.get_last_gene_innovnum();
				  }
				  else if (curword.equals("/*")) 
				  {
				  
					 if (status == 0)
						status = 1;
				  
					 if (status == 2)
					 {
						status = 1;
				//		tmp2 = new StringBuffer("");
					 } 
				  
					 curword = st.nextToken();
				//	 tmp1 = new StringBuffer("");
					 while (!curword.equals("*/")) 
					 {
		//				tmp1.append(" "+curword);
		//			 			  		System.out.print(" " + curword);
						curword = st.nextToken();
					 }
//				  				System.out.print("\n");
				/*	 tmp2.append(" "+tmp1);
				  	System.out.print("\n tmp2 = "+tmp2);*/
				  
				  }
			   
				  xline = xFile.IOseqRead();
			   }
			}
				catch (Throwable e) 
			   {	
				  System.err.println(e + " : error during read " + xFileName);
			   }
		 
			xFile.IOseqCloseR();
			//System.out.println("\n  ok readed!");
		 
			speciate();
		 
		 }
	  
	  
	  } 
   
	   public void print_to_filename(String xNameFile) 
	  {
	  //
	  // write to file genome in native format (for re-read)
	  //
		 IOseq xFile;
	  
	  
		 xFile = new IOseq(xNameFile);
		 xFile.IOseqOpenW(false);
	  
		 try 
		 {
			print_to_file(xFile);
		 } 
			 catch (Throwable e) 
			{
			   System.err.println(e);
			}
	  
		 xFile.IOseqCloseW();
	  
	  
	  } 
   /**
   * Debug Population
   * Note: This checks each genome by verifying each one
   *       Only useful for debugging
   */
	   public void verify() 
	  {
	  
		 Iterator itr_organism;
		 Organism _organism = null;
	  
		 itr_organism = organisms.iterator();
	  
		 while (itr_organism.hasNext()) 
		 {
			_organism = ((Organism) itr_organism.next());
			_organism.genome.verify();
		 }
	  
	  }
	   public void print_to_file(IOseq xFile) 
	  {
	  //
	  // write to file genome in native format (for re-read)
	  //
	  
		 Iterator itr_organism;
		 Organism _organism = null;
	  
		 itr_organism = organisms.iterator();
	  
		 while (itr_organism.hasNext()) 
		 {
			_organism = ((Organism) itr_organism.next());
			_organism.genome.print_to_file(xFile);
		 }
	  
	  
	  }/**
   *
   * Construct off of a file of Genomes
   */            
	   public Population(String xFileName,boolean _win) 
	  {
		 StringTokenizer st;
		 String xline;
		 IOseq xFile;
	  
		 boolean xok = false;
		 String curword;
		 int idcheck = 0;
	  
		 Genome new_genome;
	  
		 organisms = new Vector(10,0);
	  
		 winnergen=0;
		 highest_fitness=0.0;
		 highest_last_changed=0;
		 cur_node_id=0;
		 cur_innov_num=0.0;
	  
		 xFile = new IOseq(xFileName);
		 boolean ret =  xFile.IOseqOpenR();
		 if (ret)
		 {
		 
		 
		 
			StringBuffer tmp1 = new StringBuffer("");
			StringBuffer tmp2 = new StringBuffer("");
		 
			int status = 0;
		 
		 
			try 
			{
			
			   System.out.println("  ..opened population file "+xFileName);
			
			   xline = xFile.IOseqRead();
			
			   while (xline != "EOF") 
			   {
				  st = new StringTokenizer(xline);
				  curword = st.nextToken();
			   
				  if (curword.equalsIgnoreCase("genomestart")) 
				  {
				  
					 if (status == 1) 
						status = 2;
				  
					 curword = st.nextToken();
					 idcheck = Integer.parseInt(curword);
				  
					 new_genome=new Genome(idcheck,xFile);
					 new_genome.notes = tmp2.toString();
				  
					 if (xok) 	
						organisms.add(new Organism(0,new_genome,1));
					 xok = false;
				  
					 if (cur_node_id < new_genome.get_last_node_id())
						cur_node_id = new_genome.get_last_node_id();
					 if (cur_innov_num < new_genome.get_last_gene_innovnum())
						cur_innov_num = new_genome.get_last_gene_innovnum();
				  }


				  
				  else if (curword.equals("/*")) 
				  {


					  
					 if (xline.indexOf("WINNER") >= 0)
					 {
					 //	                System.out.print("\n found a winner genome" + xline);
						xok = true;
					 }
					 if (status == 0)
						status = 1;
				  
					 if (status == 2)
					 {
						status = 1;
//						tmp2 = new StringBuffer("");
					 } 


//					 System.out.print("\n passo de qui");
					 curword = st.nextToken();
		//			 tmp1 = new StringBuffer("");
					 while (!curword.equals("*/")) 
					 {
				//		tmp1.append(" "+curword);
			//		 			  		System.out.print(" " + curword);
						curword = st.nextToken();
					 }
				 /* 				System.out.print("\n");
					 tmp2.append(" "+tmp1);
				  	System.out.print("\n tmp2 = "+tmp2);*/
				  
				  }
			   
				  xline = xFile.IOseqRead();
			   }
			}
				catch (Throwable e) 
			   {	
				  System.err.println(e + " : error during read " + xFileName);
			   }
		 
			xFile.IOseqCloseR();
			System.out.println("\n  ok readed!");
			speciate();
		 
		 }
	  
	  
	  }}