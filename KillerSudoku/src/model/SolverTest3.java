package model;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolverTest3 {
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("easy1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("easy1Answer.txt");
		
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
		//solver.solveCagesSpanningExtendedRegion();
		solver.solveCagesSpanningExtendRegions();
		grid.printPossibleValues();
		grid.printGrid();
		System.out.println(solver.getSingleValueCellList());
//		System.out.println(cagesWithUniqueSum);
		AnswerParser.checkAnswer(grid, answer);
	}
}
