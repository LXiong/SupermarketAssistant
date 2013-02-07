package abz.chand.supermarketassistant.guide.freespace;

import org.opencv.core.Point;

public class LineSegmentIntersection {

	public static boolean isLeft(Point l1, Point l2, Point c){
	     return ((l2.x - l1.x)*(c.y - l1.y) - (l2.y - l1.y)*(c.x - l1.x)) > 0;
	}
		
	public static double ptDist(Point p1, Point p2){
		return Math.sqrt(Math.pow((p1.y-p2.y), 2) + Math.pow((p1.x-p2.x),2));
	}

	
	public static boolean linesIntersect(Point p1, Point p2, Point p3, Point p4){
		return linesIntersect(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
	}

	public static double ptSegDist(Point l1, Point l2, Point p){
		return Math.sqrt(ptSegDistSq(l1.x, l1.y, l2.x, l2.y, p.x, p.y));
	}

	public static double ptSegDist(double x1, double y1,
			double x2, double y2,
			double px, double py){
		return Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
	}

	public static double ptSegDistSq(double x1, double y1,
			double x2, double y2,
			double px, double py)
	{
		x2 -= x1;
		y2 -= y1;
		px -= x1;
		py -= y1;
		double dotprod = px * x2 + py * y2;
		double projlenSq;
		if (dotprod <= 0.0) {
			projlenSq = 0.0;
		} else {
			px = x2 - px;
			py = y2 - py;
			dotprod = px * x2 + py * y2;
			if (dotprod <= 0.0) {
				projlenSq = 0.0;
			} else {
				projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
			}
		}
		double lenSq = px * px + py * py - projlenSq;
		if (lenSq < 0) {
			lenSq = 0;
		}
		return lenSq;
	}

	public static boolean linesIntersect(double x1, double y1,
			double x2, double y2,
			double x3, double y3,
			double x4, double y4) {
		return ((relativeCCW(x1, y1, x2, y2, x3, y3) *
				relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
				&& (relativeCCW(x3, y3, x4, y4, x1, y1) *
						relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
	}

	public static int relativeCCW(double x1, double y1,
			double x2, double y2,
			double px, double py)
	{
		x2 -= x1;
		y2 -= y1;
		px -= x1;
		py -= y1;
		double ccw = px * y2 - py * x2;
		if (ccw == 0.0) {

			ccw = px * x2 + py * y2;
			if (ccw > 0.0) {

				px -= x2;
				py -= y2;
				ccw = px * x2 + py * y2;
				if (ccw < 0.0) {
					ccw = 0.0;
				}
			}
		}
		return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
	}

}
