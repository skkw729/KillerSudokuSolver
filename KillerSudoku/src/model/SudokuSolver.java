package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SudokuSolver {
	private SudokuGrid grid;
	private static final int SIZE = 9;
	private boolean solved;
	private Map<SudokuCell, Integer> hiddenSingleMap; 
	public SudokuSolver(SudokuGrid grid){
		this.grid = grid;
		solved = false;
		hiddenSingleMap = new HashMap<>();
	}
	public SudokuCell getSingleValueCell(){
		
		SudokuCell[][] cell = grid.getGrid();
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(cell[i][j].getPossibleValues().size()==1 && !cell[i][j].isSolved()){
					return cell[i][j];
				}
			}
		}
		return null;
	}
//	public List<SudokuCell> getSolvedCells(int value){
//		List<SudokuCell> cellsList = new ArrayList<>();
//		SudokuCell cells[][] = grid.getGrid(); 
//		for(int i=0;i<SIZE;i++){
//			for(int j=0;i<SIZE;j++){
//				if(cells[i][j].isSolved()&&cells[i][j].getValue()==value){
//					cellsList.add(cells[i][j]);
//				}
//			}
//		}
//		return cellsList;
//	}
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
	public boolean solveSingleValueCell(SudokuCell cell){
		if(cell.hasSinglePossibleValue()){
			int value = cell.getSinglePossibleValue();
			cell.setValue(value);
	//		System.out.println("Cell has a single possible value");
			removeFromAllZones(cell);
	//		System.out.println("Remove value as possible value from all rows, columns and nonets because a number can only appear once within a row, column or nonet.");
			return true;
		}
		throw new IllegalStateException("single value cell was not solved");
	}
	public void removeFromAllZones(SudokuCell cell) {
		removeSolvedValueFromRow(cell);
		removeSolvedValueFromColumn(cell);
		removeSolvedValueFromNonet(cell);
	}
	public void removeFromRegion(Set<Integer> value, List<SudokuCell> cellsToIgnore, Region region){
		for(int i : value){
			if(region.getRegion().equals(Type.ROW)){
				removeFromRow(cellsToIgnore, i);
			}
			else if(region.getRegion().equals(Type.COLUMN)){
				removeFromColumn(cellsToIgnore, i);
			}
			else if(region.getRegion().equals(Type.NONET)){
				removeFromNonet(cellsToIgnore, i);
			}
		}
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
	public SudokuCell getHiddenSingleRow(){
		List<SudokuCell> cells = new ArrayList<>();
		for(int number=1;number<=SIZE;number++){
			
			for(int i=0;i<SIZE;i++){
				List<SudokuCell> row = grid.getRow(i+1);
				for(int j=0; j<row.size();j++){
					if(!row.get(j).isSolved() && row.get(j).getPossibleValues().contains(number)){
						if(isValidAtLocation(number,row.get(j).getLocation())){
							cells.add(row.get(j));
							hiddenSingleMap.put(row.get(j),number);
						}
					}
				}
				if(cells.size()==1){
					return cells.get(0);
				}
			}
			
		}
		hiddenSingleMap.clear();
		return null;
	}
	public SudokuCell getHiddenSingleColumn(){
		List<SudokuCell> cells = new ArrayList<>();
		for(int number=1;number<=SIZE;number++){
			
			for(int i=0;i<SIZE;i++){
				List<SudokuCell> column = grid.getColumn(i+1);
				for(int j=0; j<column.size();j++){
					if(!column.get(j).isSolved() && column.get(j).getPossibleValues().contains(number)){
						if(isValidAtLocation(number,column.get(j).getLocation())){
							cells.add(column.get(j));
							hiddenSingleMap.put(column.get(j),number);
						}
					}
				}
				if(cells.size()==1){
					return cells.get(0);
				}
			}
			
		}
		hiddenSingleMap.clear();
		return null;
	}
	public SudokuCell getHiddenSingleNonet(){
		List<SudokuCell> cells = new ArrayList<>();
		for(int number=1;number<=SIZE;number++){
			
			for(int i=0;i<SIZE;i++){
				List<SudokuCell> nonet = grid.getNonet(i+1);
				for(int j=0; j<nonet.size();j++){
					if(!nonet.get(j).isSolved() && nonet.get(j).getPossibleValues().contains(number)){
						if(isValidAtLocation(number,nonet.get(j).getLocation())){
							cells.add(nonet.get(j));
							hiddenSingleMap.put(nonet.get(j),number);
						}
					}
				}
				if(cells.size()==1){
					return cells.get(0);
				}
			}
			
		}
		hiddenSingleMap.clear();
		return null;
	}
	public void solveHiddenSingle(SudokuCell cell){
		int value = hiddenSingleMap.get(cell);
		cell.setValue(value);		
	}
//	public void solveOneCell(){
//		if(!solved){
//			List<SudokuCell> singleValueCells = getSingleValueCellList();
//			if(singleValueCells.size()>0){
//				SudokuCell cell = getSingleValueCell();
//				solveSingleValueCell(cell);
//			}
//			else if(getHiddenSingleRow()!=null){
//				SudokuCell cell = getHiddenSingleRow();
//				solveHiddenSingle(cell);
//			}
//			
//		}
//	}
}
