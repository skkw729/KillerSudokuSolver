package test;
import java.io.FileNotFoundException;
import java.util.List;

import model.Cage;
import model.CageParser;
import model.KillerSudokuGrid;

public class Main {

	public static void main(String[] args) throws FileNotFoundException{
		
		List<Cage> cages = CageParser.parseCages("example1.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		grid.printGrid();
		grid.printNonets();

	}
}
