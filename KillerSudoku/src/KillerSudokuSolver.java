import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class KillerSudokuSolver {
	private KillerSudokuGrid grid;
	private static final int SIZE = 9;
	private boolean solved;
	private SudokuSolver sudokuSolver;

	public KillerSudokuSolver(KillerSudokuGrid grid) {
		this.grid = grid;
		sudokuSolver = new SudokuSolver(grid);
		solved = false;
	}

	public KillerSudokuGrid getGrid() {
		return grid;
	}

	public static int getSize() {
		return SIZE;
	}

	public boolean isSolved() {
		return solved;
	}

	public SudokuSolver getSudokuSolver() {
		return sudokuSolver;
	}

	public Map<List<Cage>, Type> getCagesSpanningRegion(){
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
				List<Location> locations = c.getCellLocations();
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
		Map<List<Cage>, Type> solvableCellMap = new HashMap<>();
		for(int i=1;i<=9;i++){
			List<Cage> cageListRow = rowMap.get(i);
			List<Cage> cageListColumn = columnMap.get(i);
			List<Cage> cageListNonet = nonetMap.get(i);
			int span = 0;
			for(Cage c : cageListRow){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListRow,Type.ROW);
			}
			span = 0;
			for(Cage c : cageListColumn){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListColumn,Type.COLUMN);
			}
			span = 0;
			for(Cage c : cageListNonet){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListNonet,Type.NONET);
			}
		}
		
		return solvableCellMap;
	}
	public boolean isUniqueSum(Cage cage){
		int digits = cage.getLength();
		int total = cage.getTotal();
		List<Combination> combinations = Sums.getSums(digits, total);
		return (combinations.size()==1);
	}
	
	public List<Cage> getCageUniqueSum(){
		List<Cage> cages = new ArrayList<>();
		for(Cage c : grid.getCages()){
			if(isUniqueSum(c)){
				cages.add(c);
			}
		}
		return cages;
	}
	
	public void updateCageUniqueSum(){
		//use knowledge of possible values to limit possible sums for each cage
		for(Cage c : grid.getCages()){
			if(!isUniqueSum(c)){
				//for cages with previously non-unique sums, limit their possible values if applicable
				Set<Integer> possibleNumbers = new TreeSet<>();//possible values for the cage
				List<SudokuCell> cells = grid.getCells(c);
				for(SudokuCell cell : cells){
					possibleNumbers.addAll(cell.getPossibleValues());
				}
				List<Combination> combinations = Sums.getSums(c.getLength(), c.getTotal(), possibleNumbers);
				if(combinations.size()==1){
					Combination uniqueCombination = combinations.get(0); 
					for(Location l : c.getCellLocations()){
						SudokuCell cell = grid.getCell(l);
						//compare the possible values for this cell with the set of possible values for the cage
						Set<Integer> cellPossibleValues = cell.getPossibleValues();//possible values for the cell
						for (Iterator<Integer> iterator = cellPossibleValues.iterator(); iterator.hasNext();) {
						    int value = iterator.next();
						    if (!uniqueCombination.contains(value)) {
						        // Remove the current element from the iterator and the set.
						        iterator.remove();
						    }
						}
						
					}
				}
			}
		}
	}
}


