package logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ProgramowanieLiniowe {

	private static final double TOOBIGVALUE = 100000000;
	private List<Ograniczenie> listaOgraniczen;
	private FunkcjaCelu funkcjaCelu;
	//Wyniki
	List<Double> x;
	List<Double> y;
	private double pointX;
	private double pointY;
	private double bestValue;
	//private BufferedImage I;

	
	public ProgramowanieLiniowe() {
		super();
		clearOgraniczenia();
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
	
	public void addOgraniczenieByObject(Ograniczenie o) {
		listaOgraniczen.add(o);
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
		if (cel.equals("min") || cel.equals("max")) {
			this.funkcjaCelu = new FunkcjaCelu(x1,x2,cel);
			result = 0;
		}
		return result;
	}
	
	public String getResult() {
		
		String result = "Nieznany blad";
		
		//Sprawdzamy czy funkcjaCelu jest zdefiniowana
		if (funkcjaCelu == null) {
			return "Funkcja celu niezdefiniowana!";
		}
		
		if (listaOgraniczen.size() <= 4) {
			return "Nie zdefiniowana ograniczen!";
		}
		
		//Wyznaczamy zbior punktow przeciecia ograniczen
		//2 Proste sa rownolegle i nigdy sie nie przetna, jezeli
		//A1*B2 = A2*B1 - dla takich 2 tych punktow nie wyznaczymy
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();
		
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
					//System.out.println("DODANO PUNKT [ " + wyznacznikX/wyznacznik + " , " + wyznacznikY/wyznacznik + " ]");
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
		
		//Przefiltrujemy jeszcze "powtórki"
		for (int i=0; i<x.size() ; i++) {
			for (int j=i+1; j < x.size(); j++) {
				if ((x.get(i) == x.get(j)) && (y.get(i) == y.get(j))) {
					x.remove(j);
					y.remove(j);
					j -= 1;
					break;
				}
			}
		}
		
		for (int i=0; i<x.size() ; i++) {
		 System.out.println("ZOSTAL PUNKT [ " +x.get(i) + " , " + y.get(i) + " ]");
		}
		
		//Sprawdzenie czy uklad nie jest sprzeczny
		if (x.size() == 0) {
			return "Uklad sprzeczny";
		}
		
	
		if (funkcjaCelu.getCel().equals("min")) {
			bestValue = Double.MAX_VALUE;
			for (int i=0; i<x.size(); i++) {
				double temp = funkcjaCelu.valueOf(x.get(i), y.get(i));
				if(temp < bestValue) {
					bestValue = temp;
					pointX = x.get(i);
					pointY = y.get(i);
					result = "f( " + pointX + " , " + pointY + " ) = " + bestValue;
				}
			}
			
		}
		if (funkcjaCelu.getCel().equals("max")) {
			bestValue = -Double.MAX_VALUE;
			for (int i=0; i<x.size(); i++) {
				double temp = funkcjaCelu.valueOf(x.get(i), y.get(i));
				if(temp > bestValue) {
					bestValue = temp;
					pointX = x.get(i);
					pointY = y.get(i);
					result = "f( " + pointX + " , " + pointY + " ) = " + bestValue;
				}
			}
		}
		
		//Sprawdzenie czy uklad nie jest tozsamosciowy
		if (pointX == TOOBIGVALUE || pointY == TOOBIGVALUE || pointX == -TOOBIGVALUE || pointY == -TOOBIGVALUE) {
			return "Uklad tozsamosciowy";
		}
		
		return result;
		
	}

	public BufferedImage rysujZbiorDopuszczalny(int size) {
		BufferedImage I = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = I.createGraphics();
		final int BORDER_GAP = 20;
		
		/* ==========================
	     * Zakres w jakim rysujemy
	     * ==========================
	     */
		
		double minX;
		double maxX;
		double minY;
		double maxY;
		
		if (x != null) {
			if (x.size() != 0) {
				minX = Double.MAX_VALUE;
				maxX = -Double.MAX_VALUE;
				minY = Double.MAX_VALUE;
				maxY = -Double.MAX_VALUE;
				for(int i=0; i<x.size(); i++) {
					if (x.get(i) < minX) { //&& Math.abs(x.get(i)) != TOOBIGVALUE ) {
						minX = x.get(i);
					}
					if (x.get(i) > maxX) { //&& Math.abs(x.get(i)) != TOOBIGVALUE) {
						maxX = x.get(i);
					}
					if (y.get(i) < minY) { //&& Math.abs(y.get(i)) != TOOBIGVALUE) {
						minY = y.get(i);
					}
					if (y.get(i) > maxY) { //&& Math.abs(y.get(i)) != TOOBIGVALUE) {
						maxY = y.get(i);
					}
				
					System.out.println( x.get(i) + " " + (x.get(i) > maxX) + " > " + maxX);
				}
				double wideX = maxX - minX;
				double wideY = maxY - minY;
				
				maxX += wideX/5;
				minX -= wideX/5;
				maxY += wideY/5;
				minY -= wideY/5;
			}
			else {
				minX = -5;
				maxX = 5;
				minY = -5;
				maxY = 5;
			}
			
		}
		else {
			minX = -5;
			maxX = 5;
			minY = -5;
			maxY = 5;
		}
		
		if (minX <= -TOOBIGVALUE) minX =-TOOBIGVALUE;
		if (maxX >= TOOBIGVALUE) maxX = TOOBIGVALUE;
		if (minY <= -TOOBIGVALUE) minY =-TOOBIGVALUE;
		if (maxY >= TOOBIGVALUE) maxY = TOOBIGVALUE;
		
		if (minX == maxX) {
			minX = minX - 1;
			maxX = maxX + 1;
		}
		
		if (minY == maxY) {
			minY = minY - 1;
			maxY = maxY + 1;
		}
		
		System.out.println("minX " + minX);
		System.out.println("maxX " + maxX);
		System.out.println("minY" + minY);
		System.out.println("maxY " + maxY);
		
			
		
		/* ==========================
	     * Rysowanie ograniczen
	     * ==========================
	     */
		Random rand = new Random();
		int color = rand.nextInt(16777215);

		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int n = 0;
				for (byte i = 0; i < listaOgraniczen.size(); i++) {
					double x1 = convertToDoublePoints(x,0,size,minX,maxX);
					double x2 = convertToDoublePoints(y,0,size,minY,maxY);
					if (!listaOgraniczen.get(i).isConditionTrue(x1, x2)) {
						n+=1;
					}
				}
				if(n==0) I.setRGB(x, size - y-1, color);
			}
		}
		
		System.out.println(convertToDoublePoints(5,0,size,minX,maxX));
        
		
		/* ===================
	     * CREATE X AND Y AXES
	     * ===================
	     */
		
		g.setColor(Color.WHITE);
		if(minY*maxY >= 0) { //Jezeli nie ma srodka w ukladzie to rysujemy oske na boku
			g.drawLine(BORDER_GAP,size-BORDER_GAP,size-BORDER_GAP,size - BORDER_GAP);
			
			double width = maxX - minX;
			double denomination = findDenomination(width);
			double start = Math.ceil(minX * 1/denomination)*denomination;
			
		    for (double i = start; i < maxX; i+=denomination) {
		    	int point = convertToPixelCoords(i, minX, maxX, 0, size);
		    	if (point >= BORDER_GAP && point <= size-BORDER_GAP) {
		    		g.drawLine(point, size - BORDER_GAP, point, size - BORDER_GAP - 10);
		    		if (i!=0.0)
		    			g.drawString(Double.toString(i), point, size - BORDER_GAP - 15);
		    	}
		    }
		}
		else { //Oske X rysujemy tam gdzie sie y zeruje czyli
			int p = size - convertToPixelCoords(0.0, minY, maxY, 0, size);
			g.drawLine(BORDER_GAP,p,size-BORDER_GAP,p);
			
			double width = maxX - minX;
			double denomination = findDenomination(width);
			double start = Math.ceil(minX * 1/denomination)*denomination;
			
		    for (double i = start; i < maxX; i+=denomination) {
		    	int point = convertToPixelCoords(i, minX, maxX, 0, size);
		    	if (point >= BORDER_GAP && point <= size-BORDER_GAP) {
		    		g.drawLine(point, p-10, point, p+10);
		    		if (i!=0.0)
		    			g.drawString(Double.toString(i), point,  p - 15);
		    	}
		    }
			
			
		}
		if(minX*maxX >= 0) {
			g.drawLine(BORDER_GAP,BORDER_GAP,BORDER_GAP,size - BORDER_GAP);
			
			double height = maxY - minY;
			double denomination = findDenomination(height);
			double start = Math.ceil(minY * 1/denomination)*denomination;
			
		    for (double i = start; i < maxY; i+=denomination) {
		    	int point = size - convertToPixelCoords(i, minY, maxY, 0, size);
		    	if (point >= BORDER_GAP && point <= size-BORDER_GAP) {
		    		g.drawLine(BORDER_GAP, point, BORDER_GAP + 10, point);
		    		if (i!=0.0)
		    			g.drawString(Double.toString(i), BORDER_GAP + 15, point);
		    	}
		    }
		}
		else {
			int p = convertToPixelCoords(0.0, minX, maxX, 0, size);
			g.drawLine(p,BORDER_GAP,p,size-BORDER_GAP);
			
			double height = maxY - minY;
			double denomination = findDenomination(height);
			double start = Math.ceil(minY * 1/denomination)*denomination;
			
			for (double i = start; i < maxY; i+=denomination) {
		    	int point = size - convertToPixelCoords(i, minY, maxY, 0, size);
		    	if (point >= BORDER_GAP && point <= size-BORDER_GAP) {
		    		g.drawLine(p-10, point, p + 10, point);
		    		if (i!=0.0)
		    			g.drawString(Double.toString(i), p + 15, point);
		    	}
		    }
		}
		
		if (x != null) {
			if (x.size() == 1) {
				g.setColor(Color.YELLOW);
				int xxx = convertToPixelCoords(x.get(0),minX,maxX,0,size);
				int yyy = convertToPixelCoords(y.get(0),minY,maxY,0,size);
				g.fill(new Ellipse2D.Double(xxx - 2.5, size - yyy - 2.5, 5, 5));
			}
		}
		
		if (x != null) {
			if (x.size() == 2) {
				g.setColor(Color.YELLOW);
				int xxx1 = convertToPixelCoords(x.get(0),minX,maxX,0,size);
				int yyy1 = convertToPixelCoords(y.get(0),minY,maxY,0,size);
				int xxx2 = convertToPixelCoords(x.get(1),minX,maxX,0,size);
				int yyy2 = convertToPixelCoords(y.get(1),minY,maxY,0,size);
				g.drawLine(xxx1, yyy1, xxx2, yyy2);
			}
		}
	
		return I;
 
	}

	private double findDenomination(double width) {
		double result = 1.0;
		
		int iteracje = 0;
		
		while (!(result*5 < width)) {
			if (iteracje%2 == 0) result /= 2.0;
			else if(iteracje%2 ==1) result /= 5.0;
			iteracje++;
			//result /= 10.0;
			
		}
		while (!(result*50 > width)) {
			if (iteracje%2 == 0) result *= 2.0;
			else if(iteracje%2 ==1) result *= 5.0;
			iteracje++;
			//result *= 10.0;
		}

		return result;
	}
	
	private int convertToPixelCoords(double value, double minDouble, double maxDouble, int minInt, int maxInt) {
		int result;
		
		double maxWidthDouble = maxDouble - minDouble;
		double maxWidthInt = maxInt - minInt;
		
		double widthDouble = value - minDouble;
		
		result = (int)Math.round(widthDouble * maxWidthInt/maxWidthDouble);
		
		return result;
	}
	
	private double convertToDoublePoints(int value, int minInt, int maxInt, double minDouble, double maxDouble) {
		double result;
		
		double maxWidthDouble = maxDouble - minDouble;
		double maxWidthInt = maxInt - minInt;
		
		double widthInt = value - minInt;
		
		result = minDouble + widthInt * (maxWidthDouble/maxWidthInt);
		
		return result;
	}
	
	public FunkcjaCelu getFunkcjaCelu() {
		return funkcjaCelu;
	}

	public List<Ograniczenie> getListaOgraniczen() {
		return listaOgraniczen;
	}
	
	public void clearOgraniczenia() {
		this.listaOgraniczen = new ArrayList<Ograniczenie>();
		
		//Ograniczenia duzych liczb
		listaOgraniczen.add(new Ograniczenie(1.0,0.0,"<=",TOOBIGVALUE));
		listaOgraniczen.add(new Ograniczenie(1.0,0.0,">=",-TOOBIGVALUE));		
		listaOgraniczen.add(new Ograniczenie(0.0,1.0,"<=",TOOBIGVALUE));
		listaOgraniczen.add(new Ograniczenie(0.0,1.0,">=",-TOOBIGVALUE));
	}

	public void exportToXML(String sciezka) {
		//Zapisanie tej tablicy do pliku
		File plik = new File(sciezka);
		
		try	{
			plik.createNewFile();
			FileWriter strumienZapisu = new FileWriter(plik);
			strumienZapisu.write("<ProgramowanieLiniowe>\n");
			
			//Funkcja Celu
			if (funkcjaCelu != null) {
				strumienZapisu.write("\t<FunkcjaCelu>\n");
				
				strumienZapisu.write("\t\t<A>");
				strumienZapisu.write(Double.toString(funkcjaCelu.getX1()));
				strumienZapisu.write("</A>\n");
				
				strumienZapisu.write("\t\t<B>");
				strumienZapisu.write(Double.toString(funkcjaCelu.getX2()));
				strumienZapisu.write("</B>\n");
				
				strumienZapisu.write("\t\t<Cel>");
				strumienZapisu.write(funkcjaCelu.getCel());
				strumienZapisu.write("</Cel>\n");
				
				strumienZapisu.write("\t</FunkcjaCelu>\n");
			}
			
			//Ograniczenia
			for (int i=4; i < listaOgraniczen.size(); i++) {
				strumienZapisu.write("\t<Ograniczenie>\n");
				
				strumienZapisu.write("\t\t<A>");
				strumienZapisu.write(Double.toString(listaOgraniczen.get(i).getX1()));
				strumienZapisu.write("</A>\n");
				
				strumienZapisu.write("\t\t<B>");
				strumienZapisu.write(Double.toString(listaOgraniczen.get(i).getX2()));
				strumienZapisu.write("</B>\n");
				
				strumienZapisu.write("\t\t<Znak>");
				String zapis = "";
				if (listaOgraniczen.get(i).getZnak().equals("<=")) zapis = "le";
				if (listaOgraniczen.get(i).getZnak().equals(">=")) zapis = "ge";
				if (listaOgraniczen.get(i).getZnak().equals("=")) zapis = "eq";
				strumienZapisu.write(zapis);
				strumienZapisu.write("</Znak>\n");
				
				strumienZapisu.write("\t\t<C>");
				strumienZapisu.write(Double.toString(listaOgraniczen.get(i).getOgraniczenie()));
				strumienZapisu.write("</C>\n");
				
				strumienZapisu.write("\t</Ograniczenie>\n");
			}
			strumienZapisu.write("</ProgramowanieLiniowe>");
			strumienZapisu.close();
		}
		catch (IOException io)												
			{System.out.println(io.getMessage());}
		catch (Exception se)
			{System.err.println("blad sec");}
	}
	
	public void importFromXML(String sciezka) {
		
		this.clearOgraniczenia();
		
		File plik = new File(sciezka);
		

		int beginIndex;
		int endIndex;
		
		try {
			Scanner in = new Scanner(plik);
			while (in.hasNextLine()) {
				
				double A = 0;
				double B = 0;
				double C = 0;
				String temp = "";
				
				String str = in.nextLine();
				if(str.contains("<FunkcjaCelu>")){
					while (!str.contains("</FunkcjaCelu>")) {
					
						//Zgarniamy A
						if (str.matches("(.*)<A>(.*)</A>(.*)")) {
							
							beginIndex = str.indexOf("<A>") + 3;
							endIndex = str.indexOf("</A>");
							String substr =	str.substring(beginIndex, endIndex);
							A = Double.parseDouble(substr);
						}
						
						//Zgarniamy B
						if (str.matches("(.*)<B>(.*)</B>(.*)")) {
							
							beginIndex = str.indexOf("<B>") + 3;
							endIndex = str.indexOf("</B>");
							String substr =	str.substring(beginIndex, endIndex);
							B = Double.parseDouble(substr);
						}
						
						//Zgarniamy Cel
						if (str.matches("(.*)<Cel>(.*)</Cel>(.*)")) {
							
							beginIndex = str.indexOf("<Cel>") + 5;
							endIndex = str.indexOf("</Cel>");
							temp =	str.substring(beginIndex, endIndex);
						}
						
						str = in.nextLine();
					}
					
					this.funkcjaCelu = new FunkcjaCelu(A,B,temp);
					
				}
				
				if(str.contains("<Ograniczenie>")){
					while (!str.contains("</Ograniczenie>")) {
					
						//Zgarniamy A
						if (str.matches("(.*)<A>(.*)</A>(.*)")) {
							
							beginIndex = str.indexOf("<A>") + 3;
							endIndex = str.indexOf("</A>");
							String substr =	str.substring(beginIndex, endIndex);
							A = Double.parseDouble(substr);
						}
						
						//Zgarniamy B
						if (str.matches("(.*)<B>(.*)</B>(.*)")) {
							
							beginIndex = str.indexOf("<B>") + 3;
							endIndex = str.indexOf("</B>");
							String substr =	str.substring(beginIndex, endIndex);
							B = Double.parseDouble(substr);
						}
						
						//Zgarniamy C
						if (str.matches("(.*)<C>(.*)</C>(.*)")) {
							
							beginIndex = str.indexOf("<C>") + 3;
							endIndex = str.indexOf("</C>");
							String substr =	str.substring(beginIndex, endIndex);
							C = Double.parseDouble(substr);
						}
						
						//Zgarniamy Znak
						if (str.matches("(.*)<Znak>(.*)</Znak>(.*)")) {
							
							beginIndex = str.indexOf("<Znak>") + 6;
							endIndex = str.indexOf("</Znak>");
							temp =	str.substring(beginIndex, endIndex);
						}
						
						str = in.nextLine();
					}
					
					if(temp.equals("le")) temp = "<=";
					if(temp.equals("ge")) temp = ">=";
					if(temp.equals("eq")) temp = "=";
					this.addOgraniczenie(A,B,temp,C);

				}
			}
				
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("NIE MA TAKIEGO PLIKU!");
			e.printStackTrace();
		}
	}

}

