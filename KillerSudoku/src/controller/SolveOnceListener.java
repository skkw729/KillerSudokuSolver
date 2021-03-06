package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.KillerSudokuSolver;
import model.Reason;
import view.ButtonPanel;
import view.SudokuCellUI;
import view.SudokuGridUI;

public class SolveOnceListener implements ActionListener{
	SudokuGridUI gridUI;
	KillerSudokuSolver solver;
	ButtonPanel buttonPanel;
	public SolveOnceListener(SudokuGridUI grid, KillerSudokuSolver solver, ButtonPanel buttonPanel){
		gridUI = grid;
		this.solver=solver;
		this.buttonPanel = buttonPanel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean helpOn = buttonPanel.getHelpCheckBox().isSelected();
		boolean strategyUsed = false;
		solver.setPossibleCombinationsForCages();
		solver.setPossibleValuesForCages();
		if(helpOn){
			gridUI.makeGrid();
		}
		Reason reason = solver.solveSingleValueCells();
		if(reason != null){
			if(buttonPanel.getHelpCheckBox().isSelected()){
				String s = reason.getMessage();
				JOptionPane.showMessageDialog(buttonPanel, s);
			}
			strategyUsed = true;
			gridUI.makeGrid();

		}
		else{
			reason = solver.solveCagesSpanningExtendedRegions();
			if(reason != null){

				if(buttonPanel.getHelpCheckBox().isSelected()){
					String s = reason.getMessage();
					JTextArea textArea = new JTextArea(s);
					JScrollPane scrollPane = new JScrollPane(textArea);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					scrollPane.setPreferredSize(new Dimension (200,150));
					JOptionPane.showMessageDialog(buttonPanel,scrollPane);
				}
				strategyUsed = true;
				gridUI.makeGrid();
			}
			else{
				reason = solver.solveAllAdjacentNonets();
				if(reason!=null){
					if(buttonPanel.getHelpCheckBox().isSelected()){
						String s = reason.getMessage();
						JTextArea textArea = new JTextArea(s);
						JScrollPane scrollPane = new JScrollPane(textArea);
						textArea.setLineWrap(true);
						textArea.setWrapStyleWord(true);
						scrollPane.setPreferredSize(new Dimension (200,150));
						JOptionPane.showMessageDialog(buttonPanel,scrollPane);
					}
					strategyUsed = true;
					gridUI.makeGrid();
				}
				else {
					solver.setPossibleCombinationsForCages();
					if(!solver.setPossibleCombinationsForCages().isEmpty())gridUI.makeGrid();
					else{
						List<Reason> reasons = solver.removeUniqueCageSumsFromRegions();
						if(!reasons.isEmpty()){
							if(buttonPanel.getHelpCheckBox().isSelected()){
								String s = "";
								for(Reason r : reasons){
									s += r.getMessage();
								}
								JTextArea textArea = new JTextArea(s);
								JScrollPane scrollPane = new JScrollPane(textArea);
								textArea.setLineWrap(true);
								textArea.setWrapStyleWord(true);
								scrollPane.setPreferredSize(new Dimension (200,150));
								//								gridUI.makeGrid();
								JOptionPane.showMessageDialog(buttonPanel,scrollPane);
							}
							strategyUsed = true;
							gridUI.makeGrid();
						}
						else{
							reason = solver.setSinglePositionCombinationAllCages();
							if(reason!=null) {
								if(buttonPanel.getHelpCheckBox().isSelected()){
									JOptionPane.showMessageDialog(buttonPanel, reason.getMessage());
								}
								strategyUsed = true;
								gridUI.makeGrid();
							}
						}
					}

				}
			}
		}
		if(!helpOn){
			List<SudokuCellUI> solvedCells = new ArrayList<>();
			for(SudokuCellUI cellUI : gridUI.getListUI()){
				if(cellUI.getCell().isSolved()) solvedCells.add(cellUI);
			}
			for(SudokuCellUI cellUI : gridUI.getListUI()){
				if(!solvedCells.contains(cellUI))cellUI.setBlank();
			}
		}
		if(solver.isSolved()) JOptionPane.showMessageDialog(buttonPanel, "This puzzle has been solved!");
		else if(!strategyUsed) JOptionPane.showMessageDialog(buttonPanel, "This program is unable to solve any further");
	}
}
