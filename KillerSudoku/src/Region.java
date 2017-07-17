
public class Region {
	Type region;
	int number;
	public Region(Type type, int number){
		region = type;
		this.number = number;
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

}
