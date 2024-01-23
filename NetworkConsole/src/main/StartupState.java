package main;

/**
 * The state entered when the program launches, which displays the welcome message.
 * @author sbush
 *
 */
public class StartupState implements State {

	@Override
	public void run(Main main) {
		
		System.out.println("Welcome to the Neural Network Manager.");
		System.out.println("type help for a list of commands");
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
}
