package model;
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
		solver.solveCagesSpanningExtendedRegions();
		solver.solveSingleValueCells();
		solver.setPossibleValuesForCages();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		
//		solver.setPossibleValuesForUniqueCageSums(solver.getCagesWithUniqueSum());
//		solver.solveSingleValueCells();
//		solver.solveSingleValueCells();
		Map<List<Cage>, Region> map = solver.getRegionsContainingCages();//split this method into smaller chunks - returns "3 was removed from NONET 3 because cage X is contained"
		
		Set<List<Cage>> keySet = map.keySet();
		for(List<Cage> cageList : keySet){
			Region region = map.get(cageList);
			for(Cage cage : cageList){
				solver.useSumsAsConstraints(cage, region);
			}
		}
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.setPossibleValuesForCages();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		map=solver.getRegionsContainingCages();
		keySet = map.keySet();
		for(List<Cage> cageList : keySet){
			Region region = map.get(cageList);
			for(Cage cage : cageList){
				solver.useSumsAsConstraints(cage, region);
			}
		}
		
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.setPossibleValuesForCages();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.setPossibleCombinationsForCages();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.setPossibleCombinationsForCages();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		solver.solveSingleValueCells();
		System.out.println(solver.isSolved());
//		solver.setPossibleValuesForCagesOfLength2();
//		grid.printPossibleValues();
		
		grid.printGrid();
		System.out.println(solver.getSingleValueCellList());
//		System.out.println(cagesWithUniqueSum);
		AnswerParser.checkAnswer(grid, answer);
	}
}
