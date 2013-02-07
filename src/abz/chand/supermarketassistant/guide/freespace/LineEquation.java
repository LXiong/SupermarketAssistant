package abz.chand.supermarketassistant.guide.freespace;




public class LineEquation {

	private double m;
	private double c;

	public LineEquation(double m, double c) {
		setLineEquation(m, c);
	}
	
	public double getM(){
		return m;
	}
	
	public double getC(){
		return c;
	}

	public void setLineEquation(double m, double c) {
		this.m = m;
		this.c = c;
	}
	
}
