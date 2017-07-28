package model;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CageParser {

	
	public static List<Cage> parseCages(String filename) throws FileNotFoundException{
		
		Scanner scanner = new Scanner(CageParser.class.getResourceAsStream(filename));//locate file in the same location as this class
		List<Cage> cages = new ArrayList<>();
		
		while(scanner.hasNext()){
			Scanner line = new Scanner(scanner.nextLine());//take the first line of the file as input
			int total = line.nextInt();
			Cage cage = new Cage(total);
			String coordinate = line.next();//parse the coordinate
			int row = Integer.parseInt(coordinate.substring(0,1));//ignores the comma between the numbers
			int column = Integer.parseInt(coordinate.substring(2));
			cage.addCell(Location.getInstance(row,column));
			while(line.hasNext()){//add additional coordinates
				coordinate = line.next();
				row = Integer.parseInt(coordinate.substring(0,1));
				column = Integer.parseInt(coordinate.substring(2));
				cage.addCell(Location.getInstance(row,column));
			}
			cage.init();
			cages.add(cage);
		}
		return cages;
		
	}
}
