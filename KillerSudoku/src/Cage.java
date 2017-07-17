import java.util.ArrayList;
import java.util.List;
public class Cage {
	private List<Location> locations;
	private int total;
	public Cage(int total){
		this.total = total;
		locations = new ArrayList<>();
	}
	public Cage(int total, List<Location> coordinates){
		this.total = total;
		locations = coordinates;
	}

	public void addCell(Location coordinate){
		locations.add(coordinate);
	}
	public void removeCell(Location coordinate){
		if(locations.contains(coordinate)){
			locations.remove(coordinate);
		}
	}
	public int getTotal(){return total;}
	
	public int getLength(){return locations.size();}
	
	public List<Location> getCellLocations(){return locations;}
		
	public String toString(){
		return total+" - "+locations;
	}
}
