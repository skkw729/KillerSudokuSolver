import java.util.Set;

//Set of integers that sum to a value
public class Combination {
	private int total;
	private Set<Integer> numbers;
	
	public Combination(int total, Set<Integer> numbers){
		this.total = total;
		this.numbers = numbers;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Set<Integer> getNumbers() {
		return numbers;
	}

	public void setNumbers(Set<Integer> numbers) {
		this.numbers = numbers;
	}
	public boolean contains(int i){
		return numbers.contains(i);
	}
	public boolean equals(Object obj){
		if(this==obj) return true;
		if(!(obj instanceof Combination)) return false;
		Combination c = (Combination) obj;
		return numbers.equals(c.getNumbers());
	}
	public String toString(){
		return numbers.toString();
	}
	
}
