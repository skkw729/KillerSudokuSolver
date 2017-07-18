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
	public List<SudokuCell> solveCagesSpanningRegion(Map<List<Cage>, Region> cages){
		Set<List<Cage>> cageList = cages.keySet();
		List<SudokuCell> solvedCells = new ArrayList<>();
		for(List<Cage> list : cageList){
			//find the missing cell for the region
			Region region = cages.get(list);
			int regionNumber = region.getNumber();
			Type type = region.getRegion();
			int rowColumnTotal=0;
			int cageTotal = 0;
			SudokuCell missingCell=null;
			for(Cage cage : list){
				cageTotal += cage.getTotal();
				for(Location location : cage.getCellLocations())
				{
					if(type.equals(Type.ROW)){
						rowColumnTotal += location.getColumn();
					}
					else if(type.equals(Type.COLUMN)){
						rowColumnTotal += location.getRow();
					}
					else if(type.equals(Type.NONET)){
						List<SudokuCell> nonetCells = Arrays.asList(grid.getNonet(regionNumber));
						SudokuCell cellAtLocation = grid.getCell(location);
						if(!nonetCells.contains(cellAtLocation)){
							missingCell = cellAtLocation;
						}
					}
				}
			}
			int cellValue = 45 - cageTotal;
			int missingValue = 45 - rowColumnTotal;
			if(type.equals(Type.ROW)){
				missingCell = grid.getCell(Location.getInstance(regionNumber, missingValue));
			}
			else if(type.equals(Type.COLUMN)){
				missingCell = grid.getCell(Location.getInstance(missingValue, regionNumber));
			}
			missingCell.setValue(cellValue);
			solvedCells.add(missingCell);
		}
		return solvedCells;
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
		Set<Integer> cagePossibleNumbers = getPossibleValues(cage);
		//list the possible combinations of numbers that sum to the cage
		List<Combination> combinations = Sums.getSums(cage.getLength(), cage.getTotal(), cagePossibleNumbers);
		return (combinations.size()==1);
	}

	public List<Cage> getCagesWithUniqueSum(){
		List<Cage> cages = new ArrayList<>();
		for(Cage c : grid.getCages()){
			if(isUniqueSum(c)) cages.add(c);
		}
		//remove solved cages
		List<Cage> solvedCages = new ArrayList<>();
		for(Cage cage : cages){
			boolean solved = true;
			for(SudokuCell cell : grid.getCells(cage)){
				if(!cell.isSolved()) solved = false;
			}
			if(solved) solvedCages.add(cage); 
		}
		solvedCages.removeAll(solvedCages);
		return cages;
	}
	public List<SudokuCell> setPossibleValuesForUniqueCageSums(List<Cage> cages){
		List<SudokuCell> cells = new ArrayList<>();
		for(Cage cage : cages){
			Set<Integer> possibleValues = getPossibleValues(cage);
			Set<Integer> possibleSums = new TreeSet<>();
			List<Combination> combinations = Sums.getSums(cage.getLength(), cage.getTotal(), possibleValues);
			for(int i : combinations.get(0).getNumbers()){
				possibleSums.add(i);
			}
			for(SudokuCell cell : grid.getCells(cage)){
				cell.getPossibleValues().retainAll(possibleSums);
				cells.add(cell);
			}
		}
		return cells;
	}
	/*
	 * used after solving a value in a cage
	 */
	public Map<SudokuCell, Integer> updateCage(SudokuCell solvedCell){
		Map<SudokuCell, Integer> solvableCells = new HashMap<>();
		int value = solvedCell.getValue();
		Cage cage = grid.getCage(solvedCell.getLocation());
		int remaining = cage.getTotal();
		removeFromAllRegions(solvedCell);
		List<SudokuCell> cells = grid.getCells(cage);
		List<SudokuCell> unsolvedCells = new ArrayList<>();

		for(SudokuCell cell : cells){
			if(cell.isSolved()){
				remaining -= cell.getValue();
			}
			else if(!cell.isSolved()){
				unsolvedCells.add(cell);
			}
		}
		if(unsolvedCells.size()==1){
			solvableCells.put(unsolvedCells.get(0), remaining);
			return solvableCells;
		}
		List<Combination> combinations = Sums.getSums(unsolvedCells.size(), remaining, getPossibleValues(unsolvedCells));
		//check if the remaining unsolved cells has a unique combination
		if(combinations.size()==1){
			Set<Integer> possibleValues = combinations.get(0).getNumbers();
			for(SudokuCell cell : unsolvedCells){
				cell.getPossibleValues().retainAll(possibleValues);
				if(cell.hasSinglePossibleValue()) solvableCells.put(cell, cell.getSinglePossibleValue());
			}
		}

		return solvableCells;
	}
	public List<SudokuCell> solveUpdatedCage(Map<SudokuCell, Integer> cageUpdate){
		Set<SudokuCell> solvableCells = cageUpdate.keySet();
		List<SudokuCell> solvedCells = new ArrayList<>();
		for(SudokuCell c : solvableCells){
			int value = cageUpdate.get(c);
			c.setValue(value);
			removeFromAllRegions(c);
			solvedCells.add(c);
		}
		return solvedCells;
	}
	/*
	 * updates possibleValue for cells in cages where combinations of sums can limit it
	 */
	//	public void updateCagePossibleValues(){
	//		//use knowledge of sum combinations to limit possiblevalues for each cage
	//		for(Cage c : grid.getCages()){
	//			Set<Integer> cagePossibleNumbers = getPossibleValues(c);
	//			//calculate the possible numbers that sum to the cage
	//			List<Combination> combinations = Sums.getSums(c.getLength(), c.getTotal(), cagePossibleNumbers);
	//			Set<Integer> combinationNumbers = new TreeSet<>();
	//			//list all numbers used in combinations 
	//			for(Combination combination : combinations){
	//				if(combinationNumbers.size()!=SIZE){
	//					for(int number : combination.getNumbers()){
	//						if(!combinationNumbers.contains(number)) combinationNumbers.add(number);
	//					}
	//				}
	//			}
	//			//compare the cell's possible values with the set of combination numbers
	//			for(Location l : c.getCellLocations()){
	//				SudokuCell cell = grid.getCell(l);
	//				//compare the possible values for this cell with the set of possible values for the cage
	//				if(!cell.isSolved()){
	//					cell.getPossibleValues().retainAll(cagePossibleNumbers);
	//				}
	//
	//			}
	//		}
	//	}
	/*
	 * use known unique sums contained in a region to limit possible values within
	 */
	public void useSumsAsConstraints(Cage c){
		if(!isUniqueSum(c)) return;
		Combination combination = Sums.getSums(c.getLength(), c.getTotal()).get(0);
		boolean sameRow = true;
		boolean sameColumn = true;
		boolean sameNonet = true;
		int row = c.getCellLocations().get(0).getRow();
		int col = c.getCellLocations().get(0).getColumn();
		int nonet = c.getCellLocations().get(0).getNonet();
		for(Location l : c.getCellLocations()){
			if(l.getRow()!=row){ sameRow=false;}
			if(l.getColumn()!=col){ sameColumn=false;}	
			if(l.getNonet()!=nonet){ sameNonet=false;}
		}
		if(sameRow) sudokuSolver.removeFromRegion(combination.getNumbers(), grid.getCells(c), Type.ROW);
		if(sameColumn) sudokuSolver.removeFromRegion(combination.getNumbers(), grid.getCells(c), Type.COLUMN);
		if(sameNonet) sudokuSolver.removeFromRegion(combination.getNumbers(), grid.getCells(c), Type.NONET);
	}
	public void solveSingleValueCells(){
		for(SudokuCell cell :getSingleValueCellList()){
			solveSingleValueCell(cell);
		}
	}
	public Set<Integer> getPossibleValues(Cage cage){
		Set<Integer> possibleValues = new TreeSet<>();
		for(SudokuCell cell : grid.getCells(cage)){
			possibleValues.addAll(cell.getPossibleValues());
		}
		return possibleValues;
	}
	public Set<Integer> getPossibleValues(List<SudokuCell> cells){
		Set<Integer> possibleValues = new TreeSet<>();
		for(SudokuCell cell : cells){
			possibleValues.addAll(cell.getPossibleValues());
		}
		return possibleValues;
	}
	public List<SudokuCell> getSingleValueCellList(){
		return sudokuSolver.getSingleValueCellList();
	}
	public boolean solveSingleValueCell(SudokuCell cell){
		return sudokuSolver.solveSingleValueCell(cell);
	}
	public void removeFromAllRegions(SudokuCell cell) {
		sudokuSolver.removeFromAllZones(cell);
	}
	public boolean isValidAtLocation(int value, Location location){
		return sudokuSolver.isValidAtLocation(value, location);
	}
	public SudokuCell getHiddenSingleRow(){
		return sudokuSolver.getHiddenSingleRow();
	}
	public SudokuCell getHiddenSingleColumn(){
		return sudokuSolver.getHiddenSingleColumn();
	}
	public SudokuCell getHiddenSingleNonet(){
		return sudokuSolver.getHiddenSingleNonet();
	}
	public void solveHiddenSingle(SudokuCell cell){
		sudokuSolver.solveHiddenSingle(cell);
	}
}


