package main;

import java.util.InputMismatchException;
import java.util.Random;
import network.ActivationFunction;
import network.Network;

/**
 * The state of the program responsible for the creation of new neural networks in memory.
 * @author sbush
 *
 */
public class CreateState implements State {
	
	public void run(Main main) {
		
		int numHiddenLayers = retrieveNumberOfHiddenLayersFromUser(main);
		
		int[] layerSizes = new int[numHiddenLayers + 2];
		layerSizes[0] = 784;
		layerSizes[layerSizes.length - 1] = 10;
		
		
		populateLayerSizes(layerSizes, main);
		
		ActivationFunction[] funcs = new ActivationFunction[numHiddenLayers + 1];
		
		populateActivationFunction(funcs, main);
		
		main.loadedNetwork = new Network(null, layerSizes, funcs, new Random());
		main.loadedNetworkName = null;
		main.loadedNetworkUnsavedChanges = true;
		
		System.out.println("Network has been created.");
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
	public int retrieveNumberOfHiddenLayersFromUser(Main main) {
		
		System.out.println("Enter the number of hidden layers this network will have:");
		
		boolean retrieved = false;
		
		int count = 0;
		
		while(!retrieved) {
			
			try {
				
				count = main.scanner.nextInt();
				
			} catch(InputMismatchException e) {
				
				main.scanner.nextLine();
				System.out.println("Input cannot be parsed. Make sure you have typed in a positive integer.");
				System.out.println("Please try again:");
				continue;
				
			}
			
			if(count < 1) {
				System.out.println("Network must have at least one hidden layer.");
				System.out.println("Please try again:");
			} else {
				retrieved = true;
			}
			
		}
		
		return count;
		
	}
	
	public void populateLayerSizes(int[] layerSizes, Main main) {
		
		System.out.printf("This network will have %d hidden layers.\n", layerSizes.length - 2);
		
		for(int i = 1; i < layerSizes.length - 1; i++) {
			
			System.out.printf("How many neurons will be in hidden layer %d?\n", i);
			
			int numNeurons = 0;
			boolean retrieved = false;
			
			while(!retrieved) {
				
				try {
					
					numNeurons = main.scanner.nextInt();
					
				} catch (InputMismatchException e) {
					
					main.scanner.nextLine();
					System.out.println("Input cannot be parsed. Make sure you have typed in a positive integer.");
					System.out.println("Please try again:");
					continue;
					
				}
				
				if(numNeurons < 1) {
					System.out.println("Layer must have at lease one neuron.");
					System.out.println("Please try again:");
					continue;
				} else {
					retrieved = true;
				}
				
			}
			
			layerSizes[i] = numNeurons;
			
		}
		
	}
	
	public void populateActivationFunction(ActivationFunction[] funcs, Main main) {
		
		System.out.println("Each layer except for the input layer must have an associated activation function.");
		System.out.println("Your options for activation functions are as follows:");
		System.out.println("1. Sigmoid");
		System.out.println("2. ReLU");
		System.out.println("3. Leaky ReLU");
		System.out.println("4. Tanh");
		System.out.println("5. Step function");
		System.out.println("6. Identity (activation function does not change a neuron's value)");
		System.out.println("Type 1-6 to select one of these options.");
		
		for(int i = 0; i < funcs.length; i++) {
			
			if(i == funcs.length - 1) System.out.printf("Select an activation function for the output layer: ");
			else System.out.printf("Select an activation function for hidden layer %d: ", i+1);
			
			int func = 0;
			boolean retrieved = false;
			
			while(!retrieved) {
				
				try {
					
					func = main.scanner.nextInt();
					
				} catch (InputMismatchException e) {
					
					main.scanner.nextLine();
					System.out.println("Input cannot be parsed. Make sure you have typed in a positive integer.");
					System.out.println("Please try again:");
					continue;
					
				}
				
				switch(func) {
				
				case 1:
					retrieved = true;
					funcs[i] = ActivationFunction.SIGMOID;
					break;
					
				case 2:
					retrieved = true;
					funcs[i] = ActivationFunction.RELU;
					break;
					
				case 3:
					retrieved = true;
					funcs[i] = ActivationFunction.LEAKY_RELU;
					break;
					
				case 4:
					retrieved = true;
					funcs[i] = ActivationFunction.TANH;
					break;
					
				case 5:
					retrieved = true;
					funcs[i] = ActivationFunction.STEP;
					break;
					
				case 6:
					retrieved = true;
					funcs[i] = ActivationFunction.IDENTITY;
					break;
					
				default:
					System.out.println("You must choose a number from 1 to 6.");
					System.out.println("Please try again:");
				
				}
				
			}
			
			main.scanner.nextLine();
			
		}
		
	}
	
}