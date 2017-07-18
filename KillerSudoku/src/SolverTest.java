import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class SolverTest {
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
//		solver.updateCagePossibleValues();
		List<SudokuCell> cells = solver.solveCagesSpanningRegion(solver.getCagesSpanningRegion());
		for(SudokuCell cell : cells){
			Map<SudokuCell, Integer> cageUpdates = solver.updateCage(cell);
			solver.solveUpdatedCage(cageUpdates);
		}
		
//		grid.printPossibleValues();
		
//		System.out.println(solver.getCagesSpanningRegion());
//		System.out.println(solver.getCagesWithUniqueSum());
		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		solver.solveSingleValueCells();
//		solver.solveSingleValueCells();
//		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		
		List<Cage> uniqueCages = solver.getCagesWithUniqueSum();
		Cage cage = uniqueCages.get(5);
		solver.useSumsAsConstraints(cage);
		grid.printPossibleValues();
		solver.solveSingleValueCells();
//		List<Cage> uniqueCages = solver.getCagesWithUniqueSum();
//		for(Cage c : uniqueCages){
//			solver.useSumsAsConstraints(c);
//		}
//		solver.solveSingleValueCells();
//		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
//		for(Cage c : uniqueCages){
//			solver.useSumsAsConstraints(c);
//			solver.solveSingleValueCells();
//		}
//		for(Cage cage : solver.getPartiallyFilledCages()){
//			for(SudokuCell cell : grid.getCells(cage)){
//				if(cell.isSolved()) solver.solveUpdatedCage(solver.updateCage(cell));
//			}
//			
//		}
//		
//		System.out.println(solver.getPartiallyFilledCages());
		grid.printGrid();
//		grid.printGrid();
	}
}
