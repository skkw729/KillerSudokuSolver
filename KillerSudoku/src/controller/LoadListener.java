package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.KillerSudokuSolver;
import view.ButtonPanel;
import view.SudokuGridUI;

public class LoadListener implements ActionListener{
	private SudokuGridUI gridUI;
	private KillerSudokuSolver solver;
	public LoadListener(SudokuGridUI gridUI, KillerSudokuSolver solver){
		this.gridUI = gridUI;
		this.solver = solver;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		solver.solveCagesSpanningExtendRegions();
		gridUI.makeGrid();
	}
}
