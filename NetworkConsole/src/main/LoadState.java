package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import network.NetworkFileUtils;

public class LoadState implements State {

	public void run(Main main) {
		
		System.out.println("Enter the name of the network you want to load:");
		
		String name = main.scanner.nextLine();
		File saveFolder = new File(".\\saves");
		
		String filePath = null;
		
		for(File file : saveFolder.listFiles()) {
			
			try(FileInputStream in = new FileInputStream(file)) {
				
				if(!name.equals(NetworkFileUtils.readNameFromFile(in))) continue;
				
				filePath = file.getPath();
				
				break;
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
		}
		
		if(filePath == null) {
			System.out.println("No such network exists.");
			System.out.println("Type \"list\" for the names of all saved networks.");
			main.state = main.defaultState;
			return;
		}
		
		main.loadedNetworkName = name;
		main.loadedNetwork = NetworkFileUtils.loadNetworkFromFile(filePath).get();
		main.loadedNetworkUnsavedChanges = false;
		
		System.out.println(name + " loaded.");
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
	
	
}
