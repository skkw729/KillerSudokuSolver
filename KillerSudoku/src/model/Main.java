package model;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {

	public static void main(String[] args) throws FileNotFoundException{
		
		List<Cage> cages = CageParser.parseCages("example1.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		grid.printGrid();
		grid.printNonets();

	}
}
