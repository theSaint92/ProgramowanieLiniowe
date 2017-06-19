package logic;

import java.util.ArrayList;
import java.util.List;

public class ProgramowanieLiniowe {

	private List<Ograniczenie> listaOgraniczen;
	private FunkcjaCelu funkcjaCelu;
	//Wyniki
	private double pointX;
	private double pointY;
	private double bestValue;
	
	
	
	public ProgramowanieLiniowe() {
		super();
		this.listaOgraniczen = new ArrayList<Ograniczenie>();
		
		//Ograniczenia duzych liczb
		listaOgraniczen.add(new Ograniczenie(1.0,0.0,"<=",100000000));
		listaOgraniczen.add(new Ograniczenie(1.0,0.0,">=",-100000000));		
		listaOgraniczen.add(new Ograniczenie(0.0,1.0,"<=",100000000));
		listaOgraniczen.add(new Ograniczenie(0.0,1.0,">=",-100000000));
		
		this.funkcjaCelu = null;
	}
	
	public int addOgraniczenie(double x1, double x2, String znak, double ograniczenie) {
		int result = 1;
		if (znak.equals("=") || znak.equals("<=") || znak.equals(">=")) {
			listaOgraniczen.add(new Ograniczenie(x1,x2,znak,ograniczenie));
			result = 0;
		}
		return result;
	}
	
	public int delOgraniczenie(int i) {
		int result = 1;
		if (i>=0 && i<listaOgraniczen.size()) {
			listaOgraniczen.remove(i);
			result = 0;
		}
		return result;
	}
	
	public int editFunkcjaCelu(double x1, double x2, String cel) {
		int result = 1;
		if (cel.equals("min") || cel.equals("min")) {
			this.funkcjaCelu = new FunkcjaCelu(x1,x2,cel);
			result = 0;
		}
		return result;
	}
	
	public String getResult() {
		String result = "";
		
		//Wyznaczamy zbior punktow przeciecia ograniczen
		//2 Proste sa rownolegle i nigdy sie nie przetna, jezeli
		//A1*B2 = A2*B1 - dla takich 2 tych punktow nie wyznaczymy
		List<Double> x = new ArrayList<Double>();
		List<Double> y = new ArrayList<Double>();
		
		for (int i = 0; i < listaOgraniczen.size(); i++) {
			for(int j = i+1; j<listaOgraniczen.size(); j++) {
				double A1 = listaOgraniczen.get(i).getX1();
				double B1 = listaOgraniczen.get(i).getX2();
				double A2 = listaOgraniczen.get(j).getX1();
				double B2 = listaOgraniczen.get(j).getX2();
				double C1 = listaOgraniczen.get(i).getOgraniczenie();
				double C2 = listaOgraniczen.get(j).getOgraniczenie();
				//Warunek sprawdzajacy czy proste sie przetna
				double wyznacznik = A1*B2 - A2*B1;
				if (wyznacznik != 0) {
					double wyznacznikX = C1*B2 - C2*B1;
					double wyznacznikY = A1*C2 - A2*C1;
					x.add(wyznacznikX/wyznacznik);
					y.add(wyznacznikY/wyznacznik);
					System.out.println("DODANO PUNKT [ " + wyznacznikX/wyznacznik + " , " + wyznacznikY/wyznacznik + " ]");
				}
			}
		}
		
		
		//Teraz przefiltrujemy te punkty dla ktorych nie wszystkie ograniczenia sa spelnione
		for (int i=0; i<x.size() ; i++) {
			for (int j=0; j < listaOgraniczen.size(); j++) {
				if (!listaOgraniczen.get(j).isConditionTrue(x.get(i), y.get(i))) {
					x.remove(i);
					y.remove(i);
					i -= 1;
					break;
				}
			}
		}
		
		for (int i=0; i<x.size() ; i++) {
			System.out.println("ZOSTAL PUNKT[ " +x.get(i) + " , " + y.get(i) + " ]");
		}
		
		
		return result;
	}


}
