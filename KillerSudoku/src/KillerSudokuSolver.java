import java.util.ArrayList;
import java.util.Arrays;
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
	public void solveCagesSpanningRegion(Map<List<Cage>, Region> cages){
		Set<List<Cage>> keySet = cages.keySet();
		for(List<Cage> list : keySet){
			//find the missing cell for the region
			Region region = cages.get(list);
			int regionNumber = region.getNumber();
			Type type = region.getRegion();
			int rowColumnTotal=0;
			int cageTotal = 0;
			for(Cage cage : list){
				cageTotal += cage.getTotal();
				for(Location location : cage.getCellLocations())
				{
					if(type.equals(Type.ROW)){
						rowColumnTotal += location.getColumn();
					}
					if(type.equals(Type.COLUMN)){
						rowColumnTotal += location.getRow();
					}
				}

			}
			int missingValue = 45 - rowColumnTotal;
			if(type.equals(Type.ROW)) //solve 
			if(type.equals(Type.COLUMN)) //solve
			if(type.equals(Type.NONET)); //solve
		}
	}

	public Map<List<Cage>, Region> getCagesSpanningRegion(){
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
		Map<List<Cage>, Region> solvableCellMap = new HashMap<>();
		for(int i=1;i<=9;i++){
			List<Cage> cageListRow = rowMap.get(i);
			List<Cage> cageListColumn = columnMap.get(i);
			List<Cage> cageListNonet = nonetMap.get(i);
			int span = 0;
			for(Cage c : cageListRow){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListRow,new Region(Type.ROW, i));
			}
			span = 0;
			for(Cage c : cageListColumn){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListColumn,new Region(Type.COLUMN, i));
			}
			span = 0;
			for(Cage c : cageListNonet){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListNonet,new Region(Type.NONET, i));
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

	public List<Cage> getCagesWithUniqueSum(){
		List<Cage> cages = new ArrayList<>();
		for(Cage c : grid.getCages()){
			if(isUniqueSum(c)){
				cages.add(c);
			}
		}
		return cages;
	}

	public void updateCagePossibleValues(){
		//use knowledge of sum combinations to limit possiblevalues for each cage
		for(Cage c : grid.getCages()){
			Set<Integer> cagePossibleNumbers = new TreeSet<>();//possible values for the cage
			List<SudokuCell> cells = grid.getCells(c);
			//find all possible values for the cage
			for(SudokuCell cell : cells){
				cagePossibleNumbers.addAll(cell.getPossibleValues());
			}
			//calculate the possible numbers that sum to the cage
			List<Combination> combinations = Sums.getSums(c.getLength(), c.getTotal(), cagePossibleNumbers);
			Set<Integer> combinationNumbers = new TreeSet<>();
			//list all numbers used in combinations 
			for(Combination combination : combinations){
				if(combinationNumbers.size()==SIZE) break;
				for(int number : combination.getNumbers()){
					if(!combinationNumbers.contains(number)) combinationNumbers.add(number);
				}
			}
			//compare the cell's possible values with the set of combination numbers
			for(Location l : c.getCellLocations()){
				SudokuCell cell = grid.getCell(l);
				//compare the possible values for this cell with the set of possible values for the cage
				Set<Integer> cellPossibleValues = cell.getPossibleValues();//possible values for the cell
				for (Iterator<Integer> iterator = cellPossibleValues.iterator(); iterator.hasNext();) {
					int value = iterator.next();
					if (!combinationNumbers.contains(value)) {
						// Remove the current element from the iterator and the set.
						iterator.remove();
					}
				}

			}
		}
	}
}


