package view;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.List;
import model.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.*;
public class SudokuGridUI {
	private SudokuCellUI[][] gridUI;
	private KillerSudokuGrid grid;
	private List<Cage> cages;
	private static final int SIZE=9;
	private JFrame frame;
	private JPanel gridPanel;
	public SudokuGridUI(KillerSudokuGrid grid){
		this.grid = grid;
		gridPanel = new JPanel(new GridLayout(3,3));
		frame = new JFrame("Killer Sudoku Solver");
		cages = grid.getCages();
		frame.setContentPane(gridPanel);
		for(int i=1;i<=SIZE;i++){
			List<SudokuCell> nonet = grid.getNonet(i);
			gridPanel.add(new SudokuNonetUI(nonet));
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("example1Answer.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
		solver.solveCagesSpanningExtendRegions();
		SudokuGridUI gridUI = new SudokuGridUI(grid);
	}
}
