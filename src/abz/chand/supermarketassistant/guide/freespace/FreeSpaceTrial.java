package abz.chand.supermarketassistant.guide.freespace;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Pair;
import android.view.animation.LinearInterpolator;

public class FreeSpaceTrial {

	private int width;
	private int height;

	public FreeSpaceTrial(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Pair<Point, Point> detectFreeSpace(Mat rgbMat){
		Mat copyMat = new Mat();
		Imgproc.cvtColor(rgbMat, copyMat, Imgproc.COLOR_RGB2YUV);
		Imgproc.cvtColor(copyMat, copyMat, Imgproc.COLOR_RGB2GRAY);
		Imgproc.GaussianBlur(copyMat, copyMat, new Size(9, 9), 0, 0);
		Imgproc.Canny(copyMat, copyMat, 8, 16);

		Scalar color = new Scalar(255,0,0);
		Point start = new Point(width-100, height/2);

		Core.circle(rgbMat, start, 3, color, 5);

		Mat lines = houghLines(copyMat, rgbMat);

		Pair<Point, Point> right = new GoRight(width, height).getDirection(lines, start, rgbMat);

		if (right == null){
			Pair<Point, Point> straight = new GoStraight(width, height).getDirection(lines, start, rgbMat);

			if (straight == null){
				Pair<Point, Point> left = new GoLeft(width, height).getDirection(lines, start, rgbMat);

				return left;
			}

			return straight;
		}

		return right;		
	}

	public Mat freeSpaceDetection(Mat rgbMat){
		Mat copyMat = new Mat();
		//		Imgproc.cvtColor(rgbMat, copyMat, Imgproc.COLOR_RGB2GRAY);
		//		Imgproc.GaussianBlur(copyMat, copyMat, new Size(31, 31), 0, 0);

		//		Imgproc.cvtColor(rgbMat, copyMat, Imgproc.COLOR_RGB2BGR);
		Imgproc.cvtColor(rgbMat, copyMat, Imgproc.COLOR_RGB2YUV);
		//		Imgproc.cvtColor(rgbMat, copyMat, Imgproc.COLOR_RGB2YCrCb);

		Imgproc.cvtColor(copyMat, copyMat, Imgproc.COLOR_RGB2GRAY);
		//		Imgproc.cvtColor(copyMat, copyMat, Imgproc.COLOR_YUV2GRAY_IYUV);
		Imgproc.GaussianBlur(copyMat, copyMat, new Size(9, 9), 0, 0);


		//		Imgproc.Canny(copyMat, copyMat, 10, 30);
		Imgproc.Canny(copyMat, copyMat, 8, 16);

		Scalar color = new Scalar(255,0,0);
		Point start = new Point(width-100, height/2);

		Core.circle(rgbMat, start, 3, color, 5);

		Mat lines = houghLines(copyMat, rgbMat);

		new GoStraight(width, height).getDirection(lines, start, rgbMat);
//		new GoLeft(width, height).getDirection(lines, start, rgbMat);
//		new GoRight(width, height).getDirection(lines, start, rgbMat);

		//getTheFirst10ClosestThat do not have the same m and c

		//		double leftEdge = houghLines(copyMat, rgbMat, start);
		//
		//		if (leftEdge == Double.MAX_VALUE){
		//			return copyMat;
		//		}

//		return copyMat;
		//								return boundaries;
				return rgbMat;
	}

	private void parallelLine(Mat rgbMat, Point start, LineEquation lineEquation, List<LineSegment> lineSegments) {
		// c = y - mx
		// mx = -c
		// x = -c/m				

		double m = lineEquation.getM();
		double c = lineEquation.getC();		

		Scalar color2 = new Scalar(255,255,0);		

		System.out.println("Dasu: " + lineSegments.size());

		for (double i = start.x; i >= 0; i-=2){

			double iy = m * i + c;

			double dist = LineSegmentIntersection.ptDist(new Point(i, iy), start);
			if (dist > 200){
				break;
			}

			double im = -1/m;
			double ic = iy - (im * i);

			double yStart = 0;
			double xStart = (yStart - ic) / im;

			double yEnd = height;
			double xEnd = (yEnd - ic) / im;

			Point p = new Point(xStart, yStart);
			Point q = new Point(xEnd, yEnd);

			for (LineSegment lineSegment : lineSegments){		
				if (LineSegmentIntersection.linesIntersect(p, q, lineSegment.getPoint1(), lineSegment.getPoint2())){
					break;
				}				
			}	
			System.out.println("Basu: " + dist);

			Core.line(rgbMat, p, q, color2, 2);
		}
	}

	private Mat houghLines(Mat copyMat, Mat rgbMat){
		Mat lines = new Mat();
		//		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 80, 10, 30);
		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 30, 3, 30);

		System.out.println("Size: " + lines.cols());
		Scalar color = new Scalar(255,0,0);

		for (int i = 0; i < lines.cols(); i++){			
			double[] points = lines.get(0, i); 
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];
			Point pt1 = new Point(x1, y1);
			Point pt2 = new Point(x2, y2);
			double theta = Math.atan2(y1 - y2, x1 - x2);
			double m = (y1 - y2) / (x1 - x2);
			double c = y1 - (m * x1);

			System.out.println("Theta: " + Math.toDegrees(theta));
			//			if (Math.toDegrees(theta) > 170 || Math.toDegrees(theta) < -170){
			Core.line(rgbMat, pt1, pt2, color, 3);
			//			}
		}

		return lines;
	}

	private double houghLines(Mat boundaries, Mat rgbMat, Point start){
		Mat lines = new Mat();
		//		Imgproc.HoughLinesP(boundaries, lines, 1, Math.PI/180, 50, 30, 30);
		//		Imgproc.HoughLinesP(boundaries, lines, 1, Math.PI/180, 80, 20, 20);
		Imgproc.HoughLinesP(boundaries, lines, 1, Math.PI/180, 80, 10, 30);

		Scalar color = new Scalar(0,255,255);
		Scalar color2 = new Scalar(0,0,255);
		Scalar color3 = new Scalar(255, 255, 0);

		LineEquation[] closestLines = getClosestLines(lines, start);        

		LineEquation straight = getStraightGradient(closestLines, start);

		double ya = straight.getM() * 0 + straight.getC();
		Core.line(rgbMat, start, new Point(0, ya), color3, 2);

		List<LineSegment> leftLineSegments = getSimilarLeftLines(lines, closestLines[0], straight);					
		List<LineSegment> rightLineSegments = getSimilarRightLines(lines, closestLines[1], straight);

		parallelLine(rgbMat, start, straight, leftLineSegments);
		//		parallelLine(rgbMat, start, straight, rightLineSegments);

		for (LineSegment ls : leftLineSegments){
			Core.line(rgbMat, ls.getPoint1(), ls.getPoint2(), color, 2);
		}

		for (LineSegment ls : rightLineSegments){
			Core.line(rgbMat, ls.getPoint1(), ls.getPoint2(), color2, 2);
		}

		return straight.getM();
	}

	private List<LineSegment> getSimilarLeftLines(Mat lines, LineEquation closestLine,
			LineEquation straight) {

		List<LineSegment> lineSegments = new ArrayList<LineSegment>();

		double ya = straight.getM() * 0 + straight.getC();
		double yb = straight.getM() * width + straight.getC();	

		Point a = new Point(0, ya);
		Point b = new Point(width, yb);

		for (int i = 0; i < lines.cols(); i++){			
			double[] points = lines.get(0, i); 
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];
			Point l1 = new Point(x1, y1);
			Point l2 = new Point(x2, y2);
			double m = (y1 - y2) / (x1 - x2);
			double c = y1 - (m * x1);			

			if (LineSegmentIntersection.isLeft(a, b, l1) &&
					LineSegmentIntersection.isLeft(a, b, l2)){
				System.out.println("AAkeM:" + closestLine.getM() + ", " + m);
				System.out.println("AAkeL:" + Math.toDegrees(Math.abs(Math.atan(m) - Math.atan(closestLine.getM()))));

				if (Math.abs(Math.atan(m) - Math.atan(closestLine.getM())) <= Math.toRadians(10) &&
						Math.abs(c - closestLine.getC()) <= 10){
					lineSegments.add(new LineSegment(points));	
				} else {
					System.out.println("AAkeC:" + Math.abs(c - closestLine.getC()));
				}
			}
		}

		return lineSegments;
	}

	private List<LineSegment> getSimilarRightLines(Mat lines, LineEquation closestLine,
			LineEquation straight) {

		List<LineSegment> lineSegments = new ArrayList<LineSegment>();

		double ya = straight.getM() * 0 + straight.getC();
		double yb = straight.getM() * width + straight.getC();	

		Point a = new Point(0, ya);
		Point b = new Point(width, yb);


		for (int i = 0; i < lines.cols(); i++){			
			double[] points = lines.get(0, i); 
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];
			Point l1 = new Point(x1, y1);
			Point l2 = new Point(x2, y2);
			double m = (y1 - y2) / (x1 - x2);
			double c = y1 - (m * x1);			

			if (!LineSegmentIntersection.isLeft(a, b, l1) &&
					!LineSegmentIntersection.isLeft(a, b, l2)){
				if (Math.abs(m - closestLine.getM()) <= Math.toRadians(10) &&
						Math.abs(c - closestLine.getC()) <= 5){
					lineSegments.add(new LineSegment(points));	
				}
			}
		}

		return lineSegments;
	}

	private LineEquation getStraightGradient(LineEquation[] closestLines, Point start) {
		LineEquation leftLine = closestLines[0];		
		LineEquation rightLine = closestLines[1];

		if (leftLine.getM() == Integer.MAX_VALUE &&
				rightLine.getM() == Integer.MAX_VALUE){
			return new LineEquation(0, height/2);
		} else if (leftLine.getM() == Integer.MAX_VALUE){
			double c = start.y - (rightLine.getM() * start.x);			
			return new LineEquation(rightLine.getM(), c);
		} else if (rightLine.getM() == Integer.MAX_VALUE){
			System.out.println("Nakhre");
			double c = start.y - (leftLine.getM() * start.x);			
			return new LineEquation(leftLine.getM(), c);
		} else if (Math.abs(leftLine.getM() - rightLine.getM()) <= 0.01){			
			double c = start.y - (leftLine.getM() * start.x);			
			return new LineEquation(leftLine.getM(), c);
		}

		System.out.println("Nakhre2: " + leftLine.getM() + ", " + rightLine.getM());
		double x = (leftLine.getC() - rightLine.getC())/(rightLine.getM()-leftLine.getM());
		double y = rightLine.getM() * x + rightLine.getC();		
		double m = (y - start.y) / (x - start.x);		
		m = Math.max(m, 0.01); // to avoid m = 0;		
		double c = y - (m*x);

		return new LineEquation(m, c);
	}

	private LineEquation[] getClosestLines(Mat lines, Point start) {

		double x = start.x;
		double y = start.y;

		LineEquation[] closestLines = new LineEquation[2];
		closestLines[0] = new LineEquation(Integer.MAX_VALUE, width);
		closestLines[1] = new LineEquation(Integer.MAX_VALUE, width);

		double nearestLeftLine = Integer.MAX_VALUE;
		double nearestRightLine = Integer.MAX_VALUE;

		for (int i = 0; i < lines.cols(); i++){			
			double[] points = lines.get(0, i); 
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];

			//			double theta = Math.atan2(y1 - y2, x1 - x2);

			//			if (Math.toDegrees(theta) > 170 || Math.toDegrees(theta) < -170){
			//				continue;
			//			}

			//			double middleY = (Math.min(y1, y2) + Math.abs(y1-y2));
			double middleY = Math.max(y1, y2);

			double dist = LineSegmentIntersection.ptSegDist(x1, y1, x2, y2, x, y);				

			double minX = Math.min(x1, x2);

			if (minX > x){
				continue;
			}

			if (middleY >= y){
				if (dist < nearestLeftLine){
					double m = (y1 - y2) / (x1 - x2);
					double c = y1 - (m * x1);
					closestLines[0].setLineEquation(m, c); 
					nearestLeftLine = dist;
				}
			} else {
				if (dist < nearestRightLine){
					double m = (y1 - y2) / (x1 - x2);
					double c = y1 - (m * x1);
					closestLines[1].setLineEquation(m, c); 
					nearestRightLine = dist;
				}				
			}
		}

		return closestLines;
	}

	//	private List<LinePoints> houghLinesNearEdge(Mat copyMat, Mat rgbMat, double leftEdge){
	//
	//		Mat lines = new Mat();
	//		//		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 100, 5, 30);
	//		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 50, 15, 30);
	//
	//		System.out.println("Size: " + lines.cols());
	//		Scalar color = new Scalar(0,0,255);
	//
	//		List<LinePoints> linePoints = new ArrayList<LinePoints>();
	//
	//		for (int i = 0; i < lines.cols(); i++){			
	//			double[] points = lines.get(0, i); 
	//			double x1 = points[0];
	//			double y1 = points[1];
	//			double x2 = points[2];
	//			double y2 = points[3];
	//			Point pt1 = new Point(x1, y1);
	//			Point pt2 = new Point(x2, y2);
	//			double theta = Math.atan2(y1 - y2, x1 - x2);
	//			double m = (y1 - y2) / (x1 - x2);
	//			double c = y1 - (m * x1);
	//
	//			if (Math.abs(m - leftEdge) >= Math.toRadians(5)){
	//				continue;
	//			}
	//
	//			linePoints.add(new LinePoints(points));
	//
	//			System.out.println("Theta: " + Math.toDegrees(theta));
	//			Core.line(rgbMat, pt1, pt2, color, 5);
	//		}
	//
	//		return linePoints;
	//	}

}

