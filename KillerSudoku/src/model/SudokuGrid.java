package model;
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
	
	public List<SudokuCell> getRow(int rowNumber){
		List<SudokuCell> row = new ArrayList<>();
		for(int i=0;i<SIZE;i++){
			row.add(grid[rowNumber-1][i]);
		}
		return row;
	}
	public List<SudokuCell> getColumn(int colNumber){
		List<SudokuCell> column = new ArrayList<>();
		for(int i=0;i<SIZE;i++){
			column.add(grid[i][colNumber-1]);
		}
		return column;
	}
	public List<SudokuCell> getCellsFromRow(int rowNumber, int startColumn, int endColumn){
		List<SudokuCell> cells = new ArrayList<>();
		List<SudokuCell> row = getRow(rowNumber);
		for(int i=startColumn;i<=endColumn;i++){
			cells.add(row.get(i-1));
		}
		return cells;	
	}
	public List<SudokuCell> getNonet(int nonetNumber){
		
		List<SudokuCell> list = new ArrayList<>();
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(grid[i][j].getLocation().getNonet()==nonetNumber){
					list.add(grid[i][j]);
				}
			}
		}
		return list;
	}
	public void printGrid(){
		for(int i=0;i<9;i++){
			List<SudokuCell> row = getRow(i+1);
			for(int j=0;j<row.size();j++){
				System.out.print(row.get(j)+" ");
			}
			System.out.println("");
		}
	}
	public void printNonets(){
		for(int i=1;i<=9;i++){
			List<SudokuCell> nonet = getNonet(i);
			System.out.println("Nonet "+i);
			for(int j=0;j<nonet.size()-3;j+=3){
				
				System.out.print(nonet.get(j));
				System.out.print(nonet.get(j+1));
				System.out.println(nonet.get(j+2));
			}
		}
	}
}

