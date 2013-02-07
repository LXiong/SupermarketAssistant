package abz.chand.supermarketassistant.guide.frameprocessing;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import abz.chand.supermarketassistant.guide.freespace.LineSegmentIntersection;

public class AngleDetection {

	public double getAngle(Point center, Mat grayMat, Mat rgbMat) {
		return Math.toDegrees(houghLines(grayMat, rgbMat, center)) + 90;
	}

	private double houghLines(Mat copyMat, Mat rgbMat, Point center){
		Mat lines = new Mat();
		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 35, 10, 10);

		Scalar color = new Scalar(255,0,0);

		double smallestDistance = Integer.MAX_VALUE;
		int lineNumber = 0;


		for (int i = 0; i < lines.cols(); i++){			
			double[] points = lines.get(0, i); 
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];
			Point l1 = new Point(x1, y1);
			Point l2 = new Point(x2, y2);

			double dist = LineSegmentIntersection.ptSegDist(l1, l2, center);

			if (dist < smallestDistance){
				smallestDistance = dist;
				lineNumber = i;
			}			

			double theta = Math.atan2(y1 - y2, x1 - x2);
			double m = (y1 - y2) / (x1 - x2);
			double c = y1 - (m * x1);		
		}

		double[] points = lines.get(0, lineNumber);
		double theta = 0;
		
		if (points != null){
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];
			Point l1 = new Point(x1, y1);
			Point l2 = new Point(x2, y2);

			double d1 = pointDistance(l1, center);
			double d2 = pointDistance(l2, center);

			if (d1 > d2){
				theta = Math.atan2(y1 - y2, x1 - x2);
			} else {
				theta = Math.atan2(y2 - y1, x2 - x1);
			}

			Core.line(rgbMat, l1, l2, color, 3);
		}
		return theta;
	}

	private double pointDistance(Point p1, Point p2){
		return Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y, 2));
	}

}
