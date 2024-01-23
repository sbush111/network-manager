package network;

import java.util.Arrays;
import java.util.Random;
import linearAlgebra.Matrix;
import linearAlgebra.Vector;

public class Network {
	
	protected String name;
	protected final int[] layerSizes;
	protected final Matrix[] weights;
	protected final Vector[] biases;
	protected final ActivationFunction[] functions;
	
	/**
	 * Constructs a new neural network with a number of layers equal to the size
	 * of the layerSizes array. The number of neurons in each layer is equal to the
	 * value of layerSizes at the index equal to that layer (zero-indexed). The weights
	 * and biases are stored as an array of Matrices and an array of Vectors respectively,
	 * and are randomnly initialized by the helper methods initializeWeights() and
	 * initializeBiases(). Activation functions for each layer are also stored in an array.
	 * @param layerSizes
	 * @param functions
	 * @param rand
	 */
	public Network(String name, int[] layerSizes, ActivationFunction[] functions, Random rand) {
		
		final int numLayers = layerSizes.length;
		
		if(layerSizes.length <= 2)
			throw new IllegalArgumentException("Network must have at least one hidden layer.");
		
		for(int n : layerSizes) if(n < 1)
			throw new IllegalArgumentException("Layers must have at least one neuron.");
		
		if(functions.length != layerSizes.length - 1)
			throw new IllegalArgumentException("Number of activation functions must be equal to the number of layers minus one.");
		
		for(ActivationFunction function : functions) if(function == null) 
			throw new IllegalArgumentException("Activation Function cannot be null.");
		
		////////////////////////////////////////////////////////////////
		
		this.name = name;
		
		this.functions = new ActivationFunction[layerSizes.length];
		this.functions[0] = ActivationFunction.IDENTITY;
		for(int i = 0, j = 1; i < functions.length; i++, j++) this.functions[j] = functions[i];
		
		this.layerSizes = Arrays.copyOf(layerSizes, layerSizes.length);
		
		this.weights = new Matrix[numLayers - 1];
		this.biases = new Vector[numLayers - 1];
		
		for(int i = 0; i < numLayers - 1; i++) {
			weights[i] = new Matrix(layerSizes[i+1], layerSizes[i]);
			biases[i] = new Vector(layerSizes[i+1]);
		}
		
		initializeWeights(rand);
		initializeBiases(rand);
		
	}
	
	public Network(String name, int[] layerSizes, ActivationFunction[] functions, Matrix[] weights, Vector[] biases) {
		
		this.name = name;
		this.layerSizes = layerSizes;
		this.functions = functions;
		this.weights = weights;
		this.biases = biases;
		
	}
	
	/**
	 * Randomly initializes the weights of the matrix passed in, using the
	 * random number generator object supplied. Weights are initialized to
	 * a random double between -1 and -0.1 or 0.1 and 1.
	 * @param weightMatrix
	 * @param rand
	 */
	private void initializeWeights(Random rand) {
		
		for(Matrix eachLayer : weights) {
			
			for(int r = 0; r < eachLayer.getRowCount(); r++) {
				
				for(int c = 0; c < eachLayer.getColCount(); c++) {
					
					double value = 0;
					
					while(-0.1 <= value && value <= 0.1) {
						value = 4 * rand.nextDouble() - 2;
					}
					
					eachLayer.set(r, c, value);
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Randomly initializes the biases passed in, using the
	 * random number generator object supplied. Weights are initialized to
	 * a random double between -1 and -0.1 or 0.1 and 1.
	 * @param weightMatrix
	 * @param rand
	 */
	private void initializeBiases(Random rand) {
		
		for(Vector eachLayer : biases) {
			
			for(int i = 0; i < eachLayer.size(); i++) {
				
				double value = 0;
				
				while(-0.1 <= value && value <= 0.1) {
					value = 2 * rand.nextDouble() - 1;
				}
				
				eachLayer.set(i, value);
				
			}
			
		}
		
	}
	
	/**
	 * Takes an input vector and propogates forward through the network, using the activation
	 * functions supplied at construction. The result is returned as a NetworkActivation object
	 * which contains the activations of the whole network, which is requried for the 
	 * backpropogation algorithm. 
	 * @param input
	 * @return network's activations
	 */
	public NetworkActivation forwardprop(Vector input) {
		
		if(input.size() != layerSizes[0]) {
			throw new IllegalArgumentException("Input vector must be of size " + layerSizes[0]);
		}
		
		Vector[] rawActivations = new Vector[numLayers()];
		Vector[] activations = new Vector[numLayers()];
		
		rawActivations[0] = new Vector(input.toArray());
		activations[0] = rawActivations[0].apply((x) -> x/255.0).apply(functions[0]::apply);
		
		for(int i = 1; i < numLayers(); i++) {
			rawActivations[i] = weights[i - 1].multiply(activations[i-1]).add(biases[i-1]);
			activations[i] = rawActivations[i].apply(functions[i]::apply);
		}
		
		return new NetworkActivation(activations, rawActivations);
		
	}
	
	/**
	 * Given an object containing the activations of the network for some input and its associated
	 * desired output, this method uses the backpropogation algorithm to calculate the gradients
	 * of the weights and biases of the current network according to the given training example.
	 * It returns the results as a DesiredNetworkUpdate object containing a Matrix array called
	 * weights and a Vector array called biases. 
	 * @param result
	 * @param desiredOutput
	 * @return gradients of weights and biases
	 */
	public DesiredNetworkUpdate backprop(NetworkActivation result, Vector desiredOutput) {
		
		if(desiredOutput.size() != layerSizes[numLayers() - 1])
			throw new IllegalArgumentException("Desired output vector must be of length " + layerSizes[numLayers() - 1]);
		
		int n = numLayers();
		
		Vector[] a = result.activations(); 		//size = n
		Vector[] z = result.rawActivations(); 	//size = n
		Matrix[] W = this.weights; 				//size = n-1
		Vector y = desiredOutput;
		
		Vector[] da = new Vector[n];
		Vector[] dz = new Vector[n];
		Vector[] db = new Vector[n-1];
		Matrix[] dW = new Matrix[n-1];
		
		int size = layerSizes[n-1];
		int prevSize = layerSizes[n-2];
		int nextSize;
		
		//Output layer activations
		da[n-1] = new Vector(size);
		for(int i = 0; i < size; i++)
			da[n-1].set(i, 2 * (a[n-1].get(i) - y.get(i)));
		
		//Output layer raw activations
		dz[n-1] = new Vector(size);
		for(int i = 0; i < size; i++) {
			double dE_da = da[n-1].get(i);
			double da_dz = functions[n-1].differentiate(z[n-1].get(i));
			dz[n-1].set(i, dE_da * da_dz);
		}
		
		//Output layer biases
		db[n-2] = new Vector(size);
		for(int i = 0; i < size; i++) {
			db[n-2].set(i, dz[n-1].get(i));
		}
		
		//Output layer weights
		dW[n-2] = new Matrix(size, prevSize);
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < prevSize; c++) {
				double dE_dz = dz[n-1].get(r);
				double dz_dW = a[n-2].get(c);
				dW[n-2].set(r, c, dE_dz * dz_dW); 
			}
		}
		
		//Hidden layers
		for(int L = n-2; L >= 1; L--) {
			
			nextSize = layerSizes[L+1];
			size = layerSizes[L];
			prevSize = layerSizes[L-1];
			
			//Hidden layer activations
			da[L] = new Vector(size);
			for(int i = 0; i < size; i++) {
				double dE_da = 0;
				for(int j = 0; j < nextSize; j++) {
					double dE_dz = dz[L+1].get(j);
					double dz_da = W[L].get(j, i);
					dE_da += (dE_dz * dz_da);
				}
				da[L].set(i, dE_da);
			}
			
			//Hidden layer raw activations
			dz[L] = new Vector(size);
			for(int i = 0; i < size; i++) {
				double dE_da = da[L].get(i);
				double da_dz = functions[L].differentiate(z[L].get(i));
				dz[L].set(i, dE_da * da_dz);
			}
			
			//Hidden layer biases
			db[L-1] = new Vector(size);
			for(int i = 0; i < size; i++) {
				db[L-1].set(i, dz[L].get(i));
			}
			
			//Hidden layer weights
			dW[L-1] = new Matrix(size, prevSize);
			for(int r = 0; r < size; r++) {
				for(int c = 0; c < prevSize; c++) {
					double dE_dz = dz[L].get(r);
					double dz_dW = a[L-1].get(c);
					dW[L-1].set(r, c, dE_dz * dz_dW);
				}
			}
			
		}
		
		return new DesiredNetworkUpdate(dW, db);
		
	}
	
	/**
	 * Updates the weights and biases of this network according to the desired updates
	 * passed in. This method averages the desired changes across all updates, scales
	 * the updates by the given learning rate, and then subtracts the now scaled gradients
	 * from the network's weights and biases. 
	 * @param updates
	 * @param learningRate
	 */
	public void updateNetwork(DesiredNetworkUpdate[] updates, double learningRate) {
		
		for(DesiredNetworkUpdate update : updates) {
			
			Matrix[] weightUpdates = update.weights();
			Vector[] biasUpdates = update.biases();
			
			for(int i = 0; i < numLayers() - 1; i++) {
				this.weights[i] = this.weights[i].add(weightUpdates[i].scale(-learningRate/updates.length));
				this.biases[i] = this.biases[i].add(biasUpdates[i].scale(-learningRate/updates.length));
			}
			
		}
		
	}
	
	/**
	 * This method calculates the number of layers in this network by referencing
	 * the layerSizes array given at construction. 
	 * @return the number of layer's in the network
	 */
	public int numLayers() {
		return layerSizes.length;
	}
	
	/**\
	 * Returns a string representation of the network, displaying each layer's
	 * weights and biases. 
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 1; i < numLayers(); i++) {
			
			//Layer i
			sb.append("Layer " + i + "\n========\n");
			sb.append("Weights:\n");
			sb.append(weights[i-1].toString() + "\n");
			sb.append("Biases:\n");
			sb.append(biases[i-1].toString() + "\n");
			
			sb.append("\n");
			
		}
		
		return sb.toString();
		
	}
	
	/**
	 * Returns a copy of the array containing the size of each layer of this network.
	 * @return a copy of the layerSizes array
	 */
	public int[] layerSizes() {
		return Arrays.copyOf(layerSizes, layerSizes.length);
	}
	
	/**
	 * Returns a copy of the array containing the activation functions of each layer of this network.
	 * @return a copy of the functions array
	 */
	public ActivationFunction[] activationFunctions() {
		return Arrays.copyOf(functions, functions.length);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String name() {
		return this.name;
	}
	
	public boolean equals(Network other) {
		
		if(this.layerSizes.length != other.layerSizes.length) return false;
		
		for(int layer = 0; layer < this.layerSizes.length; layer++) {
			if(this.layerSizes[layer] != other.layerSizes[layer]) return false;
		}
		
		for(int i = 0; i < layerSizes.length - 1; i++) {
			
			if(!this.weights[i].equals(other.weights[i])) return false;
			if(!this.biases[i].equals(other.biases[i])) return false;
			
		}
		
		return true;
		
	}
	
}
