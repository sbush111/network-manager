package network;

import linearAlgebra.Matrix;
import linearAlgebra.Vector;

public record DesiredNetworkUpdate(Matrix[] weights, Vector[] biases) {
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < biases.length; i++) {
			
			//Layer i
			sb.append("Layer " + (i+1) + "\n========\n");
			sb.append("Weights:\n");
			sb.append(weights[i].toString() + "\n");
			sb.append("Biases:\n");
			sb.append(biases[i].toString() + "\n");
			
			sb.append("\n");
			
		}
		
		return sb.toString();
		
	}
	
}
