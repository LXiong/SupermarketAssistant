package abz.chand.supermarketassistant.guide.freespace;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class BestLines {

	private final static double mThreshold  = 10;
	private final static double cThreshold  = 10;
	
	private final static int MAX_LINES = 5;
	
	private TreeSet<LineEquation> closestLines;

	private List<LineEquation> lineEquations;

	public BestLines() {
		closestLines = new TreeSet<LineEquation>();
		lineEquations = new ArrayList<LineEquation>();
	}

	public List<LineEquation> getBestFitLines(Mat lines, Point start) {
		closestLines.clear();
		lineEquations.clear();

		double x = start.x;
		double y = start.y;

		for (int i = 0; i < lines.cols(); i++){			
			double[] points = lines.get(0, i); 
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];

			double middleY = Math.max(y1, y2);

			double dist = LineSegmentIntersection.ptSegDist(x1, y1, x2, y2, x, y);				

			double minX = Math.min(x1, x2);

			if (minX > x){
				continue;
			}

			double m = (y1 - y2) / (x1 - x2);
			double c = y1 - (m * x1);

			closestLines.add(new LineEquation(m, c));
		}
		
		double newM = 0;
		double newC = 0;
		
//		int i = 0;
//		while (i < MAX_LINES){
//			if (i >= closestLines.size()){
//				break;
//			}
//
//			closestLines.
//					}		

		return lineEquations;
	}
}
