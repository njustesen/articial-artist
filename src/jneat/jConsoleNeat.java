/* Generated by Together */

   package jneat;

   import java.util.*;

   import jNeatCommon.*;
																public class jConsoleNeat {
   
      	  	   public static void main(java.lang.String[] args) 
	  {
	  
		 Neat.initbase();
	  
		 String nome = "c:\\jneat\\dati\\parametri";
		 String nomeA = null;
		 String nomeB = null;
	  
	  
	  //  String nome = "c:\\neat_v2\\evolution\\parametri.ne";
		 boolean rc = Neat.readParam(nome);
	  
	  
		 if (rc)
			System.out.println(" okay read ");
		 else
			System.out.println(" error in read ");
	  
		 nome = "c:\\jneat\\dati\\listay";
		 Neat.writeParam(nome);
	  
	  
	  // ------------------------------------------------------   	
	  // start code for various experiment
	  // ------------------------------------------------------
	  
	  
	  /*
	  //
	  // -------------- testing a depth, stability , for the following genome ---------------
	  //
	  nomeA = "c:\\jneat\\dati\\genome.ex2";     // depth must be = 3
	  Evolution.Experiment2(nomeA);
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex3";     // depth must be = 4
	  Evolution.Experiment2(nomeA);
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex4";     // depth must be = 5
	  Evolution.Experiment2(nomeA);
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex5";     // network not stable 
	  Evolution.Experiment2(nomeA);
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex6";     // depth must be = 3
	  Evolution.Experiment2(nomeA);
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex9";     // depth must be = 2
	  Evolution.Experiment2(nomeA);
	  
	  */
	  
	  /*
	  //
	  // -------------------- creation a population from a one genome--------------------------
	  //
	  	nomeA = "c:\\jneat\\dati\\genome.old";
	  Evolution.Experiment1(nomeA,10);
	  */
	  
	  
	  
	  
	  
	  //
	  // -------------------- creation of a random population --------------------------
	  //
		 Evolution.Experiment3(20,NeatConstant.COLD,  1);
	  //		Evolution.Experiment3(150,NeatConstant.EMER,  10);
	  
	  
	  
	  
	  /*   	 
	  //
	  // -------------------- compare and mate for two genome
	  //
	   	nomeA = "c:\\jneat\\dati\\genome.ex7";
	   	nomeB = "c:\\jneat\\dati\\genome.ex8";
	   Evolution.Experiment4(nomeA,nomeB);
		
	   	nomeA = "c:\\jneat\\dati\\genome.ex9";
	   	nomeB = "c:\\jneat\\dati\\genome.ex10";
	  Evolution.Experiment4(nomeA,nomeB);
	  
	  */
	  
	  
	  
	  /*
	  //
	  // ------ testing if exist recurrent connection from two nodes in a genome passed -------------
	  //    
	  //
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex3";			// must result = true
	  Evolution.Experiment5(nomeA,5 ,2);				// status = 0
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex3";			// must result = true
	  Evolution.Experiment5(nomeA,5 ,1);				// status = 0
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex3";			// must result = false
	  Evolution.Experiment5(nomeA,4 ,5);				// status = 0
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex5";			// must result = true
	  Evolution.Experiment5(nomeA,8 ,9);				// status = 0
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex5";			// must result = true
	  Evolution.Experiment5(nomeA,8 ,2);				// status = 0
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex5";			// must result = true
	  Evolution.Experiment5(nomeA,8 ,1);				// status = 0
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex5";			// must result = true
	  Evolution.Experiment5(nomeA,9 ,9);				// status = 0
	  
	  nomeA = "c:\\jneat\\dati\\genome.ex5";			// must result = false
	  Evolution.Experiment5(nomeA,1 ,8);				// status = 0
	  
	  */
	  
	  
	  
	  }                                                                        
   
   }