package view;
import model.Location;
import model.SudokuCell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class SudokuCellUI extends JPanel {
	private static final int SIZE = 9;
	private static final int CELL_SIZE = 50;
	private static final Font SOLVED_FONT = new Font("SansSerif", Font.BOLD, 30);
	private static final Font UNSOLVED_FONT = new Font("SansSerif", Font.PLAIN, 12);
	private Location location;
	private SudokuCell cell;
	public SudokuCellUI(SudokuCell cell){
		super(new GridLayout(3,3));
		this.cell = cell;
		location = cell.getLocation();
		paint();
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
		
	}
	private void paint() {
		if(!cell.isSolved()){
			for(int i=1;i<=SIZE;i++){
				String s = ""+i;
				JLabel l = new JLabel(s);
				l.setFont(UNSOLVED_FONT);
				l.setHorizontalAlignment(JLabel.CENTER);
				if(!cell.getPossibleValues().contains(i)) l.setVisible(false);
				this.add(l);
			} 
		}
		else{
			this.setLayout(new FlowLayout());
			String value = ""+ cell.getValue();
			JLabel l = new JLabel(value);
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setFont(SOLVED_FONT);
			this.add(l);
		}
	}
	public SudokuCell getCell(){
		return cell;
	}
	public Location getCellLocation(){
		return location;
	}
}
