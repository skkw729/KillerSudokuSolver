import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolverTest2 {
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("example1Answer.txt");
		
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
		solver.solveCagesSpanningRegion(solver.getCagesSpanningRegion());
		//System.out.println(solver.getSingleValueCellList());
		Map<SudokuCell,Integer> update = solver.updateCage(solver.getPartiallyFilledCages());
		solver.solveUpdatedCage(update);
		List<Cage> cagesWithUniqueSum = solver.getCagesWithUniqueSum();
		solver.setPossibleValuesForUniqueCageSums(cagesWithUniqueSum);
		grid.printGrid();
		System.out.println(solver.getSingleValueCellList());
		System.out.println(cagesWithUniqueSum);
		AnswerParser.checkAnswer(grid, answer);
	}
}
