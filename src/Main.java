/* This program computes a mesh of a 2-dimensional surface,
   then attemps to solve a differential equation on that space. */

import java.awt.Dimension;

public class Main{


	public static void main(String args[]){


		System.out.println("This is the main.");
				
		int nbVehicles = 1;
		double width = 20.0;
		double height = 10.0;
		World mesh = new World(nbVehicles, width, height);
		GraphicPanel panel = new GraphicPanel(mesh);
		Window window = new Window(panel);
		panel.repaint();
		window.pack();
		window.setSize(new Dimension(1800, 800));
		System.out.println("End.");
	}
}
