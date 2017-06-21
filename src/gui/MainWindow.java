package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import logic.Ograniczenie;
import logic.ProgramowanieLiniowe;


public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3880026026104218593L;
	
	private fCeluWindow fcelu;
	private OgraniczeniaWindow ograniczenia;
	
	private ProgramowanieLiniowe prog;
	private JLabel textFunkcjaCelu;
	private JButton edytujFunkcjeCelu;
	private JTextArea textOgraniczenia;
	private JButton dodajLubUsunOgraniczenie;
	
	

	public MainWindow(ProgramowanieLiniowe prog) {
		
		//Podstawowe ustawienia
		this.prog = prog;
		setSize(700, 500);
		setTitle("Programowanie Liniowe");
		setLayout(null);
		
		//Okno funkcji celu i ograniczen
		fcelu = new fCeluWindow(prog,this);
		ograniczenia = new OgraniczeniaWindow(prog,this);
		
		//Dodajemy menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuPlik = new JMenu("Plik");
		JMenu menuPomoc = new JMenu("Pomoc");
		JMenuItem menuEksportXML = new JMenuItem("Eksport do XML");
		JMenuItem menuImportXML = new JMenuItem("Import z XML");
		JMenuItem menuOProgramie = new JMenuItem("O programie");
		
		setJMenuBar(menuBar);
		menuBar.add(menuPlik);
		menuBar.add(menuPomoc);
		menuPlik.add(menuEksportXML);
		menuPlik.add(menuImportXML);
		menuPomoc.add(menuOProgramie);
		
		//labelFunkcjaCelu
		JLabel labelFunkcjaCelu = new JLabel("<html><center>FUNKCJA CELU:</center></html>");
		labelFunkcjaCelu.setBounds(20, 20, 300, 20);
		add(labelFunkcjaCelu);
		
		//TEXTFIELD textFunkcjaCelu
		textFunkcjaCelu = new JLabel("Funkcja Celu niezdefiniowana");
		textFunkcjaCelu.setBounds(20,40,300,20);
		add(textFunkcjaCelu);
		
		//BUTTON edytujFunkcjeCelu
		edytujFunkcjeCelu = new JButton("Edytuj Funkcje Celu");
		edytujFunkcjeCelu.setBounds(20,70,300,50);
		edytujFunkcjeCelu.addActionListener(this);
		add(edytujFunkcjeCelu);
		
		//labelOgraniczenia
		JLabel labelOgraniczenia = new JLabel("OGRANICZENIA:");
		labelOgraniczenia.setBounds(20, 140, 300, 20);
		add(labelOgraniczenia);
		
		//TEXTAREA textOgraniczenia
		textOgraniczenia = new JTextArea();
		textOgraniczenia.setEditable(false);
		textOgraniczenia.setText("Wprowadz Ograniczenia");
		JScrollPane scrollPane = new JScrollPane(textOgraniczenia);
		scrollPane.setBounds(20, 160, 300, 200);
		add(scrollPane);
		
		//BUTTON dodajOgraniczenie
		dodajLubUsunOgraniczenie = new JButton("Dodaj lub Usun Ogranicznia");
		dodajLubUsunOgraniczenie.setBounds(20,370,300,50);
		dodajLubUsunOgraniczenie.addActionListener(this);
		add(dodajLubUsunOgraniczenie);
		
	}
	
	

	public JLabel getTextFunkcjaCelu() {
		return textFunkcjaCelu;
	}

	public JButton getEdytujFunkcjeCelu() {
		return edytujFunkcjeCelu;
	}

	public JTextArea getTextOgraniczenia() {
		return textOgraniczenia;
	}

	public JButton getDodajLubUsunOgraniczenie() {
		return dodajLubUsunOgraniczenie;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(dodajLubUsunOgraniczenie)) {
			ograniczenia.setVisible(true);
			ograniczenia.updateOgraniczenia();
		}
		if(e.getSource().equals(edytujFunkcjeCelu)) {
			fcelu.setVisible(true);
		}
		
	}
	
	public void updateFunkcjaCelu() {
		
	}
	
	public void updateOgraniczenia() {
		List<Ograniczenie> ograniczenia = prog.getListaOgraniczen();
		String lista = "";
		for(int i=3; i<ograniczenia.size(); i++) {
			lista += ograniczenia.get(i).toString() + "\n";
		}
		this.textOgraniczenia.setText(lista);
	}

}
