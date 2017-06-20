package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import logic.ProgramowanieLiniowe;

public class fCeluWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 19097451244151406L;
	
	MainWindow main;
	ProgramowanieLiniowe prog;
	private JButton zatwierdz;
	private JTextField textFieldA;
	private JTextField textFieldB;
	private ButtonGroup grupaCel;
	private JRadioButton radioCelMin; //4.26
	private JRadioButton radioCelMax; //5.32

	

	public fCeluWindow(ProgramowanieLiniowe prog, MainWindow main) {
		
		this.prog = prog;
		this.main = main;

		setSize(300, 230);
		setTitle("Edycja Funkcji Celu");
		setLayout(null);
		
		//TEKST GLOWNY
		String text1 = "<html><center>Wprowadz wspolczynniki A i B funkcji celu<br>" +
				"f(x,y) = Ax + By </center></html>";
		JLabel labelFunkcjaCelu = new JLabel(text1);
		labelFunkcjaCelu.setBounds(20, 20, 250, 30);
		add(labelFunkcjaCelu);
		
		//WYBOR A
		JLabel labelA = new JLabel("   A");
		labelA.setBounds(20, 60, 30, 20);
		add(labelA);
		
		textFieldA = new JTextField();
		textFieldA.setBounds(50,60,80,20);
		textFieldA.setToolTipText("Podaj wartosc A");
		add(textFieldA);
		
		//WYBOR B
		JLabel labelB = new JLabel("   B");
		labelB.setBounds(20, 80, 30, 20);
		add(labelB);
		
		textFieldB = new JTextField();
		textFieldB.setBounds(50,80,80,20);
		textFieldB.setToolTipText("Podaj wartosc B");
		add(textFieldB);
		
		//WYBOR CELU
		JLabel labelCel = new JLabel("Cel");
		labelCel.setBounds(20, 100, 30, 20);
		add(labelCel);
		
		radioCelMin = new JRadioButton("min", true);
		radioCelMin.setBounds(50, 100, 50, 20);
		//radioCelMin.addActionListener(this);
		add(radioCelMin);
		
		radioCelMax = new JRadioButton("max", false);
		radioCelMax.setBounds(100, 100, 50, 20);
		//radioCelMin.addActionListener(this);
		add(radioCelMax);
		
		grupaCel = new ButtonGroup();
		grupaCel.add(radioCelMin);
		grupaCel.add(radioCelMax);

		
		
		zatwierdz = new JButton("Zatwierdz");
		zatwierdz.setBounds(20,130,250,50);
		zatwierdz.addActionListener(this);
		add(zatwierdz);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(zatwierdz)) {
			String message = "";
			double a=0,b=0;
			try {
				a = Double.parseDouble(textFieldA.getText());
			} catch (NumberFormatException exc) {
				message += "\n Nieprawidlowy Format wspolczynnika A";
			}
			try {
				b = Double.parseDouble(textFieldB.getText());
			} catch (NumberFormatException exc) {
				message += "\n Nieprawidlowy Format wspolczynnika B";
			}
			if (a == 0 && b == 0) {
				message += "\n A i B jednoczesnie nie moga byc zerami";
			}
			if (message.length() == 0 && (a!=0 || b!= 0)) {
				String cel = "min";
				if (radioCelMax.isSelected()) {
					cel = "max";
				}
				prog.editFunkcjaCelu(a, b, cel);
				
				main.getTextFunkcjaCelu().setText(prog.getFunkcjaCelu().toString());
				this.setVisible(false);
			}
			else {
				JOptionPane.showMessageDialog(null, message,
						"Niepoprawne dane", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		
	}

}
