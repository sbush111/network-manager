package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The state where every weight and bias in the neural network is printed to the console.
 * @author sbush
 *
 */
public class Info2State implements State {

	public void run(Main main) {
		
		System.out.println("Type the path of the directory where the weights and biases "
				+ "should be written into a text file:");
		
		String path = main.scanner.nextLine();
		File directory = new File(path);
		
		if(!directory.exists() || !directory.isDirectory()) {
			System.out.println("No such folder exists.\n");
			main.state = main.defaultState;
			return;
		}
		
		File output = new File(directory, main.loadedNetworkName + ".txt");
		
		try {
			output.createNewFile();
		} catch (IOException e) {
			System.out.println("Log file could not be created.\n");
			main.state = main.defaultState;
			return;
		}
		
		try(PrintWriter out = new PrintWriter(output)) {
			
			out.println(main.loadedNetwork);
			
		} catch (FileNotFoundException e) {
			System.out.println("Log file could not be found.");
			e.printStackTrace();
			System.out.println();
			main.state = main.defaultState;
			return;
		}
		
		System.out.println("Network info printed to file \"" + output.getAbsolutePath() + "\"");
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
}
