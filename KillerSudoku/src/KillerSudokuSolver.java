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
		//check row, column and nonet
		List<Cage> cages = grid.getCages();
		Map<Integer, List<Cage>> rowMap = new HashMap<>();
		Map<Integer, List<Cage>> columnMap = new HashMap<>();
		Map<Integer, List<Cage>> nonetMap = new HashMap<>();
		for(int i=1;i<=9;i++){
			List<Cage> cageRowList = new ArrayList<>();
			List<Cage> cageColumnList = new ArrayList<>();
			List<Cage> cageNonetList = new ArrayList<>();
			for(Cage c : cages){
				List<Location> locations = c.getCells();
				boolean sameRow = true;
				boolean sameColumn = true;
				boolean sameNonet = true;
				for(Location l : locations){//check if all cells within a cage are in the same location
					if(l.getRow()!=i) sameRow = false;
					if(l.getColumn()!=i) sameColumn = false;
					if(l.getNonet()!=i) sameNonet = false;
				}
				if(sameRow){
					cageRowList.add(c);
				}
				if(sameColumn){
					cageColumnList.add(c);
				}
				if(sameNonet){
					cageNonetList.add(c);
				}
			}
			rowMap.put(i, cageRowList);
			columnMap.put(i, cageColumnList);
			nonetMap.put(i, cageNonetList);
		}
		//check length of cages in map
		for(int i=1;i<=9;i++){
		List<Cage> cageList = rowMap.get(i);
		int span = 0;
		for(Cage c : cageList){
			span += c.getLength();
		}
		if(span == SIZE-1){}
		}
		
		return new ArrayList<>();
	}
}
		

