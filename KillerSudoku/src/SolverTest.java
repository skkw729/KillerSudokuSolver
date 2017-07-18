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
//		for(SudokuCell cell : grid.getCells()){
//			
//			System.out.println(cell.getLocation()+" "+cell.getPossibleValues());
//		}
		
//		System.out.println(solver.getCagesSpanningRegion());
//		System.out.println(solver.getCagesWithUniqueSum());
		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		
		List<Cage> uniqueCages = solver.getCagesWithUniqueSum();
		for(Cage c : uniqueCages){
			solver.useSumsAsConstraints(c);
			solver.solveSingleValueCells();
		}
		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		for(Cage c : uniqueCages){
			solver.useSumsAsConstraints(c);
			solver.solveSingleValueCells();
		}
//		for(SudokuCell cell : solver.solveCagesSpanningRegion(solver.getCagesSpanningRegion())){
//				solver.removeFromAllZones(cell);
//		}
		grid.printGrid();
//		System.out.println(grid.getCell(Location.getInstance(2, 9)).getPossibleValues());
//		solver.updateCagePossibleValues();
//		System.out.println(solver.getHiddenSingleColumn());
//		System.out.println(solver.getHiddenSingleRow());
//		System.out.println(solver.getHiddenSingleNonet());
//		System.out.println(solver.getSingleValueCellList());
//		System.out.println(solver.getCagesWithUniqueSum());
//		grid.printGrid();
	}
}
