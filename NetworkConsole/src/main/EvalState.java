package main;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import network.DataSet;
import network.NetworkFileUtils;

public class EvalState implements State {

	public void run(Main main) {
		new Window(main);
		System.out.println();
		main.state = main.defaultState;
	}
	
	@SuppressWarnings("serial")
	public class Window extends JFrame {
		
		int index = 0;
		int numPictures = 10;
		ImageIcon[] images;
		int[] answers;
		
		JLabel pictureFrame;
		JLabel answerLabel;
		
		public Window(Main main) {
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setBounds(10, 10, 410, 325);
			this.setLayout(null);
			this.setResizable(false);
			this.setTitle("Network Manager");
			
			var backButton = new JButton();
			backButton.setText("<");
			backButton.setBounds(10, 80, 80, 50);
			backButton.addActionListener(this::onBackButtonPressed);
			this.add(backButton);
			
			pictureFrame = new JLabel();
			var image = NetworkFileUtils.render(NetworkFileUtils.getPixels(DataSet.test, 0));
			pictureFrame.setIcon(new ImageIcon(scale(image, 7)));
			pictureFrame.setBounds(100, 10, pictureFrame.getIcon().getIconWidth(), pictureFrame.getIcon().getIconHeight());
			
			this.add(pictureFrame);

			var forwardButton = new JButton();
			forwardButton.setText(">");
			forwardButton.setBounds(306, 80, 80, 50);
			forwardButton.addActionListener(this::onForwardButtonPressed);
			this.add(forwardButton);
			
			answerLabel = new JLabel();
			answerLabel.setText("7");
			answerLabel.setFont(new Font("Arial", Font.BOLD, 24));
			answerLabel.setBounds(190, 240, 20, 20);
			this.add(answerLabel);
			
			int[] examples = generateExamples(numPictures);
			
			images = new ImageIcon[numPictures];
			answers = new int[numPictures]; 
			for(int i = 0; i < examples.length; i++) {
				byte[] pixels = NetworkFileUtils.getPixels(DataSet.test, examples[i]);
				images[i] = new ImageIcon(scale(NetworkFileUtils.render(pixels), 7));
				answers[i] = main.loadedNetwork.forwardprop(NetworkFileUtils.getInputVector(DataSet.test, examples[i]))
						.output()
						.indexOfMax();
			}
			
			pictureFrame.setIcon(images[0]);
			answerLabel.setText(String.valueOf(answers[0]));
			
			this.setVisible(true);
			
		}
		
		public void onBackButtonPressed(ActionEvent e) {
			index -= 1;
			if(index < 0) index = images.length - 1;
			pictureFrame.setIcon(images[index]);
			answerLabel.setText(String.valueOf(answers[index]));
		}
		
		public void onForwardButtonPressed(ActionEvent e) {
			index += 1;
			if(index == images.length) index = 0;
			pictureFrame.setIcon(images[index]);
			answerLabel.setText(String.valueOf(answers[index]));
		}
		
		public static Image scale(BufferedImage image, double scalingFactor) {
			
			return image.getScaledInstance(
					(int) (scalingFactor * image.getWidth()), 
					(int) (scalingFactor * image.getHeight()), 
					Image.SCALE_SMOOTH
					);
			
		}
		
		public static int[] generateExamples(int numExamples) {
			
			var examples = new int[numExamples];

			var hs = new HashSet<Integer>();
			var rng = new Random();
			
			examples[0] = rng.nextInt(10_000);
			hs.add(examples[0]);
			
			for(int i = 1; i < numExamples; i++) {
				examples[i] = rng.nextInt(10_000);
				while(hs.contains(examples[i])) 
					examples[i] = rng.nextInt(10_000);
				hs.add(examples[i]);
			}
			
			return examples;
			
		}
		
	}
	
}