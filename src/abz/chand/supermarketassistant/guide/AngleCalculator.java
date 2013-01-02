package abz.chand.supermarketassistant.guide;

import org.opencv.core.Point;

public class AngleCalculator {

	public AngleCalculator() {
	
	}

	public int findAngle(Point a, Point b, Point c) {

		boolean left = isLeft(a, b, c);

		double bax = b.x-a.x;
		double bay = b.y-a.y;

		if (left){
			double theta = Math.atan2(bax, -1*bay);
			return (int) Math.toDegrees(theta) - 90;
		} else {
			double theta = Math.atan2(-1*bax, bay);
			return (int) Math.toDegrees(theta) - 90;
		}

	}

	private boolean isLeft(Point a, Point b, Point c){
		return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
	}

}
