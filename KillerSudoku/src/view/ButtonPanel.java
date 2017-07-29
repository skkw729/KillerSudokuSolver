package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	private JButton solveB, loadB;
	public ButtonPanel(){
		super();
		this.setPreferredSize(new Dimension(200,10));
		JPanel panel = new JPanel(new GridBagLayout());
		this.add(panel, BorderLayout.EAST);
		solveB = new JButton("Solve");
//		solveB.addActionListener(this);
		loadB = new JButton("Load");
		panel.add(solveB);
		panel.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0));
		panel.add(loadB);
//		loadB.addActionListener(this);
	}
	public JButton getSolveButton(){
		return solveB;
	}
	public JButton getLoadButton(){
		return loadB;
	}
//	public void actionPerformed(ActionEvent e){
//		System.out.println("solve");
//	}
}
