import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * One single element of a neural network.
 * It know its inputs and requests their current value when it is evaluated.
 */

public class Neuron{

	private static int nbNeurons = 0;
	protected int id;

	private double threshold;
	private double value;

	protected ArrayList<Neuron> outputs;
	// private ArrayList<Integer>coefficients; // Not used at first, all values treated equally.

	double x, y;
	double radius = 3;

	public Neuron(){
		this.outputs = new ArrayList<Neuron>();
		this.threshold = 1.5;
		this.compute();

		// this.coefficients = new ArrayList<Integer>();
		// this.graphNode = null;

		this.x = 0;
		this.y = 0;

		this.id = Neuron.nbNeurons;
		Neuron.nbNeurons++;
	}

	/**
	 * Artificially set the value of the neuron. To be used only for the first layer of neuron,
	 * not for computation.
	 * 
	 * @param val
	 *            the value of the neuron.
	 */
	public void setValue(double val){
		this.value = val;
	}

	public double getValue(){
		return this.value;
	}

	public void compute(){
		double sum = 0;

		if (outputs != null){
			for(Neuron input : outputs){
				sum += input.getValue();
			}
		}
		if (sum >= this.threshold){
			this.value = 1;
		}else{
			this.value = 0;
		}
	}

	public String toString(){
		return "Neuron " + this.id + ": score: " + this.getValue() + ", inputs: " + this.outputs.size() + ".";
	}

	public void takeNewInput(Neuron input, int value){
		if (input != this && !this.outputs.contains(input)){
			this.outputs.add(input);
			System.out.println("Neuron " + this.id + " taking neuron " + input.id + " as a new input.");
			// this.coefficients.add(new Integer(value));
		}
		// else{
		// System.out.println("Error: cannot assign neuron as own input");
		// }
	}

	// public void changeRandomCoefficient(){
	// if(coefficients.size() > 0){
	// int index = (int)(Math.random() * coefficients.size());
	// int val = (int)(Math.random() * 10);
	// this.coefficients.set(index, new Integer(val));
	// }
	// }

	// /** Get a graph node for this neuron (it will be created if it does not exist yet).
	// * Make sure it is positionned properly: if it has no links, it must still be in the specified box (xMin,...)
	// */
	// public GraphNode getGraphNode(int xMin, int xMax, int yMin, int yMax){
	// if(this.graphNode == null){
	// this.graphNode = new GraphNode(this, xMin, xMax, yMin, yMax);
	// }
	// return this.graphNode;
	// }
	// private GraphNode getGraphNode(){
	// return this.getGraphNode(0, 0, 0, 0);
	// }
	// public void setGraphNode(GraphNode node){
	// this.graphNode = node;
	// }

	public boolean hasDoubleInput(){
		boolean res = false;
		for(int i = 0; i < this.outputs.size(); i++){
			for(int j = 0; j < this.outputs.size(); j++){
				if (i != j){
					if (this.outputs.get(i).id == this.outputs.get(j).id){
						/*
						 * The current neuron has two different inputs
						 * that come from the same neuron. Not correct !
						 */
						res = true;
					}
				}
			}
		}
		return res;
	}

	public void setPos(double xParam, double yParam){
		this.x = xParam;
		this.y = yParam;
	}

	public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom){

		double size = 2 * this.radius;

		if (this.value == 1){
			g.setColor(Color.red);
			size = 2.5 * size;
		}else{
			g.setColor(Color.black);
		}

		int xApp = (int)(x0 + this.x * zoom);
		int yApp = (int)(panelHeight - (y0 + this.y * zoom));
		g.fillOval(xApp, yApp, (int)size, (int)size);
		// System.out.println("x: " + xApp + ", y: " + yApp + ", size: " + size);
	}
}
