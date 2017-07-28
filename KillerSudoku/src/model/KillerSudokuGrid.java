package model;
import java.util.ArrayList;
import java.util.List;

public class KillerSudokuGrid extends SudokuGrid {
	private List<Cage> cages;
	
	public KillerSudokuGrid(){
		super();
		cages = new ArrayList<>();
	}
	public KillerSudokuGrid(List<Cage> cages){
		super();
		this.setCages(cages);
	}
	public List<SudokuCell> getCells(){
		List<SudokuCell> cells = new ArrayList<>();
		for(int i=1;i<=9;i++){
			for(int j=1;j<=9;j++){
				SudokuCell c = super.getCell(Location.getInstance(i, j));
				cells.add(c);
			}
		}
		return cells;
	}
	public List<SudokuCell> getCells(Cage c){
		
		List<SudokuCell> list = new ArrayList<>();
		for(Location l : c.getCellLocations()){
			list.add(super.getCell(l));
		}
		return list;
	}
	public List<SudokuCell> getCells(List<Location> locations){
		
		List<SudokuCell> list = new ArrayList<>();
		for(Location l : locations){
			list.add(super.getCell(l));
		}
		return list;
	}
	public List<Cage> getCages() {
		return cages;
	}
	public Cage getCage(List<Location> coordinates){
		for(Cage c:cages)
		{
			if(c.getCellLocations().equals(coordinates)){
				return c;
			}
		}
		return null;
	}
	public Cage getCage(Location location){
		for(Cage c:cages)
		{
			if(c.getCellLocations().contains(location)){
				return c;
			}
		}
		return null;
	}

	public void setCages(List<Cage> cages) {
		this.cages = cages;
	}
	public void addCage(Cage cage){
		cages.add(cage);
	}
	public void removeCage(Cage cage){
		if(cages.contains(cage)) cages.remove(cage);
	}
	public void printPossibleValues(){
		for(SudokuCell cell : getCells()){
			
			System.out.println(cell.getLocation()+" "+cell.getPossibleValues());
		}
	}
	

}
