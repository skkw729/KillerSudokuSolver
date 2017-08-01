package model;
import java.util.HashMap;
import java.util.Map;

public class Region {
	private Type region;
	private int number;
	private static Map<String, Region> REGIONS = new HashMap<>();
	private Region(Type type, int number){
		region = type;
		this.number = number;
	}
	public static Region getInstance(Type type, int number){
		String s = type+" "+number;
		Region r = REGIONS.get(s);
		if(r==null){
			r = new Region(type, number);
			return r;
		}
		return r;
	}
	public Type getRegion() {
		return region;
	}
	public void setRegion(Type region) {
		this.region = region;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String toString(){
		return region+" "+number;
	}
	public boolean equals(Object obj){
		if(this==obj) return true;
		if(!(obj instanceof Region)) return false;
		Region r = (Region) obj;
		return (r.getNumber()==this.getNumber() && r.getRegion().equals(this.getRegion()));
	}
}
