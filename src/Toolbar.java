
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/* This class describes a component that regroups buttons.
 */

public class Toolbar extends JPanel{

	private static final long serialVersionUID = 3431709111395343204L;

	//	private JButton buttonStart;
	private EvolvedButton buttonStart;
	private JButton buttonEvolve;
	private JButton buttonRotate;
//	private JButton buttonSensors;
	private JButton buttonNetwork;
	private JButton buttonClone;
	private JButton buttonKill;
	private JButton buttonDisplayNets;
	private JButton buttonUpdateNodes;
	private JButton buttonMutateNodes;
	
	private GraphicPanel panel;

	public Toolbar(GraphicPanel pan){

		this.panel = pan;
		
		this.buttonStart = new EvolvedButton("Play");
		this.buttonStart.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(buttonStart.getText().compareTo("Play") == 0){
						panel.startEquilibrating();
						buttonStart.setText("Pause");
					}
					else{
						panel.stopEquilibrating();
						buttonStart.setText("Play");
					}
				}});
		this.add(this.buttonStart);
		
		this.buttonEvolve = new EvolvedButton("evolve");
		this.buttonEvolve.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.evolve();
				}
			});
		this.add(this.buttonEvolve);
		
		this.buttonRotate = new EvolvedButton("rotate");
		this.buttonRotate.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.rotateVehicles();
				}
			});
		this.add(this.buttonRotate);
		
//		this.buttonSensors = new EvolvedButton("sensors");
//		this.buttonSensors.addActionListener(new ActionListener(){
//				public void actionPerformed(ActionEvent e){
//					panel.addSensors();
//				}
//			});
//		this.add(this.buttonSensors);
		
		this.buttonNetwork = new EvolvedButton("mutate networks");
		this.buttonNetwork.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.mutateNeuralNets();
				}
			});
		this.add(this.buttonNetwork);
		
		this.buttonClone = new EvolvedButton("clone");
		this.buttonClone.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.cloneVehicles();
				}
			});
		this.add(this.buttonClone);
		
		this.buttonKill = new EvolvedButton("kill");
		this.buttonKill.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.killWeakest();
				}
			});
		this.add(this.buttonKill);
		
		this.buttonDisplayNets = new EvolvedButton("show nets");
		this.buttonDisplayNets.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.toggleDisplayNets();
					if(buttonDisplayNets.getText().equals("show nets")){
						buttonDisplayNets.setText("hide nets");
					}
					else{
						buttonDisplayNets.setText("show nets");
					}
					panel.repaint();
				}
			});
		this.add(this.buttonDisplayNets);
		
		this.buttonUpdateNodes = new EvolvedButton("update network pos");
		this.buttonUpdateNodes.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.updateNodePositions();
					panel.repaint();
				}
			});
		this.add(this.buttonUpdateNodes);
		
		this.buttonMutateNodes = new EvolvedButton("mutate network");
		this.buttonMutateNodes.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.mutateNeuralNets();
					panel.repaint();
				}
			});
		this.add(this.buttonMutateNodes);
		
		
		
	}

	
	

	public void setKeyListener(KeyboardListener k){
		this.addKeyListener(k);
		this.buttonStart.addKeyListener(k);
		this.buttonEvolve.addKeyListener(k);
	}
}
