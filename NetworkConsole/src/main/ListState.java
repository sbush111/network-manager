package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import network.NetworkFileUtils;

public class ListState implements State {

	@Override
	public void run(Main main) {
		
		File saveFolder = new File(".\\saves");
		
		if(saveFolder.listFiles().length == 0) {
			System.out.println("No networks have been saved to this system.");
			System.out.println();
			main.state = main.defaultState;
			return;
		}
		
		for(File file : saveFolder.listFiles()) {
			
			System.out.println(getName(file));
			
		}
		
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
	private String getName(File file) {
		
		String name = "";
		
		try(FileInputStream in = new FileInputStream(file)) {
			
			name = NetworkFileUtils.readNameFromFile(in);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return name;
		
	}

}
