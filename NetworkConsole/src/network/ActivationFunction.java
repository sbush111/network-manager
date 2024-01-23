package network;

import java.util.function.Function;

public class ActivationFunction {

	private Function<Double, Double> function;
	private Function<Double, Double> derivative;
	
	public ActivationFunction(Function<Double, Double> function, Function<Double, Double> derivative) {
		this.function = function;
		this.derivative = derivative;
	}
	
	public double apply(double x) {
		return function.apply(x);
	}
	
	public double differentiate(double x) {
		return derivative.apply(x);
	}
	
	public static final ActivationFunction SIGMOID = new ActivationFunction(
			(x) -> 1.0 / (1 + Math.exp(-x)),
			(x) -> Math.exp(-x) / Math.pow(Math.exp(-x) + 1, 2)
		);
	
	public static final ActivationFunction RELU = new ActivationFunction(
			(x) -> (x > 0) ? x : 0.0,
			(x) -> (x > 0) ? 1.0 : 0.0
		);
	
	public static final ActivationFunction LEAKY_RELU = new ActivationFunction(
			(x) -> (x > 0) ? x : 0.01 * x,
			(x) -> (x > 0) ? 1 : 0.01
		);
	
	public static final ActivationFunction TANH = new ActivationFunction(
			(x) -> Math.tanh(x),
			(x) -> 1 / Math.pow(Math.cosh(x), 2)
		);
	
	public static final ActivationFunction STEP = new ActivationFunction(
			(x) -> (x > 0) ? 1.0 : 0.0,
			(x) -> 0.0
		);
	
	public static final ActivationFunction IDENTITY = new ActivationFunction(
			(x) -> x,
			(x) -> 1.0
		);
	
	@Override
	public String toString() {
		
		if(this == ActivationFunction.SIGMOID) return "Sigmoid";
		else if(this == ActivationFunction.RELU) return "ReLU";
		else if(this == ActivationFunction.LEAKY_RELU) return "Leaky ReLU";
		else if(this == ActivationFunction.TANH) return "TanH";
		else if (this == ActivationFunction.STEP) return "Step Function";
		else if(this == ActivationFunction.IDENTITY) return "Identity Function";
		else return "Custom Activation Function";
		
	}
	
}
