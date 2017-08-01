package test;
import model.*;
import view.*;

import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.*;
public class SudokuSolverTest implements Runnable {
	
		
	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<Cage> cages = null;
		try {
			cages = CageParser.parseCages("example2.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);		
		SudokuGridUI gridUI = new SudokuGridUI(grid);
		gridUI.getButtonPanel().getSolveButton().addActionListener(new SolveListener(gridUI, solver));
		gridUI.getButtonPanel().getChooseFileButton().addActionListener(new LoadFileListener(gridUI, solver));
		solver.setPossibleCombinationsForCages();
		gridUI.makeGrid();

	}
	public static void main(String[] args){
		SwingUtilities.invokeLater(new SudokuSolverTest());
	}
}
