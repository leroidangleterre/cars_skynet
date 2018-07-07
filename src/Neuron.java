import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/** One single element of a neural network.
 * It know its inputs and requests their current value when it is evaluated.
 */

public class Neuron{
	
	private static int nbNeurons = 0;
	protected int id;
	
	private double threshold;
	private double value;
	
	protected ArrayList<Neuron>inputs;
//	private ArrayList<Integer>coefficients; // Not used at first, all values treated equally.
	
	// Tool used to organize the graphic representation of the neural net.
//	private GraphNode graphNode;
	int x, y;
	int radius = 3;
	
	public Neuron(){
		this.inputs = new ArrayList<Neuron>();
		this.threshold = 1.5;
		this.compute();
		
//		this.coefficients = new ArrayList<Integer>();
//		this.graphNode = null;
		
		this.x = 0;
		this.y = 0;
		
		this.id = Neuron.nbNeurons;
		Neuron.nbNeurons++;
	}
	
	public double getValue(){
		return this.value;
	}

	public void compute(){
		double sum = 0;

		if(inputs != null){
			for(Neuron input: inputs){
				sum += input.getValue();
			}
		}
		if(sum >= this.threshold){
			this.value = 1;
		}
		else{
			this.value = 0;
		}
	}
	

	
	public String toString(){
		return "Neuron " + this.id + ": score: " + this.getValue() + ", inputs: " + this.inputs.size() + ".";
	}
	
	public void takeNewInput(Neuron input, int value){
		if(input != this && !this.inputs.contains(input)){
			this.inputs.add(input);
			System.out.println("Neuron " + this.id + " taking neuron " + input.id + " as a new input.");
//			this.coefficients.add(new Integer(value));
		}
//		else{
//			System.out.println("Error: cannot assign neuron as own input");
//		}
	}

//	public void changeRandomCoefficient(){
//		if(coefficients.size() > 0){
//			int index = (int)(Math.random() * coefficients.size());
//			int val = (int)(Math.random() * 10);
//			this.coefficients.set(index, new Integer(val));
//		}	
//	}
	
	
//	/** Get a graph node for this neuron (it will be created if it does not exist yet).
//	 * Make sure it is positionned properly: if it has no links, it must still be in the specified box (xMin,...)
//	 */
//	public GraphNode getGraphNode(int xMin, int xMax, int yMin, int yMax){
//		if(this.graphNode == null){
//			this.graphNode = new GraphNode(this, xMin, xMax, yMin, yMax);
//		}
//		return this.graphNode;
//	}
//	private GraphNode getGraphNode(){
//		return this.getGraphNode(0, 0, 0, 0);
//	}
//	public void setGraphNode(GraphNode node){
//		this.graphNode = node;
//	}

	public boolean hasDoubleInput(){
		boolean res = false;
		for(int i=0; i<this.inputs.size(); i++){
			for(int j=0; j<this.inputs.size(); j++){
				if(i != j){
					if(this.inputs.get(i).id == this.inputs.get(j).id){
						/* The current neuron has two different inputs
						 * that come from the same neuron. Not correct ! */
						res = true;
					}
				}
			}
		}
		return res;
	}
	
	public void setPos(int xParam, int yParam){
		this.x = xParam;
		this.y = yParam;
	}
	
	public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom){
		
		if(this.value == 1){
			g.setColor(Color.red);
		}
		else{
			g.setColor(Color.black);
		}
		
		int xApp = this.x-this.radius;
		int yApp = this.y-this.radius;
		int size = 2*this.radius;
		g.fillOval(xApp, panelHeight - yApp, size, size);
		System.out.println("x: " + xApp + ", y: " + yApp + ", size: " + size);
	}
}
