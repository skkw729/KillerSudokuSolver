package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.*;
public class SudokuGridUI {
	private SudokuCellUI[][] gridUI;
	private KillerSudokuGrid grid;
	private List<Cage> cages;
	private List<SudokuCellUI> listCellUI;
	Map<Cage, Color> cageColourMap;
	private static final int SIZE=9;
	private static final int CELL_SIZE = 70;
	private JFrame frame;
	private JPanel contentPanel;
	private static Color RED = new Color(255, 80, 80);
	private static Color YELLOW = new Color(255, 255, 153);
	private static Color GREEN = new Color(153, 255, 153);
	private static Color BLUE = new Color(153, 204, 255);
	private static Color PINK = new Color(255, 204, 255);
	public SudokuGridUI(KillerSudokuGrid grid){
		this.grid = grid;
		listCellUI = new ArrayList<>();
		cageColourMap = new HashMap<>();
		contentPanel = new JPanel(new BorderLayout());
		frame = new JFrame("Killer Sudoku Solver");
		frame.setLayout(new BorderLayout());
		cages = grid.getCages();
		assignAllCageColours();
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
		Map<SudokuCell, Integer> cageLeadMap = getCageLeads();
		for(int i=1;i<=SIZE;i++){
			JPanel nonetPanel = new JPanel(new GridLayout(3,3));
			List<SudokuCell> nonet = grid.getNonet(i);
			for(SudokuCell cell : nonet){
				Cage cage = grid.getCage(cell.getLocation());
				Color colour = cageColourMap.get(cage);
				if(cageLeadMap.get(cell) != null){
					int cageTotal = cageLeadMap.get(cell);
					SudokuCellUI sudokuCellUI = new SudokuCellUI(cell, colour, cageTotal);
					listCellUI.add(sudokuCellUI);
					nonetPanel.add(sudokuCellUI);
				}
				else{
					SudokuCellUI sudokuCellUI = new SudokuCellUI(cell, colour);
					listCellUI.add(sudokuCellUI);
					nonetPanel.add(sudokuCellUI);
				}
				nonetPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
			}
			panel.add(nonetPanel);
		}
		//		frame.setMinimumSize(new Dimension(SIZE*CELL_SIZE, SIZE*CELL_SIZE));
		frame.revalidate();
		frame.repaint();

	}
	public Map<SudokuCell, Integer> getCageLeads(){
		Map<SudokuCell, Integer> cageLeadMap = new HashMap<>();
		for(Cage cage : cages){
			int row = -1;
			for(Location l : cage.getCellLocations()){
				if(row==-1 || l.getRow()<row){
					row = l.getRow();
				}
			}
			int col = -1;
			for(Location l : cage.getCellLocations()){
				if(l.getRow()==row){
					if(col==-1 || l.getColumn()<col){
						col = l.getColumn();
					}
				}
			}
			SudokuCell cell = grid.getCell(Location.getInstance(row, col));
			int cageTotal = cage.getTotal();
			cageLeadMap.put(cell, cageTotal);
		}
		return cageLeadMap;
	}
	public void assignAllCageColours(){
		Map<Cage, List<Cage>> adjCages = getAdjacentCageMap();
		for(Cage cage : cages){
			assignColour(cage, adjCages);
		}
	}
	private void assignColour(Cage cage, Map<Cage,List<Cage>> adjCages){
		Color colour = RED;
		List<Cage> adjacentCages = adjCages.get(cage);
		if(adjacentCages != null){

			boolean redUsed = false;
			boolean blueUsed = false;
			boolean yellowUsed = false;
			boolean greenUsed = false;
			for(Cage c : adjacentCages){
				Color adjColor = cageColourMap.get(c);
				if(adjColor != null){
					if(adjColor.equals(RED)){
						redUsed = true;
					}
					else if(adjColor.equals(BLUE)){
						blueUsed = true;
					}
					else if(adjColor.equals(YELLOW)){
						yellowUsed = true;
					}
					else if(adjColor.equals(GREEN)){
						greenUsed = true;
					}
				}
				//greedy colouring
				if(redUsed){
					colour = BLUE;
					if(blueUsed){
						colour = YELLOW;
						if(yellowUsed){
							colour = GREEN;
							if(greenUsed){
								colour = PINK;
							}
						}


					}

				}
			}
		}
		cageColourMap.put(cage, colour);
	}
	private Map<Cage, List<Cage>> getAdjacentCageMap(){
		Map<Cage, List<Cage>> adjacentCages = new HashMap<>();
		for(Cage cage : cages){
			List<Cage> cageList = new ArrayList<>();
			Set<Cage> cageSet = new HashSet<>();
			for(Location l : cage.getCellLocations()){
				int column = l.getColumn();
				int row = l.getRow();
				if(column-1>0){ 
					Location west = Location.getInstance(row, column-1);
					if(!cage.getCellLocations().contains(west)){
						cageSet.add(grid.getCage(west));
					}
				}
				if(row-1>0){
					Location north = Location.getInstance(row-1, column);
					if(!cage.getCellLocations().contains(north)){
						cageSet.add(grid.getCage(north));
					}
				}
				if(column+1<=SIZE){
					Location east = Location.getInstance(row, column+1);
					if(!cage.getCellLocations().contains(east)){
						cageSet.add(grid.getCage(east));
					}
				}
				if(row+1<=SIZE){
					Location south = Location.getInstance(row+1, column);
					if(!cage.getCellLocations().contains(south)){
						cageSet.add(grid.getCage(south));
					}
				}
			}
			cageList.addAll(cageSet);
			adjacentCages.put(cage, cageList);
		}
		return adjacentCages;
	}

	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("example1Answer.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);
		solver.solveCagesSpanningExtendRegions();
		SudokuGridUI gridUI = new SudokuGridUI(grid);
		//		solver.setPossibleValuesForCages();
		//		solver.solveSingleValueCells();
		//		gridUI.makeGrid(grid);

	}
}
