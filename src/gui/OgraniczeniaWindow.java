package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import logic.Ograniczenie;
import logic.ProgramowanieLiniowe;

public class OgraniczeniaWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 5795001888695851349L;
	
	ProgramowanieLiniowe prog;
	MainWindow main;
	
	private JList<Ograniczenie> listaOgraniczen;
	private DefaultListModel<Ograniczenie> listaOgraniczenModel;
	private JTextField textFieldA;
	private JTextField textFieldB;
	private JTextField textFieldC;
	private JComboBox<String> znak;
	private JButton dodajOgraniczenie;
	private JButton usunZaznaczone;
	private JButton zatwierdz;

	public OgraniczeniaWindow(ProgramowanieLiniowe prog, MainWindow main)  {
		
		this.prog = prog;
		this.main = main;
		
		setSize(480, 400);
		setTitle("Edycja Ograniczen");
		setLayout(null);
		
		
		//Napis Lista Ograniczen
		JLabel labelListaOgraniczen = new JLabel("Lista Ograniczen:");
		labelListaOgraniczen.setBounds(20, 20, 200, 30);
		add(labelListaOgraniczen);
		
		//Wyswietlanie Listy ograniczen
		listaOgraniczenModel = new DefaultListModel<Ograniczenie>();
		listaOgraniczen = new JList<Ograniczenie>(listaOgraniczenModel);
		listaOgraniczen.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listaOgraniczen.setLayoutOrientation(JList.VERTICAL);
		listaOgraniczen.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(listaOgraniczen);
		//listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setBounds(20,50,200,210);
		add(listScroller);
		
		//Button UsunZaznaczone
		usunZaznaczone = new JButton("Usun Zaznaczone");
		usunZaznaczone.setBounds(20,280,200,50);
		usunZaznaczone.addActionListener(this);
		add(usunZaznaczone);
		
		//Napis dodawanie ograniczenia
		JLabel labelDodawanieOgraniczenia = new JLabel("Dodawanie ograniczenia");
		labelDodawanieOgraniczenia.setBounds(240, 20, 200, 30);
		add(labelDodawanieOgraniczenia);
		
		//Dodawanie ograniczenia 
		textFieldA = new JTextField();
		textFieldA.setBounds(240,50,40,20);
		textFieldA.setToolTipText("Podaj wartosc A");
		add(textFieldA);
		
		JLabel labelXPlus = new JLabel("x +");
		labelXPlus.setBounds(280, 50, 20, 20);
		add(labelXPlus);
		
		textFieldB = new JTextField();
		textFieldB.setBounds(300,50,40,20);
		textFieldB.setToolTipText("Podaj wartosc B");
		add(textFieldB);
		
		JLabel labelY = new JLabel("y");
		labelY.setBounds(340, 50, 20, 20);
		add(labelY);
		
		znak = new JComboBox<String>();
		znak.setBounds(350, 50, 40, 20);
		znak.addItem("<html>&le</html>");
		znak.addItem("=");
		znak.addItem("<html>&ge</html>");
		add(znak);
		
		textFieldC = new JTextField();
		textFieldC.setBounds(400,50,40,20);
		textFieldC.setToolTipText("Podaj wartosc C");
		add(textFieldC);
		
		dodajOgraniczenie = new JButton("Dodaj Ograniczenie");
		dodajOgraniczenie.setBounds(240,80,200,50);
		dodajOgraniczenie.addActionListener(this);
		add(dodajOgraniczenie);
	
		
		zatwierdz = new JButton("Zatwierdz");
		zatwierdz.setBounds(240,280,200,50);
		zatwierdz.addActionListener(this);
		add(zatwierdz);
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(usunZaznaczone)) {
		    // get a list with selected objects
		    List<Ograniczenie> selectedItems = listaOgraniczen.getSelectedValuesList();

		    for (Ograniczenie o: selectedItems) {
		        listaOgraniczenModel.removeElement(o);
		    }
		}
		if(e.getSource().equals(dodajOgraniczenie)) {
			String message = "";
			double a=0,b=0,c=0;
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
			try {
				c = Double.parseDouble(textFieldC.getText());
			} catch (NumberFormatException exc) {
				message += "\n Nieprawidlowy Format wspolczynnika C";
			}
			if (a == 0 && b == 0) {
				message += "\n A i B jednoczesnie nie moga byc zerami";
			}
			if (message.length() == 0 && (a!=0 || b!= 0)) {
				String wybranyZnak = "<=";
				if (znak.getSelectedIndex() == 0) wybranyZnak = "<=";
				else if (znak.getSelectedIndex() == 1) wybranyZnak = "=";
				else if (znak.getSelectedIndex() == 2) wybranyZnak = ">=";
				listaOgraniczenModel.addElement(new Ograniczenie(a,b,wybranyZnak,c));
			}
			else {
				JOptionPane.showMessageDialog(null, message,
						"Niepoprawne dane", JOptionPane.WARNING_MESSAGE);
			}
		}	
		if(e.getSource().equals(zatwierdz)) {
			prog.clearOgraniczenia();
		    for (int i=0; i<listaOgraniczenModel.size(); i++) {
		        prog.addOgraniczenieByObject(listaOgraniczenModel.getElementAt(i));
		    }
		    main.updateOgraniczenia();
		    this.setVisible(false);
		}
	}
	
	public void updateOgraniczenia() {
		listaOgraniczenModel.clear();
		List<Ograniczenie> lista = prog.getListaOgraniczen();
		for (int i = 4; i<lista.size(); i++) {
			listaOgraniczenModel.addElement(lista.get(i));
		}
	}

}
