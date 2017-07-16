import java.io.FileNotFoundException;
import java.util.List;

public class SolverTest {
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
//		System.out.println(solver.getCagesSpanningRegion());
//		System.out.println(solver.getCageUniqueSum());
		}
}
