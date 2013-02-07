package abz.chand.supermarketassistant.guide.freespace;

import org.opencv.core.Point;



public class LineSegment {

	private Point p1;
	private Point p2;

	public LineSegment(double[] points) {
		this.p1 = new Point(points[0], points[1]);
		this.p2 = new Point(points[2], points[3]);
	}
	
	public Point getPoint1(){
		return p1;
	}
	
	public Point getPoint2(){
		return p2;
	}
	
}
