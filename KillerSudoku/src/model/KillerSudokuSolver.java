package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class KillerSudokuSolver {
	private static final int REGION_TOTAL = 45;
	private KillerSudokuGrid grid;
	private static final int SIZE = 9;
	private boolean solved;
	private SudokuSolver sudokuSolver;

	public KillerSudokuSolver(KillerSudokuGrid grid) {
		this.grid = grid;
		sudokuSolver = new SudokuSolver(grid);
		solved = false;
	}
	public void changeGrid(KillerSudokuGrid grid){
		this.grid = grid;
		solved = false;
		sudokuSolver = new SudokuSolver(grid);
	}
	public KillerSudokuGrid getGrid() {
		return grid;
	}

	public static int getSize() {
		return SIZE;
	}

	public boolean isSolved() {
		if(!solved){	
			for(SudokuCell cell : grid.getCells()){
				if(!cell.isSolved()) return false;
			}
		}
		solved = true;
		return true;
	}

	public SudokuSolver getSudokuSolver() {
		return sudokuSolver;
	}
	public SudokuCell solveCagesSpanningExtendedRegions(){
		for(int i=1;i<=8;i++){
			SudokuCell cell = solveCagesSpanningRegions(i);
			if(cell !=null) return cell;
			
		}
		return null;
	}
	public SudokuCell solveCagesSpanningRegions(int numberOfRegions){
		for(int i=1;i<=(10-numberOfRegions);i++){

			//rows
			Set<Cage> cages = new HashSet<>();
			List<SudokuCell> row = grid.getRow(i);
			for(int j=1; j< numberOfRegions;j++){
				row.addAll(grid.getRow(j+i));
			}
			for(SudokuCell cell : row){
				cages.add(grid.getCage(cell.getLocation()));
			}
			int totalLength = 0;
			for(Cage c : cages){
				totalLength += c.getLength();
			}
			if(totalLength==(numberOfRegions*SIZE+1)){
				//find the extra cell
				SudokuCell extraCell = null; 
				int columnNumber=0;
				int rowNumber=0;
				int cageTotal=0;
				for(Cage c : cages){
					cageTotal += c.getTotal();
					for(Location location : c.getCellLocations()){
						columnNumber += location.getColumn();
						rowNumber += location.getRow();
					}
				}
				int value = cageTotal - numberOfRegions*REGION_TOTAL;
				int subtractFromRow=0;
				for(int r=0; r<numberOfRegions;r++){
					subtractFromRow += SIZE*(i+r);
				}
				rowNumber = rowNumber - subtractFromRow;
				columnNumber = columnNumber - numberOfRegions*REGION_TOTAL;

				extraCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				if(!extraCell.isSolved()){
					solveCell(extraCell, value);
					String rowsUsed="";
					for(int k=0; k<numberOfRegions;k++){
						rowsUsed+=(k+i);
					}
					System.out.println("Solved using extended rule of 45 on rows "+rowsUsed);
					return extraCell;
				}
			}
			//check if removing cages containing cells from other regions results in a solvable cell
			Set<Cage> cagesFromOtherRegion = new HashSet<>(); 
			for(Cage c : cages){
				for(Location l : c.getCellLocations()){
					boolean within = false;
					for(int k=0; k<numberOfRegions; k++){
						if(l.getRow()== (k+i)) within = true;
					}
					if(!within) {
						cagesFromOtherRegion.add(c);
					}
				}
			}
			for(Cage otherCage : cagesFromOtherRegion){
				totalLength -= otherCage.getLength();	
			}
			cages.removeAll(cagesFromOtherRegion);
			if(totalLength==(numberOfRegions*SIZE-1)){
				//find the missing cell
				SudokuCell missingCell = null;
				int columnNumber = 0;
				int rowNumber = 0;
				int cageTotal = 0;
				for(Cage c : cages){
					cageTotal += c.getTotal();
					for(Location location : c.getCellLocations()){
						columnNumber += location.getColumn();
						rowNumber += location.getRow();
					}
				}
				int value = numberOfRegions*REGION_TOTAL - cageTotal;
				int subtractFromRow=0;
				for(int r=0; r<numberOfRegions;r++){
					subtractFromRow += SIZE*(i+r);
				}
				String rowsUsed="";
				for(int k=0; k<numberOfRegions;k++){
					rowsUsed+=(k+i);
				}

				rowNumber = subtractFromRow - rowNumber;
				columnNumber = numberOfRegions*REGION_TOTAL - columnNumber;

				missingCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				if(!missingCell.isSolved()){
					solveCell(missingCell, value);
					System.out.println("Solved using extended rule of 45 on rows "+rowsUsed);
					return missingCell;

				}
			}
			//columns
			cages = new HashSet<>();
			List<SudokuCell> column = grid.getColumn(i);
			for(int j=0; j< numberOfRegions;j++){
				column.addAll(grid.getColumn(j+i));
			}
			for(SudokuCell cell : column){
				cages.add(grid.getCage(cell.getLocation()));
			}
			totalLength = 0;
			for(Cage c : cages){
				totalLength += c.getLength();
			}

			if(totalLength==(numberOfRegions*SIZE+1)){
				//find the extra cell
				SudokuCell extraCell = null; 
				int columnNumber=0;
				int rowNumber=0;
				int cageTotal=0;
				for(Cage c : cages){
					cageTotal += c.getTotal();
					for(Location location : c.getCellLocations()){
						columnNumber += location.getColumn();
						rowNumber += location.getRow();
					}
				}
				int value = cageTotal - numberOfRegions*REGION_TOTAL;
				int subtractFromColumn = 0;
				for(int k=0; k<numberOfRegions;k++){
					subtractFromColumn += SIZE*(k+i);
				}
				columnNumber = columnNumber - subtractFromColumn;
				rowNumber = rowNumber - numberOfRegions*REGION_TOTAL;

				extraCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				if(!extraCell.isSolved()){
					solveCell(extraCell, value);
					String columnsUsed="";
					for(int k=0; k<numberOfRegions;k++){
						columnsUsed += (k+i);

					}
					System.out.println("Solved using extended rule of 45 on columns "+columnsUsed);
					return extraCell;
				}

			}
			//check if removing cages containing cells from other regions results in a solvable cell
			cagesFromOtherRegion = new HashSet<>(); 
			for(Cage c : cages){
				for(Location l : c.getCellLocations()){
					boolean within = false;
					for(int k=0; k<numberOfRegions; k++){
						if(l.getColumn()== (k+i)) within = true;
					}
					if(!within) {
						cagesFromOtherRegion.add(c);
					}
				}
			}
			for(Cage otherCage : cagesFromOtherRegion){
				totalLength -= otherCage.getLength();	
			}
			cages.removeAll(cagesFromOtherRegion);
			if(totalLength==(numberOfRegions*SIZE-1)){
				//find the missing cell
				SudokuCell missingCell = null;
				int columnNumber = 0;
				int rowNumber = 0;
				int cageTotal = 0;
				for(Cage c : cages){
					cageTotal += c.getTotal();
					for(Location location : c.getCellLocations()){
						columnNumber += location.getColumn();
						rowNumber += location.getRow();
					}
				}
				int subtractFromColumn = 0;
				for(int k=0; k<numberOfRegions;k++){
					subtractFromColumn += SIZE*(k+i);
				}
				int value = numberOfRegions*REGION_TOTAL - cageTotal;
				columnNumber = subtractFromColumn - columnNumber;
				rowNumber = numberOfRegions*REGION_TOTAL - rowNumber;

				missingCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				if(!missingCell.isSolved()){
					solveCell(missingCell, value);
					String columnsUsed="";
					for(int k=0; k<numberOfRegions;k++){
						columnsUsed += (k+i);
					}

					System.out.println("Solved using extended rule of 45 on columns "+columnsUsed);
					return missingCell;
				}
			}
		}
		return null;
	}
	public boolean isUniqueSum(Cage cage){
		Set<Integer> cagePossibleNumbers = getPossibleValues(cage);
		//list the possible combinations of numbers that sum to the cage
		List<Combination> combinations = Sums.getSums(cage.getLength(), cage.getTotal(), cagePossibleNumbers);
		return (combinations.size()==1);
	}
	public List<Cage> getPartiallyFilledCages(){
		List<Cage> partiallyFilledCages = new ArrayList<>();
		for(Cage cage : grid.getCages()){
			if(!cage.isSolved() && cage.getSolvedLocations().size()>0) partiallyFilledCages.add(cage);
		}
		return partiallyFilledCages;
	}
	public List<Cage> getCagesWithUniqueSum(){
		List<Cage> cages = new ArrayList<>();
		for(Cage c : grid.getCages()){
			if(isUniqueSum(c) && !c.isSolved()) cages.add(c);
		}
		return cages;
	}
	public void setPossibleValuesForCages(){
		List<Cage> cages = grid.getCages();
		for(Cage cage : cages){
			if(!cage.isSolved()){
				Set<Integer> possibleValues = getPossibleValues(cage);
				Set<Integer> possibleSums = new TreeSet<>();
				List<Combination> combinations = Sums.getSums(cage.getLength(), cage.getTotal(), possibleValues);
				for(Combination combination : combinations){
					for(int i : combination.getNumbers()){
						possibleSums.add(i);
					}
				}
				for(SudokuCell cell : grid.getCells(cage)){
					cell.getPossibleValues().retainAll(possibleSums);
				}
			}
		}
	}
	public void setPossibleCombinationsForCages(){
		List<Cage> cages = grid.getCages();

		for(Cage cage : cages){
			Set<Combination> impossible = new HashSet<>();
			if(!cage.isSolved()){
				List<Combination> combinations = Sums.getSums(cage.getLength(),cage.getTotal(),getPossibleValues(cage));
				for(Combination combination : combinations){
					List<Integer> combinationNumberInOneCell = new ArrayList<>();
					for(int i : combination.getNumbers()){
						int numberOfCellsContaining = 0;
						for(SudokuCell cell : grid.getCells(cage)){
							if(cell.getPossibleValues().contains(i)) numberOfCellsContaining++;
						}
						if(numberOfCellsContaining == 1) combinationNumberInOneCell.add(i);
					}
					Set<Location> locations = new HashSet<>();
					for(int i : combinationNumberInOneCell){
						for(SudokuCell cell : grid.getCells(cage)){
							if(cell.getPossibleValues().contains(i)) locations.add(cell.getLocation());
						}
					}
					if(locations.size()!=combinationNumberInOneCell.size()) impossible.add(combination);
				}
				combinations.removeAll(impossible);
				Set<Integer> possibleSums = new TreeSet<>();
				for(Combination combination : combinations){
					for(int i : combination.getNumbers()){
						possibleSums.add(i);
					}
				}
				for(SudokuCell cell : grid.getCells(cage)){
					cell.getPossibleValues().retainAll(possibleSums);
				}
			}
		}
	}
	
	public List<SudokuCell> setPossibleValuesForUniqueCageSums(List<Cage> cages){
		List<SudokuCell> cells = new ArrayList<>();
		//sets possible values for cages with unique combinations
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
	public List<SudokuCell> setPossibleValuesForPartiallyFilledCages(){
		List<Cage> cages = getPartiallyFilledCages();
		List<SudokuCell> cells = new ArrayList<>();
		for(Cage cage : cages){
			int remaining = cage.getRemaining();
			int length = cage.getUnsolvedLocations().size();
			Set<Integer> possibleValues = new TreeSet<>();
			Set<Integer> possibleSums = new TreeSet<>();
			if(length == 1){
				SudokuCell cell = grid.getCell(cage.getUnsolvedLocations().get(0));
				Set<Integer> singlePossibleValue = new TreeSet<>();
				singlePossibleValue.add(remaining);
				cell.setPossibleValues(singlePossibleValue);
				cells.add(cell);
			}
			else{
				for(Location location : cage.getUnsolvedLocations()){
					possibleValues.addAll(grid.getCell(location).getPossibleValues());
				}
				List<Combination> combinations = Sums.getSums(length, remaining, possibleValues);
				for(Combination combination : combinations){
					for(int i : combination.getNumbers()){
						possibleSums.add(i);
					}
				}
				for(Location location : cage.getUnsolvedLocations()){
					grid.getCell(location).getPossibleValues().retainAll(possibleSums);
					cells.add(grid.getCell(location));
				}
			}
		}
		return cells;
	}
	/*
	 * used after solving a value in a cage on partially filled cages
	 */
	public Map<SudokuCell, Integer> updateCage(SudokuCell solvedCell){
		Map<SudokuCell, Integer> solvableCells = new HashMap<>();
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
	public Map<SudokuCell, Integer> updateCage(List<Cage> cages){
		Map<SudokuCell, Integer> solvableCells = new HashMap<>();
		for(Cage cage : cages){
			solvableCells.putAll(updateCage(cage));
		}
		return solvableCells;
	}
	/*
	 * used after solving a value in a cage on partially filled cages
	 */
	public Map<SudokuCell, Integer> updateCage(Cage cage){
		Map<SudokuCell, Integer> solvableCells = new HashMap<>();
		int remaining = cage.getTotal();

		List<SudokuCell> cells = grid.getCells(cage);
		List<SudokuCell> unsolvedCells = new ArrayList<>();
		List<SudokuCell> solvedCells = new ArrayList<>();

		for(SudokuCell cell : cells){
			if(cell.isSolved()){
				remaining -= cell.getValue();
				solvedCells.add(cell);
			}
			else if(!cell.isSolved()){
				unsolvedCells.add(cell);
			}
		}
		if(unsolvedCells.size()==1){
			solvableCells.put(unsolvedCells.get(0), remaining);
			return solvableCells;
		}
		for(SudokuCell solvedCell : solvedCells){ removeFromAllRegions(solvedCell);}
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
			solveCell(c,value);
			removeFromAllRegions(c);
			solvedCells.add(c);
		}
		return solvedCells;
	}
	/*
	 * cages completely contained within a region
	 */
	public Map<List<Cage>, Region> getRegionsContainingCages(){
		Map<List<Cage>, Region> regionMap = new HashMap<>();
		for(int i=1;i<=9;i++){
			List<Cage> cageRowList = new ArrayList<>();
			List<Cage> cageColumnList = new ArrayList<>();
			List<Cage> cageNonetList = new ArrayList<>();
			for(Cage c : grid.getCages()){
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
			if(!cageRowList.isEmpty()) regionMap.put(cageRowList, Region.getInstance(Type.ROW, i));
			if(!cageColumnList.isEmpty()) regionMap.put(cageColumnList, Region.getInstance(Type.COLUMN, i));
			if(!cageNonetList.isEmpty()) regionMap.put(cageNonetList, Region.getInstance(Type.NONET, i));
		}
		return regionMap;
	}

	/*
	 * removes a unique cage's combinations from the regions that it is contained within 
	 */
	public void useSumsAsConstraints(Cage cage, Region region){
		if(!isUniqueSum(cage)) return;
		if(cage.isSolved()) return;
		Set<Integer> cagePossibleNumbers = getPossibleValues(cage);
		List<Combination> combinations = Sums.getSums(cage.getLength(), cage.getTotal(), cagePossibleNumbers);
		Combination combination = combinations.get(0);
		Set<Integer> numbers = combination.getNumbers();
		sudokuSolver.removeFromRegion(numbers, grid.getCells(cage), region);

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
		if(cell.hasSinglePossibleValue()){
			int value = cell.getSinglePossibleValue();
			solveCell(cell, value);
			return true;
		}
		return false;
	}
	public void removeFromAllRegions(SudokuCell cell) {
		sudokuSolver.removeFromAllZones(cell);
	}
	public boolean isValidAtLocation(int value, Location location){
		return sudokuSolver.isValidAtLocation(value, location);
	}
	private void solveCell(SudokuCell cell, int value){
		Cage cage = grid.getCage(cell.getLocation());
		cell.setValue(value);
		cage.setSolvedLocation(cell.getLocation());
		cage.decreaseRemaining(value);
		if(cage.getUnsolvedLocations().isEmpty()) cage.setSolved();
		removeFromAllRegions(cell);

	}
}


