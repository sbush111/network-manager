package main;

import java.util.Random;

import linearAlgebra.Vector;
import network.DataSet;
import network.DesiredNetworkUpdate;
import network.NetworkActivation;
import network.NetworkFileUtils;

public class TrainState implements State {
	
	public void run(Main main) {
		
		System.out.println("Enter number of epochs (Default - 30):");
		int numEpochs = main.scanner.nextInt();
		
		System.out.println("Enter batch size (Default - 20):");
		int batchSize = main.scanner.nextInt();
		
		System.out.println("Enter learning rate (Default - 3.0):");
		double learningRate = main.scanner.nextDouble();
		
		System.out.println("Training in progress...");
		System.out.println("============================================================");
		
		int numExamplesTotal = numEpochs * 60_000;
		int numExamplesProcessed = 0;
		
		for(int epoch = 0; epoch < numEpochs; epoch++) {
			
			Random rand = new Random();
			int[] shuffledIndices = new int[60_000];
			for(int i = 0; i < shuffledIndices.length; i++) shuffledIndices[i] = i;
			for(int i = shuffledIndices.length - 1; i >= 1; i--) {
				int j = rand.nextInt(i+1);
				int temp = shuffledIndices[i];
				shuffledIndices[i] = shuffledIndices[j];
				shuffledIndices[j] = temp;
			}
			
			for(int batch = 0; batch < 60_000 / batchSize; batch++) {
				
				DesiredNetworkUpdate[] desiredChanges = new DesiredNetworkUpdate[batchSize];
				
				for(int example = 0; example < batchSize; example++) {
					
					if(numExamplesProcessed % (numExamplesTotal / 60) == 0) System.out.print("-");
					numExamplesProcessed++;
					
					int currentExample = shuffledIndices[batch * batchSize + example];
					
					Vector desiredOutput = new Vector(10);
					desiredOutput.set(NetworkFileUtils.getAnswer(DataSet.train, currentExample), 1);
					
					NetworkActivation networkOutput = main.loadedNetwork.forwardprop(NetworkFileUtils.getInputVector(DataSet.train, currentExample));
					desiredChanges[example] = main.loadedNetwork.backprop(networkOutput, desiredOutput);
					
				}
				
				main.loadedNetwork.updateNetwork(desiredChanges, learningRate);
				
			}
			
		}
		
		main.scanner.nextLine();
		
		System.out.println();
		System.out.println("Training session complete.");
		System.out.println();
		
		main.loadedNetworkUnsavedChanges = true;
		
		main.state = main.defaultState;
		
		
	}

}
