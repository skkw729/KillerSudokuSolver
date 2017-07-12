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
	public List<SudokuCell> getCells(Cage c){
		
		List<SudokuCell> list = new ArrayList<>();
		for(Location l : c.getCellLocations()){
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

	public void setCages(List<Cage> cages) {
		this.cages = cages;
	}
	public void addCage(Cage cage){
		cages.add(cage);
	}
	public void removeCage(Cage cage){
		if(cages.contains(cage)) cages.remove(cage);
	}
	
	

}
