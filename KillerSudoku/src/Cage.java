import java.util.ArrayList;
import java.util.List;
public class Cage {
	private List<Location> cells;
	private int total;
	public Cage(int total){
		this.total = total;
		cells = new ArrayList<>();
	}
	public Cage(int total, List<Location> coordinates){
		this.total = total;
		cells = coordinates;
	}

	public void addCell(Location coordinate){
		cells.add(coordinate);
	}
	public void removeCell(Location coordinate){
		if(cells.contains(coordinate)){
			cells.remove(coordinate);
		}
	}
	public int getTotal(){return total;}
	
	public int getLength(){return cells.size();}
	
	public List<Location> getCells(){return cells;}
	
	public String toString(){
		return total+" - "+cells;
	}
}
