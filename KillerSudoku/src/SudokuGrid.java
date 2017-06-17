import java.util.ArrayList;
import java.util.List;

public class SudokuGrid {
	private static final int SIZE = 9;
	private SudokuCell[][] grid;

	public SudokuGrid()
	{
		grid = new SudokuCell[SIZE][SIZE];
		initSudokuCells();

	}

	private void initSudokuCells() {
		for(int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				grid[i][j] = new SudokuCell(new Coordinate(i+1,j+1));
			}
		}
		
	}
	public SudokuCell getCell(Coordinate coordinate){
		return grid[coordinate.getRow()][coordinate.getColumn()];
	}
	
	public SudokuCell[][] getGrid(){
		return grid;
	}
	
	public SudokuCell[] getRow(int rowNumber){
		SudokuCell[] row = new SudokuCell[SIZE];
		for(int i=0;i<SIZE;i++){
			row[i] = grid[rowNumber-1][i];
		}
		return row;
	}
	public SudokuCell[] getColumn(int colNumber){
		SudokuCell[] column = new SudokuCell[SIZE];
		for(int i=0;i<SIZE;i++){
			column[i] = grid[i][colNumber-1];
		}
		return column;
	}
	public List<SudokuCell> getCellsFromRow(int rowNumber, int startColumn, int endColumn){
		List<SudokuCell> cells = new ArrayList<>();
		SudokuCell[] row = getRow(rowNumber);
		for(int i=startColumn;i<=endColumn;i++){
			cells.add(row[i-1]);
		}
		return cells;	
	}
	public SudokuCell[] getNonet(int nonetNumber){
		SudokuCell[] nonet = new SudokuCell[SIZE];
		List<SudokuCell> cellsList = new ArrayList<>();
		switch(nonetNumber){
		case 1: cellsList.addAll(getCellsFromRow(1, 1, 3));
				cellsList.addAll(getCellsFromRow(2, 1, 3));
				cellsList.addAll(getCellsFromRow(3, 1, 3));
				break;
		case 2: cellsList.addAll(getCellsFromRow(1, 4, 6));
				cellsList.addAll(getCellsFromRow(2, 4, 6));
				cellsList.addAll(getCellsFromRow(3, 4, 6));
				break;
		case 3: cellsList.addAll(getCellsFromRow(4, 7, 9));
				cellsList.addAll(getCellsFromRow(5, 7, 9));
				cellsList.addAll(getCellsFromRow(6, 7, 9));
				break;
		case 4: cellsList.addAll(getCellsFromRow(4, 1, 3));
				cellsList.addAll(getCellsFromRow(5, 1, 3));
				cellsList.addAll(getCellsFromRow(6, 1, 3));
				break;
		case 5: cellsList.addAll(getCellsFromRow(4, 4, 6));
				cellsList.addAll(getCellsFromRow(5, 4, 6));
				cellsList.addAll(getCellsFromRow(6, 4, 6));
				break;
		case 6: cellsList.addAll(getCellsFromRow(4, 7, 9));
				cellsList.addAll(getCellsFromRow(5, 7, 9));
				cellsList.addAll(getCellsFromRow(6, 7, 9));
				break;
		case 7: cellsList.addAll(getCellsFromRow(7, 1, 3));
				cellsList.addAll(getCellsFromRow(8, 1, 3));
				cellsList.addAll(getCellsFromRow(9, 1, 3));
				break;
		case 8: cellsList.addAll(getCellsFromRow(7, 4, 6));
				cellsList.addAll(getCellsFromRow(8, 4, 6));
				cellsList.addAll(getCellsFromRow(9, 4, 6));
				break;
		case 9: cellsList.addAll(getCellsFromRow(7, 7, 9));
				cellsList.addAll(getCellsFromRow(8, 7, 9));
				cellsList.addAll(getCellsFromRow(9, 7, 9));
				break;
		}
		nonet = (SudokuCell[]) cellsList.toArray();
		return nonet;
	}
}

