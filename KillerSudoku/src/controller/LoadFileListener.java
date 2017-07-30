package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.CageParser;
import model.KillerSudokuGrid;
import model.KillerSudokuSolver;
import view.SudokuGridUI;

public class LoadFileListener implements ActionListener {
	private SudokuGridUI gridUI;
	private KillerSudokuSolver solver;
	public LoadFileListener(SudokuGridUI gridUI, KillerSudokuSolver solver){	
		this.gridUI = gridUI;
		this.solver = solver;
	}
	public void actionPerformed(ActionEvent e){
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Text files", "txt");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File file = chooser.getSelectedFile();
	    	try {
	    		KillerSudokuGrid grid = CageParser.getKillerSudokuGrid(file);
				gridUI.changeGrid(grid);
				solver.changeGrid(grid);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
}
