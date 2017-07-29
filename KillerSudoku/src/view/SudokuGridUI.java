package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
	private static final int CELL_SIZE = 50;
	private JFrame frame;
	private JPanel contentPanel;
	public SudokuGridUI(KillerSudokuGrid grid){
		this.grid = grid;
		contentPanel = new JPanel(new BorderLayout());
		frame = new JFrame("Killer Sudoku Solver");
		frame.setLayout(new BorderLayout());
		cages = grid.getCages();
		makeGrid(grid);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	private void makeGrid(KillerSudokuGrid grid) {
		contentPanel.removeAll();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,3));
		contentPanel.add(new ButtonPanel(), BorderLayout.EAST);
		contentPanel.add(panel, BorderLayout.CENTER);
		frame.setContentPane(contentPanel);
		for(int i=1;i<=SIZE;i++){
			List<SudokuCell> nonet = grid.getNonet(i);
			panel.add(new SudokuNonetUI(nonet));
		}
//		frame.setMinimumSize(new Dimension(SIZE*CELL_SIZE, SIZE*CELL_SIZE));
		frame.revalidate();
		frame.repaint();
		
	}
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("example1Answer.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
		solver.solveCagesSpanningExtendRegions();
		SudokuGridUI gridUI = new SudokuGridUI(grid);
		solver.setPossibleValuesForCages();
		solver.solveSingleValueCells();
		gridUI.makeGrid(grid);
		
	}
}
