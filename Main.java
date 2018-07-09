
/*
 * This program computes a mesh of a 2-dimensional surface,
 * then attemps to solve a differential equation on that space.
 */

import java.awt.Dimension;

public class Main{

	public static void main(String args[]){

		int nbVehicles = 1;
		double width = 20.0;
		double height = 10.0;
		World world = new World(nbVehicles, width, height);
		GraphicPanel carsPanel = new WorldGraphicPanel(world);
		Window carsWindow = new Window(carsPanel);
		carsPanel.repaint();
		carsWindow.pack();
		carsWindow.setSize(new Dimension(800, 800));

		Vehicle v = world.getFirstVehicle();

		NeuralNetGraphicPanel neuralPanel = new NeuralNetGraphicPanel(v.getNeuralNet());
		Window neuralWindow = new Window(neuralPanel);
		neuralPanel.repaint();
		neuralWindow.pack();
		neuralWindow.setSize(800, 800);

	}
}
