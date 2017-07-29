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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.LoadListener;
import controller.SolveListener;
import model.*;
public class SudokuGridUI {
	private KillerSudokuGrid grid;
	private List<Cage> cages;
	private List<SudokuCellUI> listCellUI;
	private ButtonPanel buttonPanel;
	private Map<Cage, Color> cageColourMap;
	private static final int SIZE=9;
	private static final int CELL_SIZE = 70;
	private JFrame frame;
	private JPanel gridPanel, contentPanel;
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
		initGrid();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	public KillerSudokuGrid getGrid(){
		return grid;
	}
	public void initGrid(){
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(3,3));
		buttonPanel = new ButtonPanel();
		contentPanel.add(buttonPanel, BorderLayout.EAST);
		contentPanel.add(gridPanel, BorderLayout.CENTER);
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
			gridPanel.add(nonetPanel);
		}
	}
	public void makeGrid() {
		contentPanel.remove(gridPanel);
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(3,3));
		contentPanel.add(gridPanel, BorderLayout.CENTER);
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
			gridPanel.add(nonetPanel);
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
		Color colour = PINK;
		List<Cage> adjacentCages = adjCages.get(cage);
		if(adjacentCages != null){

			boolean pinkUsed = false;
			boolean blueUsed = false;
			boolean yellowUsed = false;
			boolean greenUsed = false;
			for(Cage c : adjacentCages){
				Color adjColor = cageColourMap.get(c);
				if(adjColor != null){
					if(adjColor.equals(PINK)){
						pinkUsed = true;
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
				if(pinkUsed){
					colour = BLUE;
					if(blueUsed){
						colour = YELLOW;
						if(yellowUsed){
							colour = GREEN;
							if(greenUsed){
								colour = RED;
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
	public ButtonPanel getButtonPanel(){
		return buttonPanel;
	}
	public static void main(String[] args) throws FileNotFoundException{
		List<Cage> cages = CageParser.parseCages("example1.txt");
		SudokuGrid answer = AnswerParser.parseAnswer("example1Answer.txt");
		KillerSudokuGrid grid = new KillerSudokuGrid(cages);
		KillerSudokuSolver solver = new KillerSudokuSolver(grid);		
		SudokuGridUI gridUI = new SudokuGridUI(grid);
		gridUI.getButtonPanel().getSolveButton().addActionListener(new SolveListener(gridUI, solver));
		gridUI.getButtonPanel().getLoadButton().addActionListener(new LoadListener(gridUI, solver));
		solver.setPossibleCombinationsForCages();
		gridUI.makeGrid();
	}
}
