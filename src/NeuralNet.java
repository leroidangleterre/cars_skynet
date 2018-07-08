import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class NeuralNet{

	private int centralListSize = 20;

	// Input data. The net must have one first-layer node for each element in
	// that list.
	private ArrayList<Neuron> inputList;

	// The nodes that make the inner, invisible part the network.
	// One layer for now.
	private ArrayList<Neuron> centralList;

	// The output nodes.
	private ArrayList<Neuron> outputList;

	// All neurons (or sensors) that make this net.
	private ArrayList<Neuron> allNeurons;

	public NeuralNet(List<Sensor> sensorList, List<Neuron> outputList){

		double posX, posY;
		int column; // If the input list is too large, it is displayed on several columns.
		int columnMaxSize = 10;
		int j;

		this.inputList = new ArrayList<Neuron>();
		posX = 0;

		// Make a link between each sensor and the associated neuron.
		j = 1;
		column = 0;
		for(Sensor s : sensorList){
			Neuron inputNeuron = new Neuron();
			s.setNeuron(inputNeuron);
			if (j > columnMaxSize){
				// Switch to a new column.
				j = 1;
				posX++;
			}
			posY = j;
			inputNeuron.setPos(posX, posY);
			this.inputList.add(inputNeuron);
			j++;
		}

		this.outputList = (ArrayList<Neuron>)outputList;
		this.centralList = new ArrayList<Neuron>();
		posX += 3;
		j = 1;
		for(int i = 0; i < centralListSize; i++){
			Neuron centralNeuron = new Neuron();
			posY = j;
			centralNeuron.setPos(posX, posY);
			this.centralList.add(centralNeuron);
			j++;
		}

		// Set the coordinates of the output neurons.
		posX += 3;
		j = 1;
		for(int i = 0; i < outputList.size(); i++){
			Neuron outputNeuron = outputList.get(i);
			posY = j;
			outputNeuron.setPos(posX, posY);
			j++;
		}

		this.allNeurons = new ArrayList<Neuron>();
		this.allNeurons.addAll(inputList);
		this.allNeurons.addAll(centralList);
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
			// this.linkOutputToCentral();
			// }else if (randomValue < 4){
			// this.addNewCentralNeuron();
			// }else{
			this.changeRandomCoefficient();
		}
	}

	public void addNewCentralNeuron(){
		Neuron theNewNeuron = new Neuron();
		this.centralList.add(theNewNeuron);
		this.allNeurons.add(theNewNeuron);
		linkCentralToInput();
		linkOutputToCentral();
	}

	private void changeRandomCoefficient(){

		int index = (int)(Math.random() * this.allNeurons.size());
		Neuron n = this.allNeurons.get(index);
		// n.changeRandomCoefficient();
	}

	/**
	 * Randomly choose an output and a central neuron, and make a link between them.
	 */
	public void linkOutputToCentral(){
		Neuron selectedOutput, selectedCentral;
		int value = (int)(Math.random() * 0.01);

		if (this.outputList.size() > 0){
			int outputIndex = (int)(Math.random() * this.outputList.size());
			selectedOutput = this.outputList.get(outputIndex);

			if (this.centralList.size() > 0){
				int nodeIndex = (int)(Math.random() * this.centralList.size());
				selectedCentral = this.centralList.get(nodeIndex);
				// System.out.println("Linking output " + selectedOutput + " to central " + selectedCentral);

				selectedOutput.takeNewInput(selectedCentral, value);
			}else{
				// System.out.println("ERROR: no neuron in central list;");
			}
		}
	}

	/**
	 * Randomly choose an input and a central neuron, and make a link between them.
	 */
	public void linkCentralToInput(){
		Neuron selectedCentral, selectedInput;
		int value = (int)(Math.random() * 0.01);

		if (this.centralList.size() > 0){
			int nodeIndex = (int)(Math.random() * this.centralList.size());
			selectedCentral = this.centralList.get(nodeIndex);

			if (this.inputList.size() > 0){
				int inputIndex = (int)(Math.random() * this.inputList.size());

				selectedInput = this.inputList.get(inputIndex);
				// System.out.println("Linking central " + selectedCentral + " to input" + selectedInput);

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

		/* The three lists (input, center, output) are displayed on three different columns. */
		for(Neuron n : allNeurons){
			n.paint(g, panelHeight, x0, y0, zoom);
		}
	}

	// Use all the values of the input neurons to compute the new value of all neurons.
	public void compute(){

		for(Neuron n : this.allNeurons){
			n.compute();
		}
	}
}
