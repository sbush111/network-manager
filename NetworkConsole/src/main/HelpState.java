package main;

/**
 * The state where the program prints out the help menu.
 * @author sbush
 *
 */
public class HelpState implements State {
		
	public void run(Main main) {
		
		System.out.println(" Command list ");
		System.out.println("-============-");
		System.out.println("help: print a list of commands and their descriptions");
		System.out.println("list: print a list of all saved neural networks");
		System.out.println("create: create a new neural network in memory");
		System.out.println("load: load a neural network from file into memory");
		System.out.println("info: print basic info aobut the loaded network");
		System.out.println("info2: print the weights and biases of the loaded netork");
		System.out.println("train: train the loaded neural network");
		System.out.println("test: test the loaded neural network");
		System.out.println("eval: evaluate a particular image with the network");
		System.out.println("save: save the currently loaded network to file");
		System.out.println("delete: delete a network");
		System.out.println("quit: quit the program");
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
}