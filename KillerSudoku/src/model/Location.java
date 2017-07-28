package model;
import java.util.HashMap;
import java.util.Map;

public class Location {
	private int row;
	private int column;
	private int nonet;
	private String stringRep;
	private static final Map<String, Location> LOCATIONS = new HashMap<>();

	public static Location getInstance(int row, int column){
		String stringRep = row+","+column;
		Location l = LOCATIONS.get(stringRep);
		if(l==null){
			l = new Location(row, column, stringRep);
			LOCATIONS.put(stringRep, l);
			return l;
		}
		return l;
	}
	private Location(int row, int column, String stringRep){
		if(row<1 || row>9) throw new IllegalArgumentException("Invalid row");
		if(column<1 || column>9) throw new IllegalArgumentException("Invalid column");
		this.row = row;
		this.column = column;
		this.setStringRep(stringRep);
		initNonet();
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	public boolean equals(Object obj){
		if(this==obj) return true;
		if(!(obj instanceof Location)) return false;
		Location c = (Location) obj;
		if((c.getColumn()==column && c.getRow()==row)) return true;
		return false;
	}
	public int hashCode(){
		int hc = 17;
		hc = hc*37 + row;
		hc = hc*37 + column;
		return hc;
	}
	public String toString(){
		return "("+row+","+column+")";
	}

	public int getNonet() {
		return nonet;
	}
	private void initNonet(){
		if(row<=3){
			if(column<=3) nonet=1;
			else if(column<=6) nonet=2;
			else if(column<=9) nonet=3;
		}
		else if(row<=6){
			if(column<=3) nonet=4;
			else if(column<=6) nonet=5;
			else if(column<=9) nonet=6;
		}
		else if(row<=9){
			if(column<=3) nonet=7;
			else if(column<=6) nonet=8;
			else if(column<=9) nonet=9;
		}
	}
	public String getStringRep() {
		return stringRep;
	}
	public void setStringRep(String stringRep) {
		this.stringRep = stringRep;
	}
}
