import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;

public class AnswerParser {
public static SudokuGrid parseAnswer(String filename) throws FileNotFoundException{
		SudokuGrid grid = new SudokuGrid();
		Scanner scanner = new Scanner(CageParser.class.getResourceAsStream(filename));//locate file in the same location as this class
		while(scanner.hasNext()){
			for(int i=1; i<=9;i++){
				Scanner line = new Scanner(scanner.nextLine());//take the first line of the file as input
				SudokuCell[] row = grid.getRow(i);
				for(int j=0;j<9;j++){
					int value = line.nextInt();
					row[j].setValue(value);
				}
			}
			
			
		}
		return grid;
		
	}
	public static boolean checkAnswer(SudokuGrid grid, SudokuGrid answer){
		boolean result = true;
		SudokuCell[][] array = grid.getGrid();
		SudokuCell[][] otherArray = answer.getGrid();
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(array[i][j].isSolved()){
					if(array[i][j].getValue()!=otherArray[i][j].getValue()) result = false;
				}
			}
			
		}
		if(!result){
			for(int i=0; i<9;i++){
				for(int j=0;j<9;j++){
					if(array[i][j].getValue()!=otherArray[i][j].getValue()){
						System.out.println("Incorrect value"+array[i][j].getValue() +" at "+array[i][j].getLocation()+" expected value: "+otherArray[i][j].getValue());
					}
				}
			}
		}
		return result;
	}
}
