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
		SudokuCell[] cells = grid.getRow(row);
		for(int i=0;i<SIZE;i++){
			if(!cells[i].equals(cell) && !cells[i].isSolved()) cells[i].setImpossibleValue(value);
		}
	}
	private void removeFromRow(List<SudokuCell> cells, int value) {
		Location location = cells.get(0).getLocation();
		int row = location.getRow();
		SudokuCell[] cellRow = grid.getRow(row);
		for(int i=0;i<SIZE;i++){
			if(!cells.contains(cellRow[i]) && !cellRow[i].isSolved()) cellRow[i].setImpossibleValue(value);
		}
	}
	private void removeSolvedValueFromColumn(SudokuCell cell){
		int value = cell.getValue();
		removeFromColumn(cell, value);
	}
	private void removeFromColumn(SudokuCell cell, int value) {
		Location location = cell.getLocation();
		int column = location.getColumn();
		SudokuCell[] cells = grid.getColumn(column);
		for(int i=0;i<SIZE;i++){
			if(!cells[i].equals(cell) && !cells[i].isSolved()) cells[i].setImpossibleValue(value);
		}
	}
	private void removeFromColumn(List<SudokuCell> cells, int value) {
		Location location = cells.get(0).getLocation();
		int column = location.getColumn();
		SudokuCell[] cellColumn = grid.getColumn(column);
		for(int i=0;i<SIZE;i++){
			if(!cells.contains(cellColumn[i]) && !cellColumn[i].isSolved()) cellColumn[i].setImpossibleValue(value);
		}
	}
	private void removeSolvedValueFromNonet(SudokuCell cell){
		int value = cell.getValue();
		removeFromNonet(cell, value);
	}
	private void removeFromNonet(SudokuCell cell, int value) {
		Location location = cell.getLocation();
		int nonet = location.getNonet();
		SudokuCell[] cells = grid.getNonet(nonet);
		for(int i=0;i<SIZE;i++){
			if(!cells[i].equals(cell) && !cells[i].isSolved()) cells[i].setImpossibleValue(value);
		}
	}
	private void removeFromNonet(List<SudokuCell> cells, int value) {
		Location location = cells.get(0).getLocation();
		int nonet = location.getNonet();
		SudokuCell[] cellNonet = grid.getNonet(nonet);
		for(int i=0;i<SIZE;i++){
			if(!cells.contains(cellNonet[i]) && !cellNonet[i].isSolved()) cellNonet[i].setImpossibleValue(value);
		}
	}
	public boolean isValidAtLocation(int value, Location location){
		int row = location.getRow();
		int column = location.getColumn();
		int nonet = location.getNonet();
		SudokuCell[] cells = grid.getRow(row);
		for(int i=0;i<cells.length;i++){
			if(cells[i].getValue()==value){
				return false;
			}
		}
		cells = grid.getColumn(column);
		for(int i=0;i<cells.length;i++){
			if(cells[i].getValue()==value){
				return false;
			}
		}
		cells = grid.getNonet(nonet);
		for(int i=0;i<cells.length;i++){
			if(cells[i].getValue()==value){
				return false;
			}
		}
		
		return true;
	}
	public SudokuCell getHiddenSingleRow(){
		List<SudokuCell> cells = new ArrayList<>();
		for(int number=1;number<=SIZE;number++){
			
			for(int i=0;i<SIZE;i++){
				SudokuCell[] row = grid.getRow(i+1);
				for(int j=0; j<row.length;j++){
					if(!row[j].isSolved() && row[j].getPossibleValues().contains(number)){
						if(isValidAtLocation(number,row[j].getLocation())){
							cells.add(row[j]);
							hiddenSingleMap.put(row[j],number);
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
				SudokuCell[] column = grid.getColumn(i+1);
				for(int j=0; j<column.length;j++){
					if(!column[j].isSolved() && column[j].getPossibleValues().contains(number)){
						if(isValidAtLocation(number,column[j].getLocation())){
							cells.add(column[j]);
							hiddenSingleMap.put(column[j],number);
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
				SudokuCell[] nonet = grid.getNonet(i+1);
				for(int j=0; j<nonet.length;j++){
					if(!nonet[j].isSolved() && nonet[j].getPossibleValues().contains(number)){
						if(isValidAtLocation(number,nonet[j].getLocation())){
							cells.add(nonet[j]);
							hiddenSingleMap.put(nonet[j],number);
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
