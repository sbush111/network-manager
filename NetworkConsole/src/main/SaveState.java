package main;

import network.NetworkFileUtils;

/**
 * The state in which the program saves whatever network is in memory to file.
 * @author sbush
 *
 */
public class SaveState implements State {
	
	public void run(Main main) {
		
		if(main.loadedNetworkName != null) {
			NetworkFileUtils.saveNetworkToFile(main.loadedNetwork, main.loadedNetworkName);
			main.loadedNetworkUnsavedChanges = false;
			main.state = main.defaultState;
			System.out.println("All changes saved.");
			System.out.println();
			return;
		}
		
		String networkName = retrieveNameFromUser(main);
		
		NetworkFileUtils.saveNetworkToFile(main.loadedNetwork, networkName);
		
		System.out.println("All changes saved");
		System.out.println();
		
		main.loadedNetworkName = networkName;
		main.loadedNetworkUnsavedChanges = false;
		
		main.state = main.defaultState;
		
	}
	
	private String retrieveNameFromUser(Main main) {

		System.out.println("Please enter the name of the new network.");
		System.out.println("You may only use numbers, letters, and underscores:");
		
		String fileName = main.scanner.nextLine();
		
		while(!isValidFileName(fileName)) {
			
			System.out.println("Invalid file name.");
			System.out.println("File name can only contain numbers, letters, and underscores.");
			System.out.println("Please try again:");
			
			fileName = main.scanner.nextLine();
			
		}
		
		return fileName;
				
	}
	
	private boolean isValidFileName(String fileName) {
		
		for(char c : fileName.toCharArray()) {
			if(Character.isAlphabetic(c)) continue;
			if(48 <= c && c <= 57) continue;
			if(c == '_') continue;
			return false;
		}
		
		return true;
		
	}
	
}
