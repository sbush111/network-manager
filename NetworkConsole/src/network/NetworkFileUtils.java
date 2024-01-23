package network;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import linearAlgebra.Matrix;
import linearAlgebra.Vector;

public class NetworkFileUtils {
	
	public static Optional<File> getFileFromName(String name) {
		
		File folder = new File(".\\saves");
		
		for(File file : folder.listFiles()) {
			
			try(FileInputStream in = new FileInputStream(file)) {
				
				if(name.equals(NetworkFileUtils.readNameFromFile(in))) return Optional.of(file);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
		}
		
		return Optional.empty();
		
	}
	
	public static boolean saveNetworkToFile(Network network, String name) {
		
		Optional<File> optional = getFileFromName(name);
		
		File file;
		
		if(optional.isPresent()) {
			file = optional.get();
		} else {
			
			int i = 0;
			file = new File(".\\saves\\0.sb");
			
			while(file.exists()) file = new File(String.format(".\\saves\\%d.sb", ++i));
			
		}
		
		try(FileOutputStream out = new FileOutputStream(file)) {
			writeNameToFile(out, name);
			writeNumLayersToFile(out, network);
			writeActivationFunctionsToFile(out, network);
			writeLayerSizesToFile(out, network);
			writeWeightsToFile(out, network);
			writeBiasesToFile(out, network);
		} catch(FileNotFoundException e) {
			System.out.println("Could not complete save operation.");
			System.out.println("Save file may have been corrupted or deleted.");
			System.out.println("Please try again.");
			System.out.println();
			return false;
		} catch (IOException e) {
			System.out.println("Could not complete save operation.");
			System.out.println("Save file may have been corrupted or deleted.");
			System.out.println("Please try again.");
			System.out.println();
			return false;
		}
		
		return true;
		
	}
	
	private static void writeNameToFile(FileOutputStream out, String name) throws IOException {
		
		byte[] bytes = name.getBytes();
		
		int numBytes = bytes.length;
		
		out.write(toBytes(numBytes));
		
		out.write(bytes);
		
	}
	
	private static void writeNumLayersToFile(FileOutputStream out, Network network) throws IOException {
		
		out.write(toBytes(network.numLayers()));
		
	}
	
	private static void writeActivationFunctionsToFile(FileOutputStream out, Network network) throws IOException {
		
		var map = new HashMap<ActivationFunction, Integer>();
		map.put(ActivationFunction.SIGMOID, 1);
		map.put(ActivationFunction.RELU, 2);
		map.put(ActivationFunction.LEAKY_RELU, 3);
		map.put(ActivationFunction.TANH, 4);
		map.put(ActivationFunction.STEP, 5);
		map.put(ActivationFunction.IDENTITY, 6);
		
		for(var af : network.functions) {
			
			out.write(toBytes(map.get(af)));
			
		}
		
	}
	
	private static void writeLayerSizesToFile(FileOutputStream out, Network network) throws IOException {
		
		for(int layer : network.layerSizes) {
			
			out.write(toBytes(layer));
			
		}
		
	}
	
	private static void writeWeightsToFile(FileOutputStream out, Network network) throws IOException { 
		
		for(int layer = 1; layer < network.layerSizes.length; layer++) {
			
			double[][] weights = network.weights[layer - 1].toArray();
			
			for(int r = 0; r < weights.length; r++) for(int c = 0; c < weights[0].length; c++) {
				out.write(toBytes(weights[r][c]));
			}
			
		}
		
	}
	
	private static void writeBiasesToFile(FileOutputStream out, Network network) throws IOException {
		
		for(int layer = 1; layer < network.layerSizes.length; layer++) {
			
			double[] biases = network.biases[layer - 1].toArray();
			
			for(int i = 0; i < biases.length; i++) {
				out.write(toBytes(biases[i]));
			}
			
		}
		
	}
	
	public static Optional<Network> loadNetworkFromFile(String fileName) { 
		
		File file = new File(fileName);
		
		Network network = null;
		
		try(FileInputStream in = new FileInputStream(file)) {
			
			String name = readNameFromFile(in);
			int numLayers = readNumLayersFromFile(in);
			ActivationFunction[] funcs = readActivationFunctionsFromFile(in, numLayers);
			int[] layerSizes = readLayerSizesFromFile(in, numLayers);
			Matrix[] weights = readWeightsFromFile(in, layerSizes);
			Vector[] biases = readBiasesFromFile(in, layerSizes);
			
			network = new Network(name, layerSizes, funcs, weights, biases);
			
		} catch(FileNotFoundException e) {
			System.out.println("Could not find any such file.");
			return Optional.empty();
		} catch (IOException e) {
			System.out.println("Something went wrong while attempting to read from file.");
			return Optional.empty();
		}
		
		return Optional.of(network);
		
	}
	
	public static String readNameFromFile(FileInputStream in) throws IOException {
		
		return new String(in.readNBytes(toInt(in.readNBytes(4))));
		
	}
	
	private static int readNumLayersFromFile(FileInputStream in) throws IOException {
		
		return toInt(in.readNBytes(4));
		
	}
	
	private static ActivationFunction[] readActivationFunctionsFromFile(FileInputStream in, int numLayers) throws IOException {
		
		var map = new HashMap<Integer, ActivationFunction>();
		map.put(1, ActivationFunction.SIGMOID);
		map.put(2, ActivationFunction.RELU);
		map.put(3, ActivationFunction.LEAKY_RELU);
		map.put(4, ActivationFunction.TANH);
		map.put(5, ActivationFunction.STEP);
		map.put(6, ActivationFunction.IDENTITY);
		
		ActivationFunction[] funcs = new ActivationFunction[numLayers];
		
		for(int i = 0; i < numLayers; i++) {
			funcs[i] = map.get(toInt(in.readNBytes(4)));
		}
		
		return funcs;
		
	}
	
	private static int[] readLayerSizesFromFile(FileInputStream in, int numLayers) throws IOException {
		
		int[] layerSizes = new int[numLayers];
		
		for(int i = 0; i < numLayers; i++) {
			layerSizes[i] = toInt(in.readNBytes(4));
		}
		
		return layerSizes;
		
	}
	
	private static Matrix[] readWeightsFromFile(FileInputStream in, int[] layerSizes) throws IOException {
		
		Matrix[] allWeights = new Matrix[layerSizes.length - 1];
		
		for(int layer = 0; layer < allWeights.length; layer++) {
			
			double[][] weights = new double[layerSizes[layer+1]][layerSizes[layer]];
			
			for(int r=0; r<weights.length; r++) for(int c=0; c<weights[0].length; c++) {
				
				weights[r][c] = toDouble(in.readNBytes(8));
				
			}
			
			allWeights[layer] = new Matrix(weights);
			
		}
		
		return allWeights;
		
	}
	
	private static Vector[] readBiasesFromFile(FileInputStream in, int[] layerSizes) throws IOException {
		
		Vector[] allBiases = new Vector[layerSizes.length-1];
		
		for(int layer = 0; layer < allBiases.length; layer++) {
			
			double[] biases = new double[layerSizes[layer + 1]];
			
			for(int i=0; i<biases.length; i++) {
				biases[i] = toDouble(in.readNBytes(8));
			}
			
			allBiases[layer] = new Vector(biases);
			
		}
		
		return allBiases;
		
	}
	
	private static byte[] toBytes(int integer) {
		
		byte[] bytes = new byte[4];
		int index = 3;
		
		while(integer > 0) {
			bytes[index--] = (byte)(integer & 0xFF);
			integer >>>= 8;
		}
		
		return bytes;
		
	}
	
	private static byte[] toBytes(double _double) {
		
		long longRepresentation = Double.doubleToLongBits(_double);
		
		byte[] bytes = new byte[8];
		
		for(int i = 7; i >= 0; i--) {
			bytes[i] = (byte) longRepresentation;
			longRepresentation >>= 8;
		}
		
		return bytes;
		
	}
	
	private static int toInt(byte[] bytes) {
		
		if(bytes.length != 4) 
			throw new IllegalArgumentException("Integer could not be parsed. Integers are represented with four bytes.");
		
		int x = 0;
		
		for(byte b : bytes) {
			x <<= 8;
			x |= b;
		}
		
		return x;
		
	}
	
	private static double toDouble(byte[] bytes) {
		
		long longRepresentation = 0;
		
		for(int i = 0; i < 8; i++) {
			
			longRepresentation += (bytes[i] & 0xFF);
			
			if(i < 7) longRepresentation <<= 8;
			
		}
		
		return Double.longBitsToDouble(longRepresentation);
		
	}
	
	public static File getIndex() {
		
		createIndexIfAbsent();
		
		File file = new File(".\\index.sb");
		
		return file;
		
	}
	
	private static void createIndexIfAbsent() {
		
		File file = new File(".\\index.sb");
		if(file.exists()) return;
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			retry_createIndexIfAbsent(1);
		}
		
	}
	
	private static void retry_createIndexIfAbsent(int count) {
		
		if(count >= 10) {
			System.out.println("Fatal error, save index file cannot be generated");
			System.exit(-1);
		}
		
		File file = new File(".\\saves\\index.sb");
		if(file.exists()) return;
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			retry_createIndexIfAbsent(count+1);
		}
		
	}
	
	
	////////////////////////////////////////////////Legacy code for interacting with the datasets
	
	public static byte[] getPixels(DataSet set, int pictureIndex) {
		
		int maxIndex = set == DataSet.train ? 60000 : 10000;
		
		if(pictureIndex >= maxIndex || pictureIndex < 0) throw new IndexOutOfBoundsException(
				"Index " + pictureIndex + " is out of bounds for the " + set + "ing dataset."
				);
		
		byte[] pixels = new byte[784];
		
		File picturesFile = new File(".\\data\\" + ((set == DataSet.train) ? "trainingPictures.sb" : "testingPictures.sb"));
		
		try(FileInputStream in = new FileInputStream(picturesFile)){
			
			in.skip(784 * pictureIndex);
			
			in.read(pixels);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pixels;
		
	}
	
	public static Vector getInputVector(DataSet set, int pictureIndex) {
			
		byte[] pixels = NetworkFileUtils.getPixels(set, pictureIndex);
		
		Vector vec = new Vector(pixels.length);
		
		for(int i = 0; i < pixels.length; i++) {
			
			vec.set(i, (double)Byte.toUnsignedInt(pixels[i]));
			
		}
		
		return vec;
		
	}

	public static int getAnswer(DataSet set, int pictureIndex) {
		
		int answer = 0;
		
		File file = new File(".\\data\\" + ((set == DataSet.train) ? "trainingAnswers.sb" : "testingAnswers.sb"));
		
		try(FileInputStream in = new FileInputStream(file)) {
			
			in.skip(pictureIndex / 2);
			
			byte[] b = new byte[1];
			
			in.read(b);
			
			if(pictureIndex % 2 == 0) b[0] >>>= 4;
			
			answer = Byte.toUnsignedInt((byte) (b[0] & 0b1111));
			
		} catch (IOException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
			
		return answer;
		
	}
	
	public static BufferedImage render(byte[] pixels) {
		
		BufferedImage image = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
		
		int r = 0, c = 0;
		
		for(int i = 0; i < 784; i++) {
			
			int grayScale = Byte.toUnsignedInt(pixels[i]);
			
			int rgb = grayScale;
			rgb <<= 8;
			rgb += grayScale;
			rgb <<= 8;
			rgb += grayScale;
			
			image.setRGB(c, r, rgb);
			
			c++;
			
			if(c == 28) {
				r++;
				c = 0;
			}
			
		}
		
		return image;
		
	}
	
	public static void display(DataSet set, int pictureIndex) {
		
		JFrame frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		Image image = render(getPixels(set, pictureIndex));
		image = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(image));
		label.setFont(new Font("Arial", Font.PLAIN, 24));
		label.setText(String.valueOf(getAnswer(set, pictureIndex)));
		frame.add(label);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
}