package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import logic.Ograniczenie;
import logic.ProgramowanieLiniowe;


public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3880026026104218593L;
	
	JMenuItem menuEksportXML;
	JMenuItem menuImportXML;
	JMenuItem menuOProgramie;
	
	private fCeluWindow fcelu;
	private OgraniczeniaWindow ograniczenia;
	
	private ProgramowanieLiniowe prog;
	private JLabel textFunkcjaCelu;
	private JButton edytujFunkcjeCelu;
	private JTextArea textOgraniczenia;
	private JButton dodajLubUsunOgraniczenie;
	private JButton rozwiaz;
	private Rysunek rys;

	private JLabel labelRozwiazanie2;
	
	

	public MainWindow(ProgramowanieLiniowe prog) {
		
		//Podstawowe ustawienia
		this.prog = prog;
		setSize(680, 510);
		setTitle("Programowanie Liniowe");
		setLayout(null);
		
		//Okno funkcji celu i ograniczen
		fcelu = new fCeluWindow(prog,this);
		ograniczenia = new OgraniczeniaWindow(prog,this);
		
		//Dodajemy menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuPlik = new JMenu("Plik");
		JMenu menuPomoc = new JMenu("Pomoc");
		menuEksportXML = new JMenuItem("Eksport do XML");
		menuImportXML = new JMenuItem("Import z XML");
		menuOProgramie = new JMenuItem("O programie");
		
		setJMenuBar(menuBar);
		menuBar.add(menuPlik);
		menuBar.add(menuPomoc);
		menuPlik.add(menuEksportXML);
		menuPlik.add(menuImportXML);
		menuPomoc.add(menuOProgramie);
	
		
		menuEksportXML.addActionListener(this);
		menuImportXML.addActionListener(this);
		menuOProgramie.addActionListener(this);
		
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
		scrollPane.setBounds(20, 160, 300, 210);
		add(scrollPane);
		
		//BUTTON dodajOgraniczenie
		dodajLubUsunOgraniczenie = new JButton("Dodaj lub Usun Ogranicznia");
		dodajLubUsunOgraniczenie.setBounds(20,380,300,50);
		dodajLubUsunOgraniczenie.addActionListener(this);
		add(dodajLubUsunOgraniczenie);
		
		//labelFunkcjaCelu
		JLabel labelRozwiazanie = new JLabel("<html><center>Rozwiazanie:</center></html>");
		labelRozwiazanie.setBounds(340, 20, 300, 20);
		add(labelRozwiazanie);
		
		labelRozwiazanie2 = new JLabel("");
		labelRozwiazanie2.setBounds(340, 40, 300, 20);
		add(labelRozwiazanie2);
		
		//BUTTON dodajOgraniczenie
		rozwiaz = new JButton("Rozwiaz");
		rozwiaz.setBounds(340,380,300,50);
		rozwiaz.addActionListener(this);
		add(rozwiaz);
		
		//Rysunek
		rys = new Rysunek(prog.rysujZbiorDopuszczalny(300));
		rys.setBounds(340, 70, 300, 300);
		add(rys);
		
		
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
		
		if(e.getSource().equals(menuImportXML)){
			JFileChooser fileChooser = new JFileChooser("./");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML FILES", "xml", "xml");
			fileChooser.setFileFilter(filter);
			if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File myFile = fileChooser.getSelectedFile();
			prog.importFromXML(myFile.getPath());
			JOptionPane.showMessageDialog(null, "Plik "
			+ myFile.getName()
			+ " zaimportowany poprawanie!");
			} 
			this.updateOgraniczenia();
			getTextFunkcjaCelu().setText(prog.getFunkcjaCelu().toString());

		}
		
		if(e.getSource().equals(menuEksportXML)){
			JFileChooser fileChooser = new JFileChooser("./");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML FILES", "xml", "xml");
			fileChooser.setFileFilter(filter);
			if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File myFile = fileChooser.getSelectedFile();
			if (!myFile.getPath().endsWith(".xml")) {
				prog.exportToXML(myFile.getPath() + ".xml");
			}
			JOptionPane.showMessageDialog(null, "Poprawnie zapisano plik " + myFile.getName() + ".xml");
			}

		}
		
		if(e.getSource().equals(menuOProgramie)){
			JOptionPane.showMessageDialog(null, "Autor: Adrian Grzelak",
					"O programie", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(e.getSource().equals(rozwiaz)){
			labelRozwiazanie2.setText(prog.getResult());
			
			rys.setImage(prog.rysujZbiorDopuszczalny(300));
			//rys = new Rysunek(prog.rysujZbiorDopuszczalny(300));
			//rys.setBounds(340, 70, 300, 300);
			rys.repaint();
			//add(rys);
			this.repaint();
		}
		
	}

	
	public void updateOgraniczenia() {
		List<Ograniczenie> ograniczenia = prog.getListaOgraniczen();
		String lista = "";
		for(int i=4; i<ograniczenia.size(); i++) {
			lista += ograniczenia.get(i).toString() + "\n";
		}
		if (lista == "") lista = "Wprowadz Ograniczenia";
		this.textOgraniczenia.setText(lista);
	}

}
