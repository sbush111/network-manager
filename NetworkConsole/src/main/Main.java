package main;

import java.util.Scanner;

import network.Network;

/**
 * The main class holds the program's key objects in memory: the neural network itself, 
 * instances of each state in the program, and the scanner which reads input from the
 * console.
 * @author sbush
 *
 */
public class Main {
	
	Scanner scanner;
	
	Network loadedNetwork;
	String loadedNetworkName;
	boolean loadedNetworkUnsavedChanges;
	
	State state;
	
	StartupState startupState;
	DefaultState defaultState;
	HelpState helpState;
	ListState listState;
	LoadState loadState;
	CreateState createState;
	InfoState infoState;
	Info2State info2State;
	TrainState trainState;
	TestState testState;
	EvalState evalState;
	SaveState saveState;
	DeleteState deleteState;
	
	public Main() {
		
		scanner = new Scanner(System.in);
		
		startupState = new StartupState();
		defaultState = new DefaultState();
		helpState = new HelpState();
		listState = new ListState();
		loadState = new LoadState();
		createState = new CreateState();
		infoState = new InfoState();
		info2State = new Info2State();
		trainState = new TrainState();
		testState = new TestState();
		evalState = new EvalState();
		saveState = new SaveState();
		deleteState = new DeleteState();
		
		state = startupState;
		
		loadedNetwork = null;
		loadedNetworkName = null;
		loadedNetworkUnsavedChanges = false;
		
	}
	
	public void run() {
		
		while(true) {
			state.run(this);
		}
		
	}
	
	public static void main(String[] args) {
		
		new Main().run();
		
	}
	
	@Override
	public String toString() {
		return "Current state: " + state.toString();
	}
	
}
