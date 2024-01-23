package linearAlgebra;

import java.util.Arrays;
import java.util.function.Function;

public class Vector {

private double[] data;
	
	/**
	 * Constructs a new Vector object of the given size, 
	 * initializing all entries to zero.
	 * @param size
	 */
	public Vector(int size) {
		data = new double[size];
	}
	
	/**
	 * Constructs a new Vector object, containing the same elements 
	 * as the given array.
	 * @param arr
	 */
	public Vector(double[] arr) {
		this.data = new double[arr.length];
		for(int i = 0; i < arr.length; i++)
			data[i] = arr[i];
	}
	
	/**
	 * Sets the value at the given index equal to the given value.
	 * @param index
	 * @param val
	 */
	public void set(int index, double val) {
		data[index] = val;
	}
	
	/**
	 * Gets the value at the given index.
	 * @param index
	 * @return value at index
	 */
	public double get(int index) {
		return data[index];
	}
	
	/**
	 * Returns an array representation of the current vector, 
	 * using the copyOf() method from java.util.Arrays.
	 * @return array
	 */
	public double[] toArray() {
		return Arrays.copyOf(data, data.length);
	}
	
	/**
	 * Returns the size of the vector.
	 * @return size
	 */
	public int size() {
		return data.length;
	}
	
	/**
	 * Returns the sum of the current Vector and the given Vector 
	 * as a new Vector object.
	 * @param addend
	 * @return sum
	 */
	public Vector add(Vector addend) {
		
		if(addend.size() != this.size())
			throw new IllegalArgumentException("Cannot add vectors of different length.");
		
		Vector sum = new Vector(addend.size());
		
		for(int i = 0; i < addend.size(); i++) {
			
			sum.set(i, this.get(i) + addend.get(i));
			
		}
		
		return sum;
		
	}
	
	/**
	 * Returns a new Vector object equal to the current vector
	 * after having scaled each of its entries by the given
	 * scaling factor.
	 * @param scalingFactor
	 * @return scaled vector
	 */
	public Vector scale(double scalingFactor) {
		
		Vector scaledVector = new Vector(this.size());
		
		for(int i = 0; i < scaledVector.size(); i++) {
			
			scaledVector.set(i, scalingFactor * this.get(i));
			
		}
		
		return scaledVector;
		
	}
	
	/**
	 * Returns the magnitude of the vector.
	 * @return magnitude
	 */
	public double magnitude() {
		
		double sumOfSquares = 0;
		
		for(double entry : this.data) sumOfSquares += Math.pow(entry, 2);
		
		return Math.sqrt(sumOfSquares);
		
	}
	
	/**
	 * Returns the result of taking the dot product of the current vector
	 * with the given vector.
	 * @param factor
	 * @return dot product
	 */
	public double dot(Vector factor) {
		
		if(this.size() != factor.size()) 
			throw new IllegalArgumentException("Vectors cannot be dotted unless they are the same length");
		
		double dot = 0;
		
		for(int i = 0; i < this.size(); i++) {
			
			dot += (this.get(i) * factor.get(i));
			
		}
		
		return dot;
		
	}
	
	/**
	 * Returns the index of the maximum value in this vector. Since
	 * the neural network will be outputting a vector given some
	 * input data, a convenient way of selecting which option is the
	 * most likely according to the network, is to have this method
	 * for identifying the largest element of the vector.
	 * @return index of maximum value
	 */
	public int indexOfMax() {
		
		double max = data[0];
		int index = 0;
		
		for(int i = 0; i < data.length; i++) {
			if(data[i] > max) {
				max = data[i];
				index = i;
			}
		}
		
		return index;
		
	}
	
	/**
	 * Returns a new vector equal to the result of applying
	 * the given function to every element of the current vector.
	 * @param function
	 * @return vector after applying function
	 */
	public Vector apply(Function<Double, Double> function) {
		
		Vector output = new Vector(data.length);
		
		for(int i = 0; i < data.length; i++) {
			output.set(i, function.apply(data[i]));
		}
		
		return output;
		
	}
	
	public boolean equals(Vector other) {
		
		if(this.size() != other.size()) return false;
		
		for(int i = 0; i < this.size(); i++) if(this.data[i] != other.data[i]) return false;
		
		return true;
		
	}
	
	/**
	 * Returns a string representation of the contents of the
	 * vector using the toString() method from java.util.Arrays
	 * on the array used to store the vector data.
	 */
	@Override
	public String toString() {
		return Arrays.toString(data);
	}
	
}
