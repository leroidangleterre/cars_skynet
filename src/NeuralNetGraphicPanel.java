
import java.awt.Color;
import java.awt.Graphics;

public class NeuralNetGraphicPanel extends GraphicPanel{

	private static final long serialVersionUID = 775535355282792517L;

	/*
	 * The origin of the represented environment will be visible
	 * at the x0-th pixel column and at the y0-th pixel line,
	 * starting from the lower-left corner.
	 * The zoom value is the amount of pixels between that origin
	 * and the point of coordinates (1, 0).
	 */

	private NeuralNet net;

	public NeuralNetGraphicPanel(Window w){
		this();
	}

	public NeuralNetGraphicPanel(){
		super();
		this.x0 = 149;
		this.y0 = 127;
		this.zoom = 46;
		// ActionListener listener = new ActionListener(){
		// public void actionPerformed(ActionEvent ev){
		// net.compute();
		// repaint();
		// }
		// };
		// int period = 50;
	}

	public NeuralNetGraphicPanel(NeuralNet n){
		this();
		this.net = n;
	}

	public void eraseAll(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int)(this.getSize().getWidth()), (int)(this.getSize().getHeight()));
	}

	public void paintComponent(Graphics g){

		System.out.println("Neural net repaint: x0 = " + x0 + ", y0 = " + y0);

		this.eraseAll(g);

		int panelHeight = (int)this.getSize().getHeight();

		this.net.paint(g, panelHeight, this.x0, this.y0, this.zoom);

		this.drawAxis(g, panelHeight);
	}

}
