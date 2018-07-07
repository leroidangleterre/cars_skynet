import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Vehicle{

	private double x, y, angle;
	private double length, width;
	private double linearSpeed;
	private double maxSpeed;

	// The three nodes used to pilot the car. They are the output of the neural net.
	private Neuron steering;
	private Neuron acc;
	private Neuron brakes;

	private ArrayList<Vehicle> collisionList;

	private static int nbSensors = 7;
	private ArrayList<Sensor> sensorList;

	private NeuralNet net;

	private double currentScore;
	private double totalScore;

	public Vehicle(double xParam, double yParam, double angle){
		this.x = xParam;
		this.y = yParam;
		this.angle = 0;
		this.length = 1.0;
		this.width = 0.4;
		this.linearSpeed = 0.25;
		this.maxSpeed = 20;
		this.collisionList = new ArrayList<>();

		this.steering = new Neuron();
		this.acc = new Neuron();
		this.brakes = new Neuron();
		this.initNeuralNet();

		this.currentScore = 0;
		this.totalScore = 0;
	}

	private void initNeuralNet(){
		this.sensorList = new ArrayList<>();
		ArrayList<Neuron> actuatorList = new ArrayList<Neuron>();
		actuatorList.add(steering);
		actuatorList.add(acc);
		actuatorList.add(brakes);
		int nbDirections = 12;
		int nbLayers = 4;
		double sensorDistance = 0.7;
		initSensors(nbDirections, nbLayers, sensorDistance);

		this.net = new NeuralNet(this.sensorList, actuatorList);

		// TESTS: adding a central neuron.
		System.out.println("Adding a central neuron");
		this.net.addNewCentralNeuron();
		this.net.addNewCentralNeuron();
	}

	public void initSensors(int nbDirections, int nbLayers){
		initSensors(nbDirections, nbLayers, 1.0);
	}

	/**
	 * Create a set of sensors disposed on concentric circles around the vehicle;
	 * 
	 * @param nbDirections
	 *            the amount of sensors on each circle;
	 * @param nbLayers
	 *            the amount of layers of sensors
	 * @param distance
	 *            the distance between two layers of sensors.
	 */
	public void initSensors(int nbDirections, int nbLayers, double distance){
		for(int count = 0; count < nbDirections; count++){
			for(int layer = 0; layer < nbLayers; layer++){
				double xS = distance * (layer + 1) * Math.cos(2 * Math.PI * ((double)count / (double)nbDirections));
				double yS = distance * (layer + 1) * Math.sin(2 * Math.PI * ((double)count / (double)nbDirections));
				Sensor sensor = new Sensor(xS, yS);
				sensor.computeWorldCoordinates(this.x, this.y, this.angle);
				this.sensorList.add(sensor);
			}
			// for(int i = 0; i < Vehicle.nbSensors; i++){
			// double xS = 3 * this.length * (Math.random() - 0.5);
			// double yS = 3 * this.length * (Math.random() - 0.5);
			// Sensor sensor = new Sensor(xS, yS);
			// }
		}
	}

	public Vehicle(double xParam, double yParam){
		this(xParam, yParam, 0);
	}

	public void evolve(double dt){

		// Update the values of the neurons.
		this.net.compute();

		// Take command from the neural net.
		this.angle += steering.getValue() * this.linearSpeed;
		double damping = 1.0; // Not meant to last. Final vehicles will evolve into sth correct.
		this.linearSpeed = this.linearSpeed + acc.getValue() * damping - brakes.getValue() * damping;
		// System.out.println("Linear speed: " + this.linearSpeed + ", dt: " + dt);
		// Actually move the vehicle.
		double dx = dt * this.linearSpeed * Math.cos(this.angle);
		this.x += dx;
		// System.out.println("dx: " + dx + ", x: " + this.x);
		this.y += dt * this.linearSpeed * Math.sin(this.angle);

		this.totalScore += this.currentScore;
	}

	public void rebound(double xMin, double xMax, double yMin, double yMax, double margin){

		// The vehicle is allowed to leave the limits within that margin.
		// TODO: only rebound a vehicle when going outward.
		if (this.x > xMax + margin || this.x < xMin - margin){
			this.angle = Math.PI - this.angle;
		}
		if (this.y > yMax + margin || this.y < yMin - margin){
			this.angle = -this.angle;
		}
	}

	public void paint(Graphics g, double panelHeight, double x0, double y0, double zoom){
		int xApp = (int)(x0 + this.x * zoom);
		int yApp = (int)(panelHeight - (y0 + this.y * zoom));

		Rectangle r = new Rectangle(this.x, this.y, this.length, this.width, this.angle);
		r.display(g, x0, y0, zoom, (int)panelHeight);

		int size = 4;

		g.setColor(Color.RED);
		g.drawRect(xApp - size / 2, yApp - size / 2, size, size);

		g.setColor(Color.WHITE);
		g.drawString(this.totalScore + "", xApp - 10, yApp + 2);
	}

	public void paintNeuralNet(Graphics g, int panelHeight, double x0, double y0, double zoom){
		this.net.paint(g, panelHeight, x0, y0, zoom);
	}

	public void paintSensors(Graphics g, double panelHeight, double x0, double y0, double zoom){
		for(Sensor s : this.sensorList){
			s.paint(g, panelHeight, x0, y0, zoom);
		}
	}

	public void paintCollisions(Graphics g, double panelHeight, double x0, double y0, double zoom){

		for(Vehicle other : this.collisionList){
			// Display a line between the two vehicles.
			int xApp0 = (int)(x0 + this.x * zoom);
			int yApp0 = (int)(panelHeight - (y0 + this.y * zoom));
			int xApp1 = (int)(x0 + other.x * zoom);
			int yApp1 = (int)(panelHeight - (y0 + other.y * zoom));
			g.setColor(Color.ORANGE);
			g.drawLine(xApp0, yApp0, xApp1, yApp1);
		}
	}

	public void rotate(double angleIncr){
		this.angle += angleIncr;
		for(Sensor s : this.sensorList){
			s.computeWorldCoordinates(this.x, this.y, this.angle);
		}
	}

	public double distanceTo(Vehicle v){
		double dx = this.x - v.x;
		double dy = this.y - v.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public void razCollisions(){
		this.collisionList.clear();
		for(Sensor s : this.sensorList){
			s.setColor(Color.BLACK);
			s.setSize(1);
		}
		this.currentScore = 0;
	}

	public void collideWith(Vehicle other){
		// First simple version
		// if(this.distanceTo(other) < this.length + other.length){
		// this.collisionList.add(other);
		// }

		// Version using sensors:
		for(Sensor s : this.sensorList){
			if (s.collideWithVehicle(other)){
				// System.out.println("Collision sensor-vehicle");
				s.setColor(Color.yellow);
				s.setSize(7);
				this.currentScore++;
			}
		}
	}

	public void collideWithBorders(double xMin, double xMax, double yMin, double yMax){
		for(Sensor s : this.sensorList){
			s.setColor(Color.BLACK);
			s.computeWorldCoordinates(this.x, this.y, angle);
			if (s.collideWithBorders(xMin, xMax, yMin, yMax)){
				s.setColor(Color.MAGENTA);
				s.setSize(7);
				this.currentScore++;
			}
		}
	}

	// public void addRandomSensor(){
	// double xS = 3*this.length*(Math.random()-0.5);
	// double yS = 3*this.length*(Math.random()-0.5);
	//
	// Sensor s = new Sensor(xS, yS);
	// s.computeWorldCoordinates(this.x, this.y, angle);
	// this.sensorList.add(s);
	// this.net.addInputNode(s);
	// }

	public boolean containsPoint(double xPoint, double yPoint){
		boolean res = true;

		// convert the param coordinates into the vehicle's own coordinates.
		double dx = xPoint - this.x;
		double dy = yPoint - this.y;

		double xConv = dx * Math.cos(-angle) - dy * Math.sin(-angle);
		double yConv = dy * Math.cos(-angle) + dx * Math.sin(-angle);

		// Horizontal limits of the rectangle:
		if (xConv > this.length / 2 || xConv < -this.length / 2){
			res = false;
		}
		if (yConv > this.width / 2 || yConv < -this.width / 2){
			res = false;
		}

		return res;
	}

	public void updateNodePositions(){
		if (this.net != null){
			// net.updateNodePositions();
		}
	}

	public void mutateNeuralNet(){
		this.net.mutate();
		this.currentScore = 0;
		this.totalScore = 0;
	}

}
