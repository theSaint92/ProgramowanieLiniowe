package logic;

public class Ograniczenie {

	private double x1;
	private double x2;
	private String znak;
	private double ograniczenie;

	public Ograniczenie(double x1, double x2, String znak, double ograniczenie) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.znak = znak;
		this.ograniczenie = ograniczenie;
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

	public String getZnak() {
		return znak;
	}

	public void setZnak(String znak) {
		this.znak = znak;
	}

	public double getOgraniczenie() {
		return ograniczenie;
	}

	public void setOgraniczenie(double ograniczenie) {
		this.ograniczenie = ograniczenie;
	}
	
	public boolean isConditionTrue(double p1, double p2) {
		boolean result = false;
		if (znak.equals("=")) {
			if (x1*p1+x2*p2 == ograniczenie) result = true;
		}
		if (znak.equals(">=")) {
				if (x1*p1+x2*p2 >= ograniczenie) result = true;
		}
		if (znak.equals("<=")) {
			if (x1*p1+x2*p2 <= ograniczenie) result = true;
		}
		return result;
	}
	
	@Override
	public String toString() {
		if (x1 == 0) {
			return x2  + "x2 " + znak + " " + ograniczenie;
		}
		else if  (x2 == 0) {
			return x1  + "x1 " + znak + " " + ograniczenie;
		}
		else {
			return x1 + "x1 + " + x2  + "x2 " + znak + " " + ograniczenie;
		}
		
	}
	

}
