package neat;

import jneat.NNode;
import jneat.Network;

public class Painter {

	Network brain;
	
	public Painter(Network brain) {
		this.brain = brain;
	}

	public double[] getOutput(double[] inputs){
		
		double in[] = new double[inputs.length+1];
		in[inputs.length] = -1.0; // Bias
	  
	  	// Populate the rest of "inputs" from this organism's status in the simulation.
	  	for(int i = 0; i < in.length; i++)
	  		in[i] = inputs[i];
	 
	  	// Load these inputs into the neural network.
	  	brain.load_sensors(in);
	 
	  	int net_depth = brain.max_depth();
	  	// first activate from sensor to next layer....
	  	brain.activate();
	 
		// next activate each layer until the last level is reached
		for (int relax = 0; relax <= net_depth; relax++)
		{
			brain.activate();
	 	}
	        
		// Retrieve outputs from the final layer.
		double[] outputs = new double[brain.getOutputs().size()];
		for(int i = 0; i < outputs.length; i++)
			outputs[i] = ((NNode) brain.getOutputs().elementAt(i)).getActivation();
		
		return outputs;
		
	}
	
}
