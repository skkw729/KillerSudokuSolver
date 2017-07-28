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
	public void solveCagesSpanningExtendRegions(){
		for(int i=1;i<=8;i++){
			solveCagesSpanningRegions(i);
		}
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
						List<SudokuCell> nonetCells = grid.getNonet(regionNumber);
						SudokuCell cellAtLocation = grid.getCell(location);
						if(!nonetCells.contains(cellAtLocation)){
							missingCell = cellAtLocation;
						}
					}
				}
			}
			int cellValue = REGION_TOTAL - cageTotal;
			int missingValue = REGION_TOTAL - rowColumnTotal;
			if(type.equals(Type.ROW)){
				missingCell = grid.getCell(Location.getInstance(regionNumber, missingValue));
			}
			else if(type.equals(Type.COLUMN)){
				missingCell = grid.getCell(Location.getInstance(missingValue, regionNumber));
			}
			solveCell(missingCell, cellValue);
			solvedCells.add(missingCell);
		}
		return solvedCells;
	}
	public void cageSplit(){
		//TODO
		//rule of 45 when using partially filled cage
	}
	public void solveCagesSpanningRegions(int numberOfRegions){
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
				}
			}
			//nonets
//			cages = new HashSet<>();
//			List<SudokuCell> nonet = grid.getNonet(i);
//			for(int j=1; j<numberOfRegions;j++){
//				nonet.addAll(grid.getNonet(j+i));
//			}
//			for(SudokuCell cell : nonet){
//				cages.add(grid.getCage(cell.getLocation()));
//			}
//			totalLength = 0;
//			for(Cage c : cages){
//				totalLength += c.getLength();
//			}
//			if(totalLength == numberOfRegions*SIZE+1){
//				int value = 0;
//				SudokuCell solvableCell = null;
//				for(Cage cage : cages){
//					value += cage.getTotal();
//					for(SudokuCell cell : grid.getCells(cage)){
//						boolean within = false;
//						for(int k=0; k<numberOfRegions; k++){
//							if(cell.getLocation().getNonet()== (k+i)) within = true;
//						}
//						if(!within) {
//							solvableCell = cell;
//						}
//					}
//					
//				}
//				value -= numberOfRegions*REGION_TOTAL;
//				solveCell(solvableCell, value);
//				String nonetsUsed="";
//				for(int k=0; k<numberOfRegions;k++){
//					nonetsUsed += (k+i);
//				}
//				
//				System.out.println("Solved using extended rule of 45 on nonets "+nonetsUsed);
//			}
//			cagesFromOtherRegion = new HashSet<>(); 
//			for(Cage c : cages){
//					for(SudokuCell cell : grid.getCells(c)){
//						boolean within = false;
//						for(int k=0; k<numberOfRegions; k++){
//							if(cell.getLocation().getNonet()== (k+i)) within = true;
//						}
//						if(!within) {
//							cagesFromOtherRegion.add(c);
//						}
//					}
//			}
//			for(Cage otherCage : cagesFromOtherRegion){
//				totalLength -= otherCage.getLength();	
//			}
//			cages.removeAll(cagesFromOtherRegion);
//			if(totalLength==(numberOfRegions*SIZE-1)){
//				int value = 0;
//				SudokuCell solvableCell = null;
//				for(Cage cage : cages){
//					value += cage.getTotal();
//					for(SudokuCell cell : nonet){
//						boolean within = false;
//						for(int k=0; k<numberOfRegions; k++){
//							if(cages.contains(grid.getCage(cell.getLocation()))) within = true;
//						}
//						if(!within) solvableCell = cell;
//					}
//					
//				}
//				value = numberOfRegions*REGION_TOTAL - value;
//				solveCell(solvableCell, value);
//				String nonetsUsed="";
//				for(int k=0; k<numberOfRegions;k++){
//					nonetsUsed += (k+i);
//				}
//				
//				System.out.println("Solved using extended rule of 45 on nonets "+nonetsUsed);
//				}
//			
		}
	}
	public void solveCagesSpanningExtendedRegion(){
		for(int i=1;i<9;i++){
			//for each pair of row/column/nonets
			//find the cage associated with each cell

			//rows
			
			Set<Cage> cages = new HashSet<>();
			List<SudokuCell> row = grid.getRow(i);
			row.addAll(grid.getRow(i+1));
			for(SudokuCell cell : row){
				cages.add(grid.getCage(cell.getLocation()));
			}
			int totalLength = 0;
			for(Cage c : cages){
				totalLength += c.getLength();
			}
			if(totalLength==(2*SIZE+1)){
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
				int value = cageTotal - 2*REGION_TOTAL;
				rowNumber = rowNumber - (SIZE*i + SIZE*(i+1));
				columnNumber = columnNumber - 2*REGION_TOTAL;

				extraCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				solveCell(extraCell, value);
				System.out.println("Solved using extended rule of 45 on rows " + i +" and "+ (i+1));
			}
			//check if removing cages containing cells from other regions results in a solvable cell
			Set<Cage> cagesFromOtherRegion = new HashSet<>(); 
			for(Cage c : cages){
				for(Location l : c.getCellLocations()){
					if(l.getRow()!=i && l.getRow()!=i+1){
						cagesFromOtherRegion.add(c);
					}
				}
			}
			for(Cage otherCage : cagesFromOtherRegion){
				totalLength -= otherCage.getLength();	
			}
			cages.removeAll(cagesFromOtherRegion);
			if(totalLength==(2*SIZE-1)){
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
				int value = 2*REGION_TOTAL - cageTotal;
				rowNumber = ( SIZE*i + SIZE*(i+1) ) - rowNumber;
				columnNumber = 2*REGION_TOTAL - columnNumber;

				missingCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				solveCell(missingCell, value);
				System.out.println("Solved using extended rule of 45 on rows " + i +" and "+ (i+1));


			}
			//columns
			cages = new HashSet<>();
			List<SudokuCell> column = grid.getColumn(i);
			column.addAll(grid.getColumn(i+1));
			for(SudokuCell cell : column){
				cages.add(grid.getCage(cell.getLocation()));
			}
			totalLength = 0;
			for(Cage c : cages){
				totalLength += c.getLength();
			}

			if(totalLength==(2*SIZE+1)){
				//find the extra cell
				SudokuCell extraCell = null; 
				int columnNumber=0;
				int rowNumber=0;
				int cageTotal=0;
				for(Cage c : cages){
					cageTotal += cageTotal;
					for(Location location : c.getCellLocations()){
						columnNumber += location.getColumn();
						rowNumber += location.getRow();
					}
				}
				int value = cageTotal - 2*REGION_TOTAL;
				columnNumber = columnNumber - (SIZE*i + SIZE*(i+1));
				rowNumber = rowNumber - 2*REGION_TOTAL;

				extraCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				solveCell(extraCell, value);
				System.out.println("Solved using extended rule of 45 on columns " + i +" and "+ (i+1));
			}
			//check if removing cages containing cells from other regions results in a solvable cell
			cagesFromOtherRegion = new HashSet<>(); 
			for(Cage c : cages){
				for(Location l : c.getCellLocations()){
					if(l.getColumn()!=i && l.getColumn()!=i+1){
						cagesFromOtherRegion.add(c);
					}
				}
			}
			for(Cage otherCage : cagesFromOtherRegion){
				totalLength -= otherCage.getLength();	
			}
			cages.removeAll(cagesFromOtherRegion);
			if(totalLength==(2*SIZE-1)){
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
				int value = 2*REGION_TOTAL - cageTotal;
				columnNumber = ( SIZE*i + SIZE*(i+1) ) - columnNumber;
				rowNumber = 2*REGION_TOTAL - rowNumber;

				missingCell = grid.getCell(Location.getInstance(rowNumber, columnNumber));
				solveCell(missingCell, value);
				System.out.println("Solved using extended rule of 45 on columns " + i +" and "+ (i+1));

			}
			//nonets
			cages = new HashSet<>();
			List<SudokuCell> nonet = grid.getNonet(i);
			nonet.addAll((grid.getNonet(i+1)));
			for(SudokuCell cell : nonet){
				cages.add(grid.getCage(cell.getLocation()));
			}
			totalLength = 0;
			for(Cage c : cages){
				totalLength += c.getLength();
			}
			if(totalLength == 2*SIZE+1){
				int value = 0;
				SudokuCell solvableCell = null;
				for(Cage cage : cages){
					value += cage.getTotal();
					for(SudokuCell cell : grid.getCells(cage)){
						if(cell.getLocation().getNonet()!=i && cell.getLocation().getNonet()!=i+1) {
							solvableCell = cell;
						
						}
					}
					
				}
				value -= 2*REGION_TOTAL;
				solveCell(solvableCell, value);
				System.out.println("Solved using extended rule of 45 on nonets " + i +" and "+ (i+1));
			}
			cagesFromOtherRegion = new HashSet<>(); 
			for(Cage c : cages){
				for(Location l : c.getCellLocations()){
					if(l.getNonet()!=i || l.getNonet()!=i+1){
						cagesFromOtherRegion.add(c);
					}
				}
			}
			for(Cage otherCage : cagesFromOtherRegion){
				totalLength -= otherCage.getLength();	
			}
			cages.removeAll(cagesFromOtherRegion);
			if(totalLength==(2*SIZE-1)){
				int value = 0;
				SudokuCell solvableCell = null;
				for(Cage cage : cages){
					value += cage.getTotal();
					for(SudokuCell cell : grid.getCells(cage)){
						if(cell.getLocation().getNonet()!=i && cell.getLocation().getNonet()!=i+1) {
							solvableCell = cell;
						
						}
					}
					
				}
				value = 2*REGION_TOTAL - value;
				solveCell(solvableCell, value);
				System.out.println("Solved using extended rule of 45 on nonets " + i +" and "+ (i+1));
			}
			
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
				solvableCellMap.put(cageListRow, Region.getInstance(Type.ROW, i));
			}
			span = 0;
			for(Cage c : cageListColumn){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListColumn, Region.getInstance(Type.COLUMN, i));
			}
			span = 0;
			for(Cage c : cageListNonet){
				span += c.getLength();
			}
			if(span == SIZE-1){
				solvableCellMap.put(cageListNonet,Region.getInstance(Type.NONET, i));
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
	public void setPossibleValuesForCagesOfLength2(){
		List<Cage> cages = grid.getCages();
		for(Cage cage : cages){
			if(!cage.isSolved() && cage.getLength()==2){
				for(SudokuCell cell : grid.getCells(cage)){
					for(SudokuCell cell2 : grid.getCells(cage)){
						if(!cell.equals(cell2) && !cell.getPossibleValues().equals(cell2.getPossibleValues())){
							if(cell.getPossibleValues().size()==2){
								cell2.getPossibleValues().retainAll(cell.getPossibleValues());
							}
							else if(cell2.getPossibleValues().size()==2){
								cell.getPossibleValues().retainAll(cell2.getPossibleValues());
							}
						}
					}
				}

			}
		}
		cages = getPartiallyFilledCages();
		for(Cage cage : cages){
			if(cage.getUnsolvedLocations().size()==2){
				for(SudokuCell cell : grid.getCells(cage.getUnsolvedLocations())){
					for(SudokuCell cell2 : grid.getCells(cage.getUnsolvedLocations())){
						if(!cell.equals(cell2) && !cell.getPossibleValues().equals(cell2.getPossibleValues())){
							if(cell.getPossibleValues().size()==2){
								cell2.getPossibleValues().retainAll(cell.getPossibleValues());
							}
							else if(cell2.getPossibleValues().size()==2){
								cell.getPossibleValues().retainAll(cell2.getPossibleValues());
							}
						}
					}
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
	//	public SudokuCell getHiddenSingleRow(){
	//		return sudokuSolver.getHiddenSingleRow();
	//	}
	//	public SudokuCell getHiddenSingleColumn(){
	//		return sudokuSolver.getHiddenSingleColumn();
	//	}
	//	public SudokuCell getHiddenSingleNonet(){
	//		return sudokuSolver.getHiddenSingleNonet();
	//	}
	private void solveCell(SudokuCell cell, int value){
		Cage cage = grid.getCage(cell.getLocation());
		cell.setValue(value);
		cage.setSolvedLocation(cell.getLocation());
		cage.decreaseRemaining(value);
		if(cage.getUnsolvedLocations().isEmpty()) cage.setSolved();
		removeFromAllRegions(cell);

	}
}


