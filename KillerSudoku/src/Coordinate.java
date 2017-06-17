
public class Coordinate {
	private int row;
	private int column;
	
	public Coordinate(int row, int column){
		if(row<1 || row>9) throw new IllegalArgumentException("Invalid row");
		if(column<1 || column>9) throw new IllegalArgumentException("Invalid column");
		this.row = row;
		this.column = column;
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
		if(!(obj instanceof Coordinate)) return false;
		Coordinate c = (Coordinate) obj;
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
}
