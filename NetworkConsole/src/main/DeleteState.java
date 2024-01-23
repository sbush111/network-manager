package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import network.NetworkFileUtils;

public class DeleteState implements State {

	public void run(Main main) {
		
		System.out.println("Enter the name of the network you want to delete:");
		
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
		
		if(main.loadedNetwork != null && main.loadedNetworkName.equals(name)) {
			main.loadedNetwork = null;
			main.loadedNetworkName = null;
		}
		
		File file = new File(filePath);
		file.delete();
		
		System.out.println(name + " deleted.");
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
}
