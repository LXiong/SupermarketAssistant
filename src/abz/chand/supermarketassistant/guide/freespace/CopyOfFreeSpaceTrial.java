package abz.chand.supermarketassistant.guide.freespace;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Canvas;

public class CopyOfFreeSpaceTrial {

	private int width;
	private int height;

	public CopyOfFreeSpaceTrial(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Mat freeSpaceDetection(Mat rgbMat){
		Mat copyMat = new Mat();
		Imgproc.cvtColor(rgbMat, copyMat, Imgproc.COLOR_RGB2GRAY);

		//		Imgproc.threshold(copyMat, copyMat, 30, 255, Imgproc.THRESH_BINARY);
		Imgproc.GaussianBlur(copyMat, copyMat, new Size(9, 9), 0, 0);
		//		Imgproc.Canny(copyMat, copyMat, 30, 60);
		Imgproc.Canny(copyMat, copyMat, 20, 60);

		Scalar color = new Scalar(255,0,0);
		Point start = new Point(width-200, height/2);

		Core.circle(rgbMat, start, 3, color, 5);

		double leftEdge = houghLinesLeft(copyMat, rgbMat, start);

		if (leftEdge == Double.MAX_VALUE){
			return copyMat;
			//			return rgbMat;	
		}

		houghLines(copyMat, rgbMat);

		List<LineSegment> linePoints = houghLinesNearEdge(copyMat, rgbMat, leftEdge);		

		parallelLine(rgbMat, start, leftEdge, linePoints);

		//				return copyMat;
		//						return boundaries;
		return rgbMat;
	}

	private void parallelLine(Mat rgbMat, Point start, double m, List<LineSegment> linePoints) {

		Scalar color = new Scalar(255,255,0);		
		// c = y - mx
		// mx = -c
		// x = -c/m				
		double c = start.y - (m * start.x);		
		double x = -c/m;		
		Core.line(rgbMat, start, new Point(x, 0), color, 5);

		Scalar color2 = new Scalar(255,255,255);		


		for (double y = start.y; y >= 0; y-=2){
			double ix = (y - c) / m;
			double im = -1/m;

			double ic = y - (im * ix);

			double yEnd = height;
			double xEnd = (yEnd - ic) / im;

			Point p = new Point(ix, y);
			Point q = new Point(xEnd, yEnd);

			boolean intersect = false;
			for (LineSegment linePoint : linePoints){
				if (LineSegmentIntersection.linesIntersect(p, q, linePoint.getPoint1(), linePoint.getPoint2())){
					intersect = true;
					break;
				}				
			}
			if (!intersect){				
				Core.line(rgbMat, p, q, color2, 2);
			}
		}
	}

	private void houghLines(Mat copyMat, Mat rgbMat){
		Mat lines = new Mat();
		//		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 100, 5, 30);
		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 50, 15, 30);

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
			Core.line(rgbMat, pt1, pt2, color, 5);
		}
	}

	private double houghLinesLeft(Mat boundaries, Mat rgbMat, Point start){
		Mat lines = new Mat();

		Imgproc.HoughLinesP(boundaries, lines, 1, Math.PI/180, 50, 15, 30);

		int x = (int) start.x;
		int y = (int) start.y;

		System.out.println("Size: " + lines.cols());
		Scalar color = new Scalar(0,255,255);

		double m = Double.MAX_VALUE;
		
		
		double nearestLine = height-1;  

		for (int i = 0; i < lines.cols(); i++){			
			double[] points = lines.get(0, i); 
			double x1 = points[0];
			double y1 = points[1];
			double x2 = points[2];
			double y2 = points[3];

			double middleY = (Math.min(y1, y2) + Math.abs(y1-y2));
			
			if (x >= x1 && x <=x2 && middleY >= start.y){
				//				Core.line(rgbMat, start, new Point(start.x+xd, start.y+yd), color, 5);

				double dist = LineSegmentIntersection.ptSegDist(x1, y1, x2, y2, start.x, start.y);				
				if (dist < nearestLine){
					m = (y1 - y2) / (x1 - x2);
					nearestLine = dist;
				}
			}
		}

		return m;
	}


	private List<LineSegment> houghLinesNearEdge(Mat copyMat, Mat rgbMat, double leftEdge){

		Mat lines = new Mat();
		//		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 100, 5, 30);
		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 50, 15, 30);

		System.out.println("Size: " + lines.cols());
		Scalar color = new Scalar(0,0,255);

		List<LineSegment> linePoints = new ArrayList<LineSegment>();

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

			if (Math.abs(m - leftEdge) >= Math.toRadians(10)){
				continue;
			}

			linePoints.add(new LineSegment(points));

			System.out.println("Theta: " + Math.toDegrees(theta));
			Core.line(rgbMat, pt1, pt2, color, 5);
		}

		return linePoints;
	}

}

