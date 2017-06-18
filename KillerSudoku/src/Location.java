
public class Location {
	private int row;
	private int column;
	private int nonet;
	
	public Location(int row, int column){
		if(row<1 || row>9) throw new IllegalArgumentException("Invalid row");
		if(column<1 || column>9) throw new IllegalArgumentException("Invalid column");
		this.row = row;
		this.column = column;
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
}
