import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class World{
	
	private int nbVehicles;
	private ArrayList<Vehicle>vehicleList;

	private double width, height;
	private double margin;
	
	private boolean isRunning;
	private double dt;
	
	private boolean displayNets;
	
	public World(){
		this(5);
	}
	public World(int nbVehicles, double width, double height){
		this.isRunning = false;
		this.nbVehicles = nbVehicles;
		this.width = width;
		this.height = height;
		this.margin = 0.1*this.width;

		this.vehicleList = new ArrayList<Vehicle>();
		
		this.displayNets = true;
		
		double x, y;
		for(int i=0; i<=this.nbVehicles/2; i++){
			// Vehicles on the left, facing right.
			x = 0.2*Math.random() * this.width;
			y = Math.random() * this.height;
			Vehicle v = new Vehicle(x, y);
			this.vehicleList.add(v);
//			// Vehicles on the right, facing left.
//			x = (0.8 + 0.2*Math.random()) * this.width;
//			y = Math.random() * this.height;
//			v = new Vehicle(x, y);
//			v.rotate(Math.PI);
//			this.vehicleList.add(v);
		}
		double dt = 0.3;
	}
	public World(int nbPoints){
		this(nbPoints, 1.0, 1.0);
	}
	
	/* Return the physical width of the mesh. */
	public double getWidth(){
		return this.width;
	}
	public double getHeight(){
		return this.height;
	}
		

	/* Move each point away from its closest neighbor. */
	public void evolve(){
		if(this.isRunning){
			this.evolveOneStep();
		}
	}
	
	public void evolveOneStep(){
		for(Vehicle v: this.vehicleList){
			if(this.dt == 0){
				this.dt = 0.3;
			}
//			System.out.println("World.evolve: dt = " + this.dt);
			v.evolve(this.dt);
			v.rebound(0, this.width, 0, this.height, margin);
		}
		this.computeCollisions();
	}	
	// Simple version.
	private void computeCollisions(){
		
		for(Vehicle v: this.vehicleList){
			v.razCollisions();
			v.collideWithBorders(0, this.width, 0, this.height);
			for(Vehicle v2: this.vehicleList){
				if(v != v2){
					v.collideWith(v2);
				}
			}
		}
	}
	

	private void paintBorders(Graphics g, double panelHeight, double x0, double y0, double zoom){
		g.setColor(Color.black);
		// Legal borders
		g.drawRect((int)x0, (int)(panelHeight - (y0 + height*zoom)), (int)(width*zoom), (int)(height*zoom));
		// Extended borders
		g.drawRect((int)(x0 - margin*zoom), 
				(int)(panelHeight - (y0 + (height+margin)*zoom)),
				(int)((width + 2*margin)*zoom),
				(int)((height + 2*margin)*zoom));
	}
	
	/* Display the mesh on a graphic panel. */
	public void paint(Graphics g, int panelHeight, double x0, double y0, double zoom){
		
		this.paintBorders(g, panelHeight, x0, y0, zoom);
		
		for(Vehicle v: this.vehicleList){
			v.paint(g, panelHeight, x0, y0, zoom);
		}
		for(Vehicle v: this.vehicleList){
			v.paintCollisions(g, panelHeight, x0, y0, zoom);
		}
		for(Vehicle v: this.vehicleList){
			v.paintSensors(g, panelHeight, x0, y0, zoom);
		}
		if(this.displayNets){
			for(Vehicle v: this.vehicleList){
				v.paintNeuralNet(g, panelHeight, x0, y0, zoom);
			}
		}
	}
	
	public void startEquilibrating(){
		this.isRunning = true;
//		System.out.println("World.isRunning == true");
	}
	public void stopEquilibrating(){
		this.isRunning = false;
	}
	public void toggleEquilibrating(){
		this.isRunning = !this.isRunning;
	}
	public void rotateVehicles(){
		for(Vehicle v: this.vehicleList){
			double angle = 0.5;
			v.rotate(angle);
		}
	}
//	public void addSensors(){
//		for(Vehicle v: this.vehicleList){
//			v.addRandomSensor();
//		}
//	}

	// Randomly mutate the neural net of some vehicles.
	public void mutateNeuralNets(){
		
		for(Vehicle v: this.vehicleList){
			v.mutateNeuralNet();
		}
	}
	public void cloneVehicles(){
		// TODO Auto-generated method stub
		
	}
	public void killWeakest(){
		
		
	}
	
	public void toggleDisplayNets(){
		this.displayNets = !this.displayNets;
	}
	public void updateNodePositions(){
		for(Vehicle v: vehicleList){
			v.updateNodePositions();
		}
	}	
	
	
}
