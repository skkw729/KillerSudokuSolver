package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.KillerSudokuSolver;
import view.ButtonPanel;
import view.SudokuGridUI;

public class SolveListener implements ActionListener{
	private SudokuGridUI gridUI;
	private KillerSudokuSolver solver;
	private ButtonPanel bPanel;
	public SolveListener(SudokuGridUI gridUI, KillerSudokuSolver solver){
		this.gridUI = gridUI;
		this.solver = solver;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		solver.solveSingleValueCells();
		gridUI.makeGrid();
	}
}
