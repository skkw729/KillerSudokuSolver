package model;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolverTest {
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("example1Answer.txt");
		answer.printGrid();
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
//		solver.updateCagePossibleValues();
//		grid.printPossibleValues();
		
//		System.out.println(solver.getCagesSpanningRegion());
//		System.out.println(solver.getCagesWithUniqueSum());
		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		solver.solveSingleValueCells();
//		solver.solveSingleValueCells();
//		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		
		//List<Cage> uniqueCages = solver.getCagesWithUniqueSum();
		Map<List<Cage>, Region> map = solver.getRegionsContainingCages();
		Set<List<Cage>> keySet = map.keySet();
		for(List<Cage> c: keySet){
			Region r = map.get(c);
			for(Cage cage : c){
			solver.useSumsAsConstraints(cage, r);
			}
		}
		
		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
		solver.solveSingleValueCells();
		map = solver.getRegionsContainingCages();
		keySet = map.keySet();
		for(List<Cage> c: keySet){
			Region r = map.get(c);
			for(Cage cage : c){
			solver.useSumsAsConstraints(cage, r);
			}
		}
		
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
		System.out.println(AnswerParser.checkAnswer(grid, answer));
//		grid.printGrid();
	}
}
