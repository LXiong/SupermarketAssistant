package abz.chand.supermarketassistant.guide.freespace;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import android.util.Pair;

public class GoRight implements Go {

	private final static int MAX_LINES = 20;
	
	private int width;
	private int height;
	private Pair<Point, Point> directionLine;

	public GoRight(int width, int height) {
		this.width = width;
		this.height = height;
		directionLine = new Pair<Point, Point>(new Point(), new Point());
	}

	@Override
	public Pair<Point, Point> getDirection(Mat lines, Point start, Mat rgbMat) {
		double y = start.y;

		Scalar color = new Scalar(0,0,255);

		double startX = width/MAX_LINES;
				
		int count = 0;
		
		for (double x = width-startX; x > 0; x -= startX){
			boolean intersects = false;

			for (int i = 0; i < lines.cols(); i++){			
				double[] points = lines.get(0, i); 
				double x1 = points[0];
				double y1 = points[1];
				double x2 = points[2];
				double y2 = points[3];
				
				double minX = Math.min(x1, x2);

				if (minX > x){
					continue;
				}			

				intersects = LineSegmentIntersection.linesIntersect(x1, y1, x2, y2, x, y, x, 0);
				if (intersects){
					break;
				}
			}
			if (!intersects){
				count++;
			} else {
				count = 0;
				continue;
			}

			if (count >= 4){
				Core.line(rgbMat, new Point(x, y), new Point(x, 0), color, 2);
				directionLine.first.x = x;
				directionLine.first.y = y;
				directionLine.second.x = x;
				directionLine.second.y = 0;
				return directionLine;
			}
		}

		return null;
	}
}
