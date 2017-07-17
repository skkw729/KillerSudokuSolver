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
				grid[i][j] = new SudokuCell(Location.getInstance(i+1,j+1));
			}
		}
		
	}
	public SudokuCell getCell(Location coordinate){
		return grid[coordinate.getRow()-1][coordinate.getColumn()-1];
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
		List<SudokuCell> list = new ArrayList<>();
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(grid[i][j].getLocation().getNonet()==nonetNumber){
					list.add(grid[i][j]);
				}
			}
		}
		return list.toArray(nonet);
	}
	public void printGrid(){
		for(int i=0;i<9;i++){
			SudokuCell[] row = getRow(i+1);
			for(int j=0;j<row.length;j++){
				System.out.print(row[j]+" ");
			}
			System.out.println("");
		}
	}
	public void printNonets(){
		for(int i=1;i<=9;i++){
			SudokuCell[] nonet = getNonet(i);
			System.out.println("Nonet "+i);
			for(int j=0;j<nonet.length;j+=3){
				
				System.out.print(nonet[j]);
				System.out.print(nonet[j+1]);
				System.out.println(nonet[j+2]);
			}
		}
	}
}

