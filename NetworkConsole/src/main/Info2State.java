package main;

/**
 * The state where every weight and bias in the neural network is printed to the console.
 * @author sbush
 *
 */
public class Info2State implements State {

	public void run(Main main) {
		
		System.out.println(main.loadedNetwork);
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
}
