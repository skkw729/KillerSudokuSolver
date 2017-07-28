package model;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParallelCageTest {
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("example1Answer.txt");
		
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
		solver.solveCagesSpanningRegion(solver.getCagesSpanningRegion());
		solver.setPossibleValuesForPartiallyFilledCages();
		solver.solveSingleValueCells();
		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		grid.printGrid();
		System.out.println(solver.getSingleValueCellList());
		AnswerParser.checkAnswer(grid, answer);
	}
}
