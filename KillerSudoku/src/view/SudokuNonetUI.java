package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.SudokuCell;

public class SudokuNonetUI extends JPanel {
	private static final int SIZE = 9;
	
	public SudokuNonetUI(List<SudokuCell> nonet){
		super(new GridLayout(3,3));
		for(SudokuCell cell : nonet){
			this.add(new SudokuCellUI(cell));
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}
	}
}
