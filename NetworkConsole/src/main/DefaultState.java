package main;

/**
 * The state of the program when the program is idle and awaiting a command from the user. 
 * @author sbush
 *
 */
public class DefaultState implements State {

	public void run(Main main) {
		
		System.out.println("Awaiting command:");
		
		String command = main.scanner.nextLine();
		command = command.toLowerCase().trim();
		
		switch(command) {
		
		case "help":
			help(main);
			return;
			
		case "list":
			list(main);
			return;
			
		case "load":
			load(main);
			return;
			
		case "create":
			create(main);
			return;
			
		case "info":
			info(main);
			return;
			
		case "info2":
			info2(main);
			return;
			
		case "train":
			train(main);
			return;
			
		case "test":
			test(main);
			return;
			
		case "eval":
			eval(main);
			return;
			
		case "save":
			save(main);
			return;
			
		case "delete":
			delete(main);
			return;
			
		case "quit":
			quit(main);
			return;
			
		default: 
			System.out.printf("\"%s\" is not a valid command\n\n", command);
			return;
		
		}
		
	}
	
	private void help(Main main) {
		
		main.state = main.helpState;
		
	}
	
	private void list(Main main) {
		
		main.state = main.listState;
		
	}
	
	private void load(Main main) {
		
		main.state = main.loadState;
		
	}
	
	private void create(Main main) {
		
		if(main.loadedNetworkUnsavedChanges) {
			
			System.out.println("Changes to the currently loaded network will not be saved.");
			System.out.println("Are you sure? (Y/N)");
			
			String input = main.scanner.nextLine();
			
			while(!input.toLowerCase().equals("y") && ! input.toLowerCase().equals("n")) {
				
				System.out.println("Please type 'N' to cancel the create command, so you can save the currently loaded network.");
				System.out.println("Type 'Y' if you wish to proceed.");
				
				input = main.scanner.nextLine();
				
			}
			
			if(input.toLowerCase().equals("n")) {
				System.out.println("Create cancelled.");
				return;
			}
			
		}
		
		main.state = main.createState;
		
	}
	
	private void info(Main main) {
		if(main.loadedNetwork != null) main.state = main.infoState;
		else System.out.println("No network loaded.\n");
	}
	
	private void info2(Main main) {
		if(main.loadedNetwork != null) main.state = main.info2State;
		else System.out.println("No network loaded.\n");
	}
	
	private void train(Main main) {
		if(main.loadedNetwork != null) main.state = main.trainState;
		else System.out.println("No network loaded.\n");
	}
	
	private void test(Main main) {
		if(main.loadedNetwork != null) main.state = main.testState;
		else System.out.println("No network loaded.\n");
	}
	
	private void eval(Main main) {
		if(main.loadedNetwork != null) main.state = main.evalState;
		else System.out.println("No network loaded.\n");
	}
	
	private void save(Main main) {
		if(main.loadedNetwork != null) main.state = main.saveState;
		else System.out.println("No network loaded.\n");
	}
	
	private void delete(Main main) {
		main.state = main.deleteState;
	}
	
	private void quit(Main main) {
		main.scanner.close();
		System.exit(0);
	}
	
}