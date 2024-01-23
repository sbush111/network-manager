package network;

import linearAlgebra.Vector;

public record NetworkActivation(Vector[] activations, Vector[] rawActivations) {

	public Vector output() {
		return activations[activations.length - 1];
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < activations.length; i++) {
			
			//Layer i
			sb.append("Layer " + i + "\n========\n");
			sb.append("Raw activations:\n");
			sb.append(rawActivations[i].toString() + "\n");
			sb.append("Activations:\n");
			sb.append(activations[i].toString() + "\n");
			sb.append("\n");
			
		}
		
		return sb.toString();
		
	}
	
}
