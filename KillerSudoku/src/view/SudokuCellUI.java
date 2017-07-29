package view;
import model.Location;
import model.SudokuCell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SudokuCellUI extends JPanel {
	private static final int SIZE = 9;
	private static final int CELL_SIZE = 70;
	private static final Font SOLVED_FONT = new Font("SansSerif", Font.BOLD, 30);
	private static final Font TOTAL_FONT = new Font("Serif", Font.BOLD, 15);
	private static final Font UNSOLVED_FONT = new Font("SansSerif", Font.PLAIN, 12);
	private static Border COMPOUND_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(18,0,0,0));
	private static Border BORDER = BorderFactory.createLineBorder(Color.black);
	private Location location;
	private SudokuCell cell;
	private Color colour;
	public SudokuCellUI(SudokuCell cell, Color colour){
		super(new GridLayout(3,3));
		this.cell = cell;
		this.colour = colour;
		location = cell.getLocation();
		init();
//		this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
		
	}
	public SudokuCellUI(SudokuCell cell, Color colour, int cageTotal){
		super(new GridLayout(3,3));
		this.cell = cell;
		this.colour = colour;
		location = cell.getLocation();
		init();
		Border TITLED_BORDER = BorderFactory.createCompoundBorder(BORDER, BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), ""+cageTotal, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT, TOTAL_FONT));
		this.setBorder(TITLED_BORDER);
		//		this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
		
	}
	public Dimension getPreferredSize(){
		return new Dimension(CELL_SIZE, CELL_SIZE);
	}
	private void init() {
		if(!cell.isSolved()){
			for(int i=1;i<=SIZE;i++){
				String s = ""+i;
				JLabel l = new JLabel(s);
				l.setFont(UNSOLVED_FONT);
				l.setHorizontalAlignment(JLabel.CENTER);
				if(!cell.getPossibleValues().contains(i)) l.setVisible(false);
				this.add(l);
			} 
			this.setBorder(COMPOUND_BORDER);
		}
		else{
			this.setLayout(new FlowLayout());
			String value = ""+ cell.getValue();
			JLabel l = new JLabel(value);
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setVerticalAlignment(JLabel.CENTER);
			l.setFont(SOLVED_FONT);
			this.add(l);
		}
		this.setBorder(COMPOUND_BORDER);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		super.setBackground(colour);
	}
	public SudokuCell getCell(){
		return cell;
	}
	public Location getCellLocation(){
		return location;
	}
}
