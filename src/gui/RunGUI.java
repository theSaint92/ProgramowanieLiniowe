package gui;
import javax.swing.JFrame;

import logic.ProgramowanieLiniowe;

public class RunGUI {


	public static void main(String[] args) {
		
		ProgramowanieLiniowe prog = new ProgramowanieLiniowe();
		MainWindow okno = new MainWindow(prog);
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okno.setVisible(true);
	}

}
