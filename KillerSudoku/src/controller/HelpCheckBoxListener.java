package controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import view.SudokuCellUI;
import view.SudokuGridUI;

public class HelpCheckBoxListener implements ItemListener {
	SudokuGridUI gridUI;
	public HelpCheckBoxListener(SudokuGridUI gridUI){
		this.gridUI = gridUI;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange()==ItemEvent.DESELECTED){
			for(SudokuCellUI cellUI: gridUI.getListUI()){
				if(!cellUI.getCell().isSolved()) cellUI.setBlank();
			}
			gridUI.rePaint();
		}
		else if(e.getStateChange()==ItemEvent.SELECTED){
			gridUI.makeGrid();
		}
		
	}
}
