package logic;

public class FunkcjaCelu {

	private double x1;
	private double x2;
	private String cel;
	
	public FunkcjaCelu(double x1, double x2, String cel) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.cel = cel;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public String getCel() {
		return cel;
	}

	public void setCel(String cel) {
		this.cel = cel;
	}
	
	public double valueOf(double p1, double p2) {
		return x1*p1 + x2*p2;
	}

	@Override
	public String toString() {
		String result = "<html>f(x,y) = ";
		if (x1 != 0) result += x1 + "x ";
		if (x1 != 0 && x2 != 0) result += "+ ";
		if (x2 != 0) result += x2 + "y ";
		result += "&rarr " + cel;
		return result;
	}
	
	
	

}
