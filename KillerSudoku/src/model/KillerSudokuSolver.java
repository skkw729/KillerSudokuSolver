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
	private static List<Pair<Cage, Region>> CHECKED_CAGE_REGIONS = new ArrayList<>();
	private static Map<Integer, List<Integer>> ADJACENT_NONETS = new HashMap<>();
	private static List<Triple<SudokuCell, SudokuCell,Integer>> CHECKED_SUM_CONSTRAINT = new ArrayList<>();
	private static List<Triple<SudokuCell, SudokuCell, Integer>> CHECK_SUBSTRACT_CONSTRAINT = new ArrayList<>();

	public KillerSudokuSolver(KillerSudokuGrid grid) {
		this.grid = grid;
		assignAdjacentNonets();
		solved = false;
	}
	public void changeGrid(KillerSudokuGrid grid){
		this.grid = grid;
		solved = false;
		CHECKED_CAGE_REGIONS = new ArrayList<>();
	}
	public KillerSudokuGrid getGrid() {
		return grid;
	}

	public static int getSize() {
		return SIZE;
	}
	public void assignAdjacentNonets(){
		//only for SIZE = 9
		for(int i=1;i<=9;i++){
			List<Integer> adjNonets = new ArrayList<>();
			switch(i){
			case 1:	adjNonets.add(2);
			adjNonets.add(4);

			case 2: adjNonets.add(1);
			adjNonets.add(3);
			adjNonets.add(5);

			case 3: adjNonets.add(2);
			adjNonets.add(6);

			case 4: adjNonets.add(1);
			adjNonets.add(5);
			adjNonets.add(7);

			case 5: adjNonets.add(2);
			adjNonets.add(4);
			adjNonets.add(6);
			adjNonets.add(8);

			case 6: adjNonets.add(3);
			adjNonets.add(5);
			adjNonets.add(9);

			case 7: adjNonets.add(4);
			adjNonets.add(8);

			case 8: adjNonets.add(5);
			adjNonets.add(7);
			adjNonets.add(9);

			case 9: adjNonets.add(6);
			adjNonets.add(8);
			}
			ADJACENT_NONETS.put(i,adjNonets);
		}
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
	public Reason solveAllAdjacentNonets(){
		for(int i=0;i<SIZE;i++){
			Reason r = solveAdjacentNonets(i);
			if(r!=null) return r;
		}
		return null;
	}
	public Reason solveAdjacentNonets(int numberOfNonets){

		for(int i=1;i<=SIZE;i++){
			Set<Cage> cages = new HashSet<>();
			List<SudokuCell> nonet = grid.getNonet(i);
			List<Integer> adjNonets = ADJACENT_NONETS.get(i);
			Set<Integer> nonetsUsed = new TreeSet<>();
			nonetsUsed.add(i);
			for(int j=0;j<numberOfNonets;j++){
				if(j<adjNonets.size()){
					nonet.addAll(grid.getNonet(adjNonets.get(j)));
					nonetsUsed.add(adjNonets.get(j));
				}
			}
			int totalLength = 0;
			for(SudokuCell cell : nonet){
				cages.add(grid.getCage(cell.getLocation()));
			}
			for(Cage c : cages){
				totalLength +=c.getLength();
			}
			if(totalLength==nonetsUsed.size()*SIZE + 2){
				//find both extra cells
				SudokuCell cell1 = null;
				SudokuCell cell2 = null;
				int cageTotal = 0;
				for(Cage c : cages){
					cageTotal += c.getTotal();
					for(Location l : c.getCellLocations()){
						if(!nonetsUsed.contains(l.getNonet())){
							if(cell1==null && cell2==null){
								cell1=grid.getCell(l);
							}
							else{
								cell2=grid.getCell(l);
							}
						}
					}
				}
				int value = cageTotal - nonetsUsed.size()*REGION_TOTAL;
				//both cells must sum to this value
				Triple<SudokuCell, SudokuCell, Integer> t = new Triple<SudokuCell, SudokuCell, Integer>(cell1, cell2, value);
				if(!CHECKED_SUM_CONSTRAINT.contains(t)){
					//check if constraint exists
					if(!cell1.isSolved() || !cell1.isSolved()){
						while(true){
							Set<Integer> impossibleValues = new TreeSet<>();
							for(int cell1Value : cell1.getPossibleValues()){
								boolean impossible = true;
								for(int cell2Value : cell2.getPossibleValues()){
									if(cell1Value+cell2Value==value) impossible = false;
								}
								if(impossible) impossibleValues.add(cell1Value);
							}
							cell1.getPossibleValues().removeAll(impossibleValues);
							impossibleValues.clear();
							for(int cell2Value : cell2.getPossibleValues()){
								boolean impossible = true;
								for(int cell1Value : cell1.getPossibleValues()){
									if(cell1Value+cell2Value==value) impossible = false;
								}
								if(impossible) impossibleValues.add(cell2Value);
							}
							cell2.getPossibleValues().removeAll(impossibleValues);
							if(impossibleValues.isEmpty()) break;
						}
						List<SudokuCell> cells = new ArrayList<>();
						cells.add(cell1);
						cells.add(cell2);
						String s = cell1.getLocation() + " and " + cell2.getLocation() + " must sum to " + value +"\nThis is because the cages within nonet(s) "+nonetsUsed+" must sum to "
								+nonetsUsed.size()*REGION_TOTAL+"\nThe cages in nonet(s) "+nonetsUsed+" sum to "+cageTotal+"\nPossible values of these cells have been adjusted to fit this constraint";
						Reason r = new Reason(s,cells);
						CHECKED_SUM_CONSTRAINT.add(t);
						return r;
					}
				}
			}
			if(totalLength == nonetsUsed.size()*SIZE + 1){
				//find extra cell
				SudokuCell cell = null;
				int cageTotal=0;
				for(Cage c : cages){
					cageTotal+=c.getTotal();
					for(Location l : c.getCellLocations()){
						if(!nonetsUsed.contains(l.getNonet()))cell = grid.getCell(l);
					}
				}
				int value = cageTotal - REGION_TOTAL*nonetsUsed.size();
				if(!cell.isSolved()){
					solveCell(cell, value);
					String s = cell.getLocation()+" has been solved using the extended rule of 45 on the nonet(s) "+nonetsUsed+
							"\nSince each nonet must total 45, the cages contained in "+nonetsUsed.size()+" nonet(s) must sum to "+ nonetsUsed.size()*REGION_TOTAL
							+"\nThe cages in nonet(s) "+nonetsUsed+" sum to "+cageTotal;
					Reason r = new Reason(s,cell);
					return r;
				}
			}
			Set<Cage> cagesFromOtherNonet = new HashSet<>();
			for(Cage c : cages){
				for(Location l : c.getCellLocations()){
					if(!nonetsUsed.contains(l.getNonet())) cagesFromOtherNonet.add(c);
				}
			}
			for(Cage cage : cagesFromOtherNonet){
				cages.remove(cage);
				totalLength -= cage.getLength();
			}
			if(totalLength==nonetsUsed.size()*SIZE-1){
				int cageTotal = 0;
				SudokuCell cell = null;
				List<Location> locationList = new ArrayList<>();
				for(Cage c : cages){
					cageTotal += c.getTotal();
					for(Location l : c.getCellLocations()){
						locationList.add(l);
					}
				}
				for(SudokuCell cell2 : nonet){
					if(!locationList.contains(cell2.getLocation())){
						cell = cell2;
					}
				}
				int value = REGION_TOTAL*nonetsUsed.size() - cageTotal;		
				if(!cell.isSolved()){
					solveCell(cell, value);
					String s =  cell.getLocation()+" has been solved using the extended rule of 45 on the nonet(s) "+nonetsUsed+
							"\nSince each nonet must total 45, the cages contained in "+nonetsUsed.size()+" nonet(s) must sum to "+ nonetsUsed.size()*REGION_TOTAL
							+"\nThe cages in nonet(s) "+nonetsUsed+" sum to "+cageTotal;
					Reason r = new Reason(s, cell);
					return r;
				}
			}

		}
		return null;

	}
	public Reason solveCagesSpanningExtendedRegions(){
		for(int i=1;i<=8;i++){
			Reason reason = solveCagesSpanningRegions(i);
			if(reason !=null) return reason;

		}
		return null;
	}
	public Reason solveCagesSpanningRegions(int numberOfRegions){
		for(int i=1;i<=(10-numberOfRegions);i++){

			//rows
			Set<Cage> cages = new HashSet<>();
			Set<Integer> rowsUsed = new HashSet<>();
			rowsUsed.add(i);
			List<SudokuCell> row = grid.getRow(i);
			for(int j=1; j< numberOfRegions;j++){
				row.addAll(grid.getRow(j+i));
				rowsUsed.add(j+1);
			}
			for(SudokuCell cell : row){
				cages.add(grid.getCage(cell.getLocation()));
			}
			int totalLength = 0;
			for(Cage c : cages){
				totalLength += c.getLength();
			}
//			if(totalLength==numberOfRegions*SIZE+2){
//				//find both extra cells
//				SudokuCell cell1 = null;
//				SudokuCell cell2 = null;
//				int cageTotal = 0;
//				for(Cage c : cages){
//					cageTotal += c.getTotal();
//					for(Location l : c.getCellLocations()){
//						if(!rowsUsed.contains(l.getRow())){
//							if(cell1==null && cell2==null){
//								cell1=grid.getCell(l);
//							}
//							else{
//								cell2=grid.getCell(l);
//							}
//						}
//					}
//				}
//				int value = cageTotal - numberOfRegions*REGION_TOTAL;
//				//both cells must sum to this value
//				Triple<SudokuCell, SudokuCell, Integer> t = new Triple<SudokuCell, SudokuCell, Integer>(cell1, cell2, value);
//				if(!CHECKED_SUM_CONSTRAINT.contains(t)){
//					//check if constraint exists
//					if(!cell1.isSolved() || !cell1.isSolved()){
//						while(true){
//							Set<Integer> impossibleValues = new TreeSet<>();
//							for(int cell1Value : cell1.getPossibleValues()){
//								boolean impossible = true;
//								for(int cell2Value : cell2.getPossibleValues()){
//									if(cell1Value+cell2Value==value) impossible = false;
//								}
//								if(impossible) impossibleValues.add(cell1Value);
//							}
//							cell1.getPossibleValues().removeAll(impossibleValues);
//							impossibleValues.clear();
//							for(int cell2Value : cell2.getPossibleValues()){
//								boolean impossible = true;
//								for(int cell1Value : cell1.getPossibleValues()){
//									if(cell1Value+cell2Value==value) impossible = false;
//								}
//								if(impossible) impossibleValues.add(cell2Value);
//							}
//							cell2.getPossibleValues().removeAll(impossibleValues);
//							if(impossibleValues.isEmpty()) break;
//						}
//						List<SudokuCell> cells = new ArrayList<>();
//						cells.add(cell1);
//						cells.add(cell2);
//						String s = cell1.getLocation() + " and " + cell2.getLocation() + " must sum to " + value +"\nThis is because the cages within row(s) "+rowsUsed+" must sum to "
//								+rowsUsed.size()*REGION_TOTAL+"\nThe cages in row(s) "+rowsUsed+" sum to "+cageTotal+"\nPossible values of these cells have been adjusted to fit this constraint";
//						Reason r = new Reason(s,cells);
//						CHECKED_SUM_CONSTRAINT.add(t);
//						return r;
//					}
//				}
//			}
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
					String message = extraCell.getLocation()+" has been solved using the extended rule of 45 on the row(s) "+rowsUsed+
							"\nSince each row must total 45, the cages contained in "+numberOfRegions+" row(s) must sum to "+ numberOfRegions*REGION_TOTAL
							+"\nThe cages in row(s) "+rowsUsed+" sum to "+cageTotal;

					Reason reason = new Reason(message, extraCell);
					return reason;
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
				rowNumber = subtractFromRow - rowNumber;
				columnNumber = numberOfRegions*REGION_TOTAL - columnNumber;

				missingCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				if(!missingCell.isSolved()){
					solveCell(missingCell, value);
					String message = missingCell.getLocation()+" has been solved using the extended rule of 45 on the row(s) "+rowsUsed+
							"\nSince each row must total 45, the cages contained in "+numberOfRegions+" row(s) must sum to "+ numberOfRegions*REGION_TOTAL
							+"\nThe cages in row(s) "+rowsUsed+" sum to "+cageTotal;

					Reason reason = new Reason(message, missingCell);
					return reason;

				}
			}
			//columns
			cages = new HashSet<>();
			List<SudokuCell> column = grid.getColumn(i);
			Set<Integer> columnsUsed = new HashSet<>();
			columnsUsed.add(i);
			for(int j=0; j< numberOfRegions;j++){
				column.addAll(grid.getColumn(j+i));
				columnsUsed.add(j+i);
			}
			for(SudokuCell cell : column){
				cages.add(grid.getCage(cell.getLocation()));
			}
			totalLength = 0;
			for(Cage c : cages){
				totalLength += c.getLength();
			}
//			if(totalLength==(numberOfRegions*SIZE+2)){
//				SudokuCell cell1 = null;
//				SudokuCell cell2 = null;
//				int cageTotal = 0;
//				for(Cage c : cages){
//					cageTotal += c.getTotal();
//					for(Location l : c.getCellLocations()){
//						if(!columnsUsed.contains(l.getColumn())){
//							if(cell1==null && cell2==null){
//								cell1=grid.getCell(l);
//							}
//							else{
//								cell2=grid.getCell(l);
//							}
//						}
//					}
//				}
//				int value = cageTotal - numberOfRegions*REGION_TOTAL;
//				Triple<SudokuCell, SudokuCell, Integer> triple = new Triple<>(cell1, cell2, value);
//				if(!CHECKED_SUM_CONSTRAINT.contains(triple)){
//					if(!cell1.isSolved() || !cell2.isSolved()){
//						while(true){
//							Set<Integer> impossibleValues = new TreeSet<>();
//							for(int cell1Value : cell1.getPossibleValues()){
//								boolean impossible = true;
//								for(int cell2Value : cell2.getPossibleValues()){
//									if(cell1Value+cell2Value==value) impossible = false;
//								}
//								if(impossible) impossibleValues.add(cell1Value);
//							}
//							cell1.getPossibleValues().removeAll(impossibleValues);
//							impossibleValues.clear();
//							for(int cell2Value : cell2.getPossibleValues()){
//								boolean impossible = true;
//								for(int cell1Value : cell1.getPossibleValues()){
//									if(cell1Value+cell2Value==value) impossible = false;
//								}
//								if(impossible) impossibleValues.add(cell2Value);
//							}
//							cell2.getPossibleValues().removeAll(impossibleValues);
//							if(impossibleValues.isEmpty()) break;
//						}
//						List<SudokuCell> cells = new ArrayList<>();
//						cells.add(cell1);
//						cells.add(cell2);
//						String s = cell1.getLocation() + " and " + cell2.getLocation() + " must sum to " + value +"\nThis is because the cages within column(s) "+columnsUsed+" must sum to "
//								+numberOfRegions*REGION_TOTAL+"\nThe cages in column(s) "+columnsUsed+" sum to "+cageTotal+"\nPossible values of these cells have been adjusted to fit this constraint";
//						Reason r = new Reason(s,cells);
//						CHECKED_SUM_CONSTRAINT.add(triple);
//						return r;
//					}
//				}
//			}
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
					String message = extraCell.getLocation()+" has been solved using the extended rule of 45 on the column(s) "+columnsUsed+
							"\nSince each column must total 45, the cages contained in "+numberOfRegions+" column(s) must sum to "+ numberOfRegions*REGION_TOTAL
							+"\nThe cages in column(s) "+columnsUsed+" sum to "+cageTotal;

					Reason reason = new Reason(message, extraCell);
					return reason;
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
					String message = missingCell.getLocation()+" has been solved using the extended rule of 45 on the column(s) "+columnsUsed+
							"\nSince each column must total 45, the cages contained in "+numberOfRegions+" column(s) must sum to "+ numberOfRegions*REGION_TOTAL
							+"\nThe cages in column(s) "+columnsUsed+" sum to "+cageTotal;

					Reason reason = new Reason(message, missingCell);
					return reason;
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
	public List<SudokuCell> setPossibleValuesForCages(){
		List<SudokuCell> changedCells = new ArrayList<>();
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
					Set<Integer> initialValues = new TreeSet<>();
					initialValues.addAll(cell.getPossibleValues());
					cell.getPossibleValues().retainAll(possibleSums);
					if(!initialValues.equals(cell.getPossibleValues())){
						changedCells.add(cell);
					}
				}
			}
		}
		return changedCells;
	}
	public List<SudokuCell> setPossibleCombinationsForCages(){
		List<SudokuCell> changedCells = new ArrayList<>();
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
					Set<Integer> initialValues = new TreeSet<>();
					initialValues.addAll(cell.getPossibleValues());
					cell.getPossibleValues().retainAll(possibleSums);
					if(!initialValues.equals(cell.getPossibleValues())) changedCells.add(cell);

				}
			}
		}
		return changedCells;
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
	public List<Reason> removeUniqueCageSumsFromRegions(){
		List<Reason> list = new ArrayList<>();
		Map<List<Cage>, Region> map = getRegionsContainingCages();
		Set<List<Cage>> keySet = map.keySet();
		for(List<Cage> cageList : keySet){
			Region region = map.get(cageList);
			for(Cage cage : cageList){
				Reason r = useSumsAsConstraints(cage, region);
				if(r!=null) list.add(r);
			}
		}
		return list;
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
			if(!cageRowList.isEmpty()) regionMap.put(cageRowList, Region.getInstance(Type.Row, i));
			if(!cageColumnList.isEmpty()) regionMap.put(cageColumnList, Region.getInstance(Type.Column, i));
			if(!cageNonetList.isEmpty()) regionMap.put(cageNonetList, Region.getInstance(Type.Nonet, i));
		}
		return regionMap;
	}

	/*
	 * removes a unique cage's combinations from the regions that it is contained within 
	 */
	public Reason useSumsAsConstraints(Cage cage, Region region){
		Pair<Cage, Region> cageRegion = new Pair<Cage, Region>(cage,region);
		if(CHECKED_CAGE_REGIONS.contains(cageRegion)) return null;
		if(!isUniqueSum(cage)) return null;
		if(cage.isSolved()) return null;
		Set<Integer> cagePossibleNumbers = getPossibleValues(cage);
		List<Combination> combinations = Sums.getSums(cage.getLength(), cage.getTotal(), cagePossibleNumbers);
		Combination combination = combinations.get(0);
		Set<Integer> numbers = combination.getNumbers();
		removeFromRegion(numbers, grid.getCells(cage), region);
		CHECKED_CAGE_REGIONS.add(cageRegion);
		Reason reason = new Reason("The "+cage.getTotal()+ " cage located at "+cage.getCellLocations().get(0)+" has only one possible combination and is fully contained within "+region
				+"\nThis means that this combination cannot appear in "+region+" therefore "+numbers +" has been removed from the possible values of cells in "+region+"\n\n");
		return reason;

	}
	public Reason solveSingleValueCells(){
		for(SudokuCell cell :getSingleValueCellList()){
			return solveSingleValueCell(cell);
		}
		return null;
	}
	public Set<Integer> getPossibleValues(Cage cage){
		Set<Integer> possibleValues = new TreeSet<>();
		for(SudokuCell cell : grid.getCells(cage)){
			possibleValues.addAll(cell.getPossibleValues());
		}
		return possibleValues;
	}
	public Reason setSinglePositionCombinationAllCages(){
		List<Cage> cages = getCagesWithUniqueSum();
		for(Cage cage : cages){
			Reason r = setSinglePositionCombination(cage);
			if(r != null) return r;
		}
		return null;
	}
	/*
	 * if a cage has a single possible combination, check if any of the numbers
	 * have a single possible position within the cage
	 */
	public Reason setSinglePositionCombination(Cage cage){
		if(!isUniqueSum(cage)) return null;
		if(cage.isSolved()) return null;
		Set<Integer> possibleNumbers = new TreeSet<>();
		for(Location l : cage.getUnsolvedLocations()){
			possibleNumbers.addAll(grid.getCell(l).getPossibleValues());
		}
		List<Combination> combinations = Sums.getSums(cage.getUnsolvedLocations().size(), cage.getRemaining(), possibleNumbers);
		Combination combination = combinations.get(0);
		Set<Integer> numbers = combination.getNumbers();
		for(int i : numbers){
			int possiblePositions = 0;
			for(Location l : cage.getUnsolvedLocations()){
				if(grid.getCell(l).getPossibleValues().contains(i)) possiblePositions++;
			}
			if(possiblePositions==1){
				int value = i;
				SudokuCell cell = null;
				//find the cell
				for(Location l : cage.getUnsolvedLocations()){
					if(grid.getCell(l).getPossibleValues().contains(i)) cell = grid.getCell(l);
				}
				if(!cell.isSolved()){
					solveCell(cell, value);
					String message = "The "+cage.getTotal()+" cage located at "+cage.getCellLocations().get(0)+" can only have one combination of numbers "+
							numbers+".\nThere is only one possible location for the value "+value+" within this cage.";
					Reason r = new Reason(message, cell);
					return r;
				}
			}

		}
		return null;

	}
	public Set<Integer> getPossibleValues(List<SudokuCell> cells){
		Set<Integer> possibleValues = new TreeSet<>();
		for(SudokuCell cell : cells){
			possibleValues.addAll(cell.getPossibleValues());
		}
		return possibleValues;
	}
	public List<SudokuCell> getSingleValueCellList(){
		List<SudokuCell> cellsList = new ArrayList<>();
		SudokuCell[][] cell = grid.getGrid();
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(cell[i][j].hasSinglePossibleValue() && !cell[i][j].isSolved()){
					cellsList.add(cell[i][j]);
				}
			}
		}
		return cellsList;
	}
	public Reason solveSingleValueCell(SudokuCell cell){
		if(cell.hasSinglePossibleValue()){
			int value = cell.getSinglePossibleValue();
			solveCell(cell, value);
			Reason reason = new Reason(cell.getLocation()+" has only one possible value", cell);
			return reason;
		}
		return null;
	}

	public void removeFromAllRegions(SudokuCell cell) {
		removeSolvedValueFromRow(cell);
		removeSolvedValueFromColumn(cell);
		removeSolvedValueFromNonet(cell);
	}
	//
	public void removeFromRegion(Set<Integer> value, List<SudokuCell> cellsToIgnore, Region region){
		for(int i : value){
			if(region.getRegion().equals(Type.Row)){
				removeFromRow(cellsToIgnore, i);
			}
			else if(region.getRegion().equals(Type.Column)){
				removeFromColumn(cellsToIgnore, i);
			}
			else if(region.getRegion().equals(Type.Nonet)){
				removeFromNonet(cellsToIgnore, i);
			}
		}
	}
	
	//
	public boolean isValidAtLocation(int value, Location location){
		int row = location.getRow();
		int column = location.getColumn();
		int nonet = location.getNonet();
		List<SudokuCell> cells = grid.getRow(row);
		for(int i=0;i<cells.size();i++){
			if(cells.get(i).getValue()==value){
				return false;
			}
		}
		cells = grid.getColumn(column);
		for(int i=0;i<cells.size();i++){
			if(cells.get(i).getValue()==value){
				return false;
			}
		}
		cells = grid.getNonet(nonet);
		for(int i=0;i<cells.size();i++){
			if(cells.get(i).getValue()==value){
				return false;
			}
		}
		
		return true;
	}
	private void removeSolvedValueFromRow(SudokuCell cell){
		int value = cell.getValue();
		removeFromRow(cell, value);
	}
	private void removeFromRow(SudokuCell cell, int value) {
		Location location = cell.getLocation();
		int row = location.getRow();
		List<SudokuCell> cells = grid.getRow(row);
		for(int i=0;i<SIZE;i++){
			if(!cells.get(i).equals(cell) && !cells.get(i).isSolved()) cells.get(i).setImpossibleValue(value);
		}
	}
	private void removeFromRow(List<SudokuCell> cells, int value) {
		Location location = cells.get(0).getLocation();
		int row = location.getRow();
		List<SudokuCell> cellRow = grid.getRow(row);
		for(int i=0;i<SIZE;i++){
			if(!cells.contains(cellRow.get(i)) && !cellRow.get(i).isSolved()) cellRow.get(i).setImpossibleValue(value);
		}
	}
	private void removeSolvedValueFromColumn(SudokuCell cell){
		int value = cell.getValue();
		removeFromColumn(cell, value);
	}
	private void removeFromColumn(SudokuCell cell, int value) {
		Location location = cell.getLocation();
		int column = location.getColumn();
		List<SudokuCell> cells = grid.getColumn(column);
		for(int i=0;i<SIZE;i++){
			if(!cells.get(i).equals(cell) && !cells.get(i).isSolved()) cells.get(i).setImpossibleValue(value);
		}
	}
	private void removeFromColumn(List<SudokuCell> cells, int value) {
		Location location = cells.get(0).getLocation();
		int column = location.getColumn();
		List<SudokuCell> cellColumn = grid.getColumn(column);
		for(int i=0;i<SIZE;i++){
			if(!cells.contains(cellColumn.get(i)) && !cellColumn.get(i).isSolved()) cellColumn.get(i).setImpossibleValue(value);
		}
	}
	private void removeSolvedValueFromNonet(SudokuCell cell){
		int value = cell.getValue();
		removeFromNonet(cell, value);
	}
	private void removeFromNonet(SudokuCell cell, int value) {
		Location location = cell.getLocation();
		int nonet = location.getNonet();
		List<SudokuCell> cells = grid.getNonet(nonet);
		for(int i=0;i<SIZE;i++){
			if(!cells.get(i).equals(cell) && !cells.get(i).isSolved()) cells.get(i).setImpossibleValue(value);
		}
	}
	private void removeFromNonet(List<SudokuCell> cells, int value) {
		Location location = cells.get(0).getLocation();
		int nonet = location.getNonet();
		List<SudokuCell> cellNonet = grid.getNonet(nonet);
		for(int i=0;i<SIZE;i++){
			if(!cells.contains(cellNonet.get(i)) && !cellNonet.get(i).isSolved()) cellNonet.get(i).setImpossibleValue(value);
		}
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


