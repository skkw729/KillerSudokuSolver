import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillerSudokuSolver {
	private KillerSudokuGrid grid;
	private static final int SIZE = 9;
	private boolean solved;
	private SudokuSolver sudokuSolver;
	
	public KillerSudokuSolver(KillerSudokuGrid grid) {
		this.grid = grid;
		sudokuSolver = new SudokuSolver(grid);
	}
	
	public List<SudokuCell> getCagesSpanningRegion(){
		//rule of 45 - for each cage, check cell location
		//check row
		List<Cage> cages = grid.getCages();
		Map<Integer, List<Cage>> rowMap = new HashMap<>();
		Map<Integer, List<Cage>> columnMap = new HashMap<>();
		Map<Integer, List<Cage>> nonetMap = new HashMap<>();
		for(int i=1;i<=9;i++){
			for(Cage c : cages){
				List<Cage> cageList = new ArrayList<>();
				List<Location> locations = c.getCells();
				boolean same=true;
				for(Location l : locations){
					if(l.getRow()!=i) same = false;
				}
				if(same){
					cageList.add(c);
				}
			}
		}
		//check column
		//check nonet
		
		return new ArrayList<>();
	}

}
