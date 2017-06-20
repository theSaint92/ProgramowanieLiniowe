package logic;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProgramowanieLiniowe prog = new ProgramowanieLiniowe();
		prog.addOgraniczenie(3, 5, ">=", 7);
		prog.editFunkcjaCelu(2, 3, "min");
		
		System.out.println(prog.getResult());
		
		prog.rysujZbiorDopuszczalny(400);
		

	}

}
