import java.util.ArrayList;
import java.util.List;
public class Cage {
	private List<Coordinate> cells;
	private int total;
	public Cage(int total){
		this.total = total;
		cells = new ArrayList<>();
	}
	public Cage(int total, List<Coordinate> coordinates){
		this.total = total;
		cells = coordinates;
	}

	public void addCell(Coordinate coordinate){
		cells.add(coordinate);
	}
	public void removeCell(Coordinate coordinate){
		if(cells.contains(coordinate)){
			cells.remove(coordinate);
		}
	}
	public int getTotal(){return total;}
	
	public int getLength(){return cells.size();}
	
	public List<Coordinate> getCells(){return cells;}
	
	public String toString(){
		return total+" - "+cells;
	}
}
