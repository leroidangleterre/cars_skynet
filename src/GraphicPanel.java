

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GraphicPanel extends JPanel{

	private static final long serialVersionUID = 775535355282792517L;

	/* The origin of the represented environment will be visible
	   at the x0-th pixel column and at the y0-th pixel line,
	   starting from the lower-left corner.
	   The zoom value is the amount of pixels between that origin
	   and the point of coordinates (1, 0).  */
	private double x0, y0, zoom;
	
	private World world;
	private Timer timer;
	private boolean isRunning;

	private int date;
	

	
	/* Either the machines are superposed (each one is displayed in the same referential),
	   or they all have their own referential. */
	private boolean superposed = true;

	public GraphicPanel(Window w){
		this();
	}

	public GraphicPanel(){
		super();
		this.x0 = 149;
		this.y0 = 127;
		this.zoom = 46;
		ActionListener listener = new ActionListener(){
				public void actionPerformed(ActionEvent ev){
					date++;
//					System.out.println("Timer " + date);
					world.evolve();
					repaint();
				}
			};
		int period = 50;
		this.timer = new Timer(period, listener);
		this.isRunning = false;
		this.date = 0;
	}
	
	public GraphicPanel(World m){
		this();
		this.world = m;
	}

	public void eraseAll(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0,
			   (int)(this.getSize().getWidth()),
			   (int)(this.getSize().getHeight()));
	}
			   
	public void paintComponent(Graphics g){

//		System.out.println("GraphicPanel.repaint(); x0 = " + this.x0 + ", y0 = " + this.y0 + ", zoom = " + this.zoom);

		this.eraseAll(g);
		
		int panelHeight = (int)this.getSize().getHeight();

		this.world.paint(g, panelHeight,
				 this.x0, this.y0, this.zoom);
		
		this.drawAxis(g, panelHeight);
	}

	public void drawAxis(Graphics g, double panelHeight){
		
		double axisLength = 1; // Length in physical world.
		
		g.setColor(Color.BLACK);
		g.drawLine((int)(this.x0), (int)(panelHeight - this.y0),
			   (int)(this.x0 + axisLength * this.zoom), (int)(panelHeight - this.y0));
		g.drawLine((int)(this.x0), (int)(panelHeight - this.y0),
			   (int)(this.x0), (int)(panelHeight - (this.y0 + axisLength * this.zoom)));
	}

	public double getX0(){
		return this.x0;
	}
	public void setX0(double newX0){
		this.x0 = newX0;
		repaint();
	}
	public double getY0(){
		return this.x0;
	}
	public void setY0(double newY0){
		this.y0 = newY0;
		repaint();
	}
	public void translate(double dx, double dy){
		this.x0 += dx;
		this.y0 += dy;
		repaint();
//		System.out.println("GraphicPanel: x0 = " + this.x0 + ", y0 = " + this.y0+ ", zoom = " + this.zoom);
	}
	public double getZoom(){
		return this.zoom;
	}
	public void setZoom(double newZoom){
		this.zoom = newZoom;
		repaint();
	}
	public void multiplyZoom(double fact){
		this.zoom *= fact;
		repaint();
	}
	public void zoomOnMouse(double fact, int xMouse, int yMouse){

		double panelHeight = this.getSize().getHeight();
		
		if(fact < 1){
//			System.out.println("GraphicPanel.zoomOnMouse(" + fact + ", ...");
		}
		x0 = fact*(x0 - xMouse) + xMouse;
		y0 = (panelHeight-yMouse) + fact * (y0 - (panelHeight-yMouse));
		
		this.zoom *= fact;
		repaint();
	}
	
	public void resetView(){

		this.x0 = 0;
		this.y0 = 0;
		this.zoom = 1;
		
		repaint();
	}

	public void swipe(int dx, int dy){
		this.x0 += dx;
		this.y0 += dy;
		repaint();
	}
	public void zoomIn(){
		this.zoom *= 1.1;
//		System.out.println("zoom = " + this.zoom);
		repaint();
	}
	public void zoomOut(){
		this.zoom /= 1.1;
		repaint();
	}

	public void evolve(){
		this.world.evolveOneStep();
		this.repaint();
	}

	public void switchSuperposition(){
		this.superposed = ! this.superposed;
		this.repaint();
	}

	public void startEquilibrating(){
		this.timer.start();
		if(this.timer == null){
//			System.out.println("timer null");
		}
		this.world.startEquilibrating();
	}
	public void stopEquilibrating(){
		this.timer.stop();
		this.world.stopEquilibrating();
	}
	public void togglePlayPause(){
		if(this.isRunning){
			this.isRunning = false;
			this.stopEquilibrating();
		}
		else{
			this.isRunning = true;
			this.startEquilibrating();
		}
	}

	public void rotateVehicles(){
		world.rotateVehicles();
		this.repaint();
	}

//	public void addSensors(){
//		world.addSensors();
//		System.out.println("Adding sensors.");
//		this.repaint();
//	}
	
	

	public void cloneVehicles(){
		world.cloneVehicles();
	}

	public void killWeakest(){
		world.killWeakest();
	}

	public void toggleDisplayNets(){
		world.toggleDisplayNets();
		this.repaint();
	}

	public void updateNodePositions(){
		world.updateNodePositions();
	}
	
	
	// Randomly mutate the neural net of some vehicles.
	public void mutateNeuralNets(){
		world.mutateNeuralNets();
	}
	


}
