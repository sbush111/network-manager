package main;

import linearAlgebra.Vector;
import network.DataSet;
import network.NetworkFileUtils;

public class TestState implements State {
	
	public void run(Main main) {
		
		System.out.println("Testing network");
		System.out.println("===========");
		
		int numCorrect = 0;
		int numTotal = 10_000;
		
		for(int i = 0; i < 10_000; i++) {
			
			if(i % 1000 == 0) System.out.print("-");
			
			Vector pixels = NetworkFileUtils.getInputVector(DataSet.test, i);
			int answer = NetworkFileUtils.getAnswer(DataSet.test, i);
			
			Vector[] activations = main.loadedNetwork.forwardprop(pixels).activations();
			int output = activations[activations.length - 1].indexOfMax();
			
			if(output == answer) numCorrect++;
			
		}
		
		System.out.println("-");
		System.out.println("Testing complete");
		System.out.printf("%d / 10_000 correct, %f%% accuracy\n", numCorrect, 100 * numCorrect / (double) numTotal);
		System.out.println();
		
		main.state = main.defaultState;
		
		
	}

}
