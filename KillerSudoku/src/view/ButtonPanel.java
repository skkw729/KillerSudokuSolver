package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
	private JButton solveB, chooseFileB;
	public ButtonPanel(){
		super();
		JPanel panel = new JPanel(new FlowLayout());
		this.add(panel, BorderLayout.EAST);
		solveB = new JButton("Solve");
		chooseFileB = new JButton("Load");
		panel.add(solveB);
		panel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
		panel.add(chooseFileB);
		
	}
	public JButton getSolveButton(){
		return solveB;
	}
	public JButton getChooseFileButton(){
		return chooseFileB;
	}

}
