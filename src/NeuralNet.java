import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class NeuralNet{

	// Input data. The net must have one first-layer node for each element in
	// that list.
	private ArrayList<Neuron> inputList;

	// The nodes that make the inner, invisible part the network.
	private ArrayList<Neuron> nodeList;

	// The output nodes.
	private ArrayList<Neuron> outputList;

	// The list of visual GraphNodes (the ones that will be displayed on screen).
	private ArrayList<GraphNode> graphNodes;
	// Graphic coordinates of the drawing region.
	private int xMin, xMax, yMin, yMax;
	
	// All neurons (or sensors) that make this net.
	private ArrayList<Neuron> allNeurons;
	
	public NeuralNet(ArrayList<Sensor> inputList, ArrayList<Neuron> outputList){
		this.inputList = new ArrayList<Neuron>();
		
		this.outputList = outputList;
		this.nodeList = new ArrayList<Neuron>();
		this.allNeurons = new ArrayList<Neuron>();
		
		
		this.allNeurons.addAll(nodeList);
		this.allNeurons.addAll(outputList);
	}

	/**
	 * Randomly select one node from the nodeList or the outputList, have it
	 * change either a coefficient or a connection to another node, or add a
	 * neuron in the central list.
	 */
	public void mutate(){
		int randomValue = (int)(Math.random() * 10);

		if (randomValue < 2){
			this.linkOutputToCentral();
		}else if (randomValue < 4){
			this.addNewCentralNeuron();
		}else{
			this.changeRandomCoefficient();
		}
	}

	public void addNewCentralNeuron(){
		Neuron theNewNeuron = new Neuron();
		this.nodeList.add(theNewNeuron);
		this.allNeurons.add(theNewNeuron);
		linkCentralToInput();
		linkOutputToCentral();
	}

	private void changeRandomCoefficient(){

		int index = (int)(Math.random() * this.allNeurons.size());
		Neuron n = this.allNeurons.get(index);
//		n.changeRandomCoefficient();
	}

	/** Create a link between an output neuron and an inner neuron
	 * (if any can be found).
	 */
	public void linkOutputToCentral(){
		Neuron selectedOutput, selectedCentral;
		int value = (int)(Math.random() * 0.01);

		if (this.outputList.size() > 0){
			int outputIndex = (int)(Math.random() * this.outputList.size());
			selectedOutput = this.outputList.get(outputIndex);

			if (this.nodeList.size() > 0){
				int nodeIndex = (int)(Math.random() * this.nodeList.size());
				selectedCentral = this.nodeList.get(nodeIndex);
//				System.out.println("Linking output " + selectedOutput + " to central " + selectedCentral);

				selectedOutput.takeNewInput(selectedCentral, value);
			}
			else{
//				System.out.println("ERROR: no neuron in central list;");
			}
		}
	}

	/** Create a link between an inner neuron and an input neuron
	 * (if any can be found).
	 */
	public void linkCentralToInput(){
		Neuron selectedCentral, selectedInput;
		int value = (int)(Math.random() * 0.01);

		if (this.nodeList.size() > 0){
			int nodeIndex = (int)(Math.random() * this.nodeList.size());
			selectedCentral = this.nodeList.get(nodeIndex);

			if (this.inputList.size() > 0){
				int inputIndex = (int)(Math.random() * this.inputList.size());

				selectedInput = this.inputList.get(inputIndex);
//				System.out.println("Linking central " + selectedCentral + " to input" + selectedInput);

				selectedCentral.takeNewInput(selectedInput, value);
			}
		}
	}

	/**
	 * Paint the neural net in a subregion of the panel. Display the input nodes
	 * on the left, the output nodes on the right, all other nodes in the
	 * central area. Display the links between nodes.
	 */
	public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom){
		System.out.println("NeuralNet.paint();");
		int radius = 20;

		int subPanelWidth = 600;
		int subPanelHeight = 500;
		xMin = g.getClipBounds().width - subPanelWidth;
		xMax = g.getClipBounds().width;
		yMin = g.getClipBounds().height - subPanelHeight;
		yMax = g.getClipBounds().height;
//		System.out.println("Printing net; xMin = " + xMin +", xMax = " + xMax + ", yMin = " + yMin + ", yMax = " + yMax);

		g.setColor(Color.red);
		g.drawRect(xMin, 0, xMax - xMin, yMax - yMin);

		int x, y;
		
		// All nodes (input, central, outputs) are grouped in this list.
		graphNodes = new ArrayList<>();

		// The input nodes are displayed on a single column on the left.
		int nbInputs = inputList.size();
		System.out.println("nbInput: " + nbInputs);
//		System.out.println("Printing " + nbInputs + " input nodes.");
		for(int i = 0; i < nbInputs; i++){
			Neuron n = this.inputList.get(i);
			x = (int)(xMin + 1.5 * radius);
			y = yMin + (int)(((double)i + 0.5) * (yMax - yMin)) / nbInputs;
			System.out.println("Printing neuron at " + x + ", " + y);
			n.setPos(x, y);
			n.paint(g, panelHeight, x0, y0, zoom);
		}
		
		// The output nodes are displayed on a single column on the right.
		int nbOutputs = outputList.size();
	}
	
	
	// Use all the values of the input neurons to compute the new value of all neurons.
	public void compute(){
		
		for(Neuron n: this.allNeurons){
			n.compute();
		}
	}
}
