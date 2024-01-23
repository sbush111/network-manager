package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import linearAlgebra.Vector;

import javax.imageio.ImageIO;

public class EvalState implements State {

	public void run(Main main) {
		
		System.out.println("Type the absolute file path of the image you'd like to evaluate:");
		File file = new File(main.scanner.nextLine());
		
		if(!file.exists()) {
			System.out.println("No such file exists.");
			System.out.println();
			main.state = main.defaultState;
			return;
		}
		
		BufferedImage image;
		
		
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Something went wrong, please ensure that the image file"
					+ "\nis in a standard image file format such as png, jpg, or gif.");
			System.out.println();
			main.state = main.defaultState;
			return;
		}
		
		if(image.getWidth() != 28 || image.getHeight() != 28) {
			System.out.println("Image must be 28 by 28 pixels. Please resize the image\n"
					+ "and then try again.");
			System.out.println();
			main.state = main.defaultState;
			return;
		}
		
		double[] pixels = new double[784];
		
		for(int r = 0; r < 28; r++) {
			
			for(int c = 0; c < 28; c++) {
				
				int rgb = image.getRGB(c, r);
				
				int red = (rgb >> 16) & 0xFF;
				int blue = (rgb >> 8) & 0xFF;
				int green = rgb & 0xFF;
				
				int avg = (red + blue + green) / 3;
				
				int i = r * 28 + c;
				pixels[i] = (byte) avg;
				
			}
			
		}
		
		int result = main.loadedNetwork.forwardprop(new Vector(pixels)).output().indexOfMax();
		
		System.out.println("This network has evaluated this image, and determined that it is the number " + result + ".");
		System.out.println();
		
		main.state = main.defaultState;
		
	}
	
}
