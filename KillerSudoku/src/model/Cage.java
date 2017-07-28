package model;
import java.util.ArrayList;
import java.util.List;
public class Cage {
	private List<Location> locations;
	private List<Location> solvedLocations;
	private List<Location> unsolvedLocations;
	private boolean solved;
	private int total;
	private int remaining;//total - solvedValues
	public Cage(int total){
		this.total = total;
		locations = new ArrayList<>();
		solvedLocations = new ArrayList<>();
		unsolvedLocations = new ArrayList<>();
		remaining = total;
		solved = false;
	}
	public Cage(int total, List<Location> coordinates){
		this.total = total;
		locations = coordinates;
		unsolvedLocations = new ArrayList<>();
		solvedLocations = new ArrayList<>();
		unsolvedLocations.addAll(locations);
		remaining = total;
		solved = false;
	}
	public void init(){
		unsolvedLocations.addAll(locations);
		
	}
	public boolean isSolved(){
		return solved;
	}
	public void setSolved(){
		solved = true;
	}
	public List<Location> getUnsolvedLocations(){
		return unsolvedLocations;
	}
	public List<Location> getSolvedLocations(){
		return solvedLocations;
	}
	public int getRemaining(){
		return remaining;
	}
	public void decreaseRemaining(int value){
		remaining -= value;
	}
	public void setSolvedLocation(Location location){
		solvedLocations.add(location);
		unsolvedLocations.remove(location);
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
