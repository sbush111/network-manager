package main;

import java.util.Arrays;

import network.ActivationFunction;
import network.Network;

/**
 * The state in which basic information about the neural network is printed to the console.
 * @author sbush
 *
 */
public class InfoState implements State {

	public void run(Main main) {
		
		Network network = main.loadedNetwork;
		
		String name = main.loadedNetworkName;
		
		if(name == null) System.out.println("Network is unsaved.");
		else System.out.printf("Name: %s\n", name);
		
		if (main.loadedNetworkUnsavedChanges) System.out.println("Network has unsaved changes.");
		else System.out.println("All changes to the network have been saved.");
		
		System.out.println();
		
		int[] layerSizes = network.layerSizes();
		
		System.out.println("Number of neurons in each layer: " + Arrays.toString(layerSizes));
		
		ActivationFunction[] funcs = network.activationFunctions();
		
		System.out.print("Activation functions for each layer: [None, ");
		for(int i = 1; i < funcs.length - 1; i++) {
			System.out.print(funcs[i].toString());
			System.out.print(", ");
		}
		System.out.println(funcs[funcs.length - 1].toString() + "]");
		
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
}
