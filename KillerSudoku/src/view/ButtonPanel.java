package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JButton solveB, chooseFileB, solveOneStepB;
	private JCheckBox help;
	public ButtonPanel(){
		super();
		JPanel panel = new JPanel(new FlowLayout());
		this.add(panel, BorderLayout.EAST);
		solveB = new JButton("Solve");
		chooseFileB = new JButton("Load");
		panel.add(solveB);
		panel.add(chooseFileB);
		solveOneStepB = new JButton("Solve One Step");
		panel.add(solveOneStepB);
		panel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
		help = new JCheckBox("Help");
		panel.add(help);
		
	}
	public JButton getSolveButton(){
		return solveB;
	}
	public JButton getChooseFileButton(){
		return chooseFileB;
	}
	public JButton getSolveOneButton(){
		return solveOneStepB;
	}
	public JCheckBox getHelpCheckBox(){
		return help;
	}

}
