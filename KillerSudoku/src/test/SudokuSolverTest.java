package test;
import model.*;
import view.*;

import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import controller.*;
public class SudokuSolverTest {
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example2.txt");
//		SudokuGrid answer = AnswerParser.parseAnswer("easy1Answer.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);		
		SudokuGridUI gridUI = new SudokuGridUI(grid);
		gridUI.getButtonPanel().getSolveButton().addActionListener(new SolveListener(gridUI, solver));
		gridUI.getButtonPanel().getChooseFileButton().addActionListener(new LoadFileListener(gridUI, solver));
		solver.setPossibleCombinationsForCages();
		gridUI.makeGrid();
//		System.out.println(AnswerParser.checkAnswer(grid, answer));
	}
}
