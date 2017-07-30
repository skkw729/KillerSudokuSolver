package model;

import java.util.List;

/*
 * class to be read by driver class in order to display informative dialog to user
 */
public class Reason {
	private String message;
	private SudokuCell cell;
	private List<SudokuCell> cellsChanged;
	public Reason(String s){
		message = s;
	}
	public Reason(String s, SudokuCell c){
		this(s);
		cell = c;
	}
	public Reason(String s, List<SudokuCell> cells){
		message = s;
		cellsChanged = cells;
	}
	public String getMessage(){
		return message;
	}
	public SudokuCell getCell(){
		return cell;
	}
	public List<SudokuCell> getCellList(){
		return cellsChanged;
	}
	public void setMessage(String s){
		message = s;
	}
	public void setCell(SudokuCell cell){
		this.cell = cell;
	}
	public void setCellList(List<SudokuCell> cells){
		cellsChanged = cells;
	}
}
