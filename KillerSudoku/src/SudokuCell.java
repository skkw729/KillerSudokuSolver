import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SudokuCell {
	private int value;
	private Location location;
	private boolean isSolved;
	private Set<Integer> possibleValues;
	private static final int GAME_SIZE = 9;
	
	public SudokuCell(Location location){
		value = 0;
		this.location = location;
		isSolved = false;
		possibleValues = new TreeSet<>();
		addPossibleValues();
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public boolean isSolved() {
		return isSolved;
	}
	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}
	public Set<Integer> getPossibleValues() {
		return possibleValues;
	}
	private void addPossibleValues() {
		for(int i=0;i<GAME_SIZE;i++){
			possibleValues.add(i+1);
		}
	}
	public void setPossibleValues(Set<Integer> values){
		possibleValues = values;
	}
	public void setImpossibleValue(int value){
		if(value>0 && value<=GAME_SIZE){
			if(possibleValues.contains(value)){
				possibleValues.remove(value);
			}
		}
	}
	public String toString(){
		return value+" "+location;
	}
}
