package controller;

import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.KillerSudokuSolver;
import model.Reason;
import model.SudokuCell;
import view.ButtonPanel;
import view.SudokuGridUI;

public class SolveListener implements ActionListener{
	private SudokuGridUI gridUI;
	private KillerSudokuSolver solver;
	List<Reason> reasons;
	public SolveListener(SudokuGridUI gridUI, KillerSudokuSolver solver){
		this.gridUI = gridUI;
		this.solver = solver;
		reasons = new ArrayList<>();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		solver.setPossibleValuesForCages();
		solver.solveSingleValueCells();
		int i=0;
		boolean running = true;
		while(!solver.isSolved() && running){
			Reason reason = solver.solveCagesSpanningExtendedRegions();
			if(reason != null){
				String s = reason.getMessage();
				gridUI.makeGrid();
				JOptionPane.showMessageDialog(null, s);
				
			}
			else{
				reason = solver.solveAllAdjacentNonets();
				if(reason!=null){
					String s = reason.getMessage();
					gridUI.makeGrid();
					JOptionPane.showMessageDialog(null, s);
					
				}
				else{
					reason = solver.solveSingleValueCells();
					if(reason != null){
						String s = reason.getMessage();
						gridUI.makeGrid();
						JOptionPane.showMessageDialog(null, s);
						
					}
					
						
						else {
							solver.setPossibleCombinationsForCages();
							if(!solver.setPossibleCombinationsForCages().isEmpty())gridUI.makeGrid();
							else{
								reasons = solver.removeUniqueCageSumsFromRegions();
								if(!reasons.isEmpty()){
									String s = "";
									for(Reason r : reasons){
										s += r.getMessage();
									}
									JTextArea textArea = new JTextArea(s);
									JScrollPane scrollPane = new JScrollPane(textArea);
									textArea.setLineWrap(true);
									textArea.setWrapStyleWord(true);
									scrollPane.setPreferredSize(new Dimension (500,500));
									gridUI.makeGrid();
									JOptionPane.showMessageDialog(null,scrollPane);
									
								}
								else{
								running = false;

							}
						}

					}


				}
				

				i++;	
			}
		}
	}
}
