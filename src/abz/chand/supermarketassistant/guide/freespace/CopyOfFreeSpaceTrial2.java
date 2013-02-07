package abz.chand.supermarketassistant.guide.freespace;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CopyOfFreeSpaceTrial2 {

	private int width;
	private int height;

	public CopyOfFreeSpaceTrial2(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Mat freeSpaceDetection(Mat rgbMat){
		Mat copyMat = new Mat();
		Imgproc.cvtColor(rgbMat, copyMat, Imgproc.COLOR_RGB2GRAY);

		Imgproc.GaussianBlur(copyMat, copyMat, new Size(9, 9), 0, 0);
		Imgproc.Canny(copyMat, copyMat, 20, 50);

		Scalar color = new Scalar(255,0,0);
		Point start = new Point(width-200, height/2);

		Core.circle(rgbMat, start, 3, color, 5);

		Mat boundaries = Mat.zeros(rgbMat.size(), rgbMat.type());

		findBetterEdgeCollision(boundaries, copyMat, start);

		Imgproc.cvtColor(boundaries, boundaries, Imgproc.COLOR_RGB2GRAY);

		houghLines(boundaries, rgbMat, start);
		
		houghLines(boundaries, rgbMat);
		//		Point edgeBoundary = findEdgeCollision(copyMat, start);		
		//		System.out.println("jjjj x: " + edgeBoundary.x + " , y:" + edgeBoundary.y);
		//		Scalar color2 = new Scalar(0,255,0);
		//		Core.circle(rgbMat, edgeBoundary, 3, color2, 5);

				return copyMat;
//				return boundaries;
//		return rgbMat;
	}

	private Point findBetterEdgeCollision(Mat rgbMat, Mat copyMat, Point start) {

		//		findTop(rgbMat, copyMat, start);
		findLeft(rgbMat, copyMat, start);
		//		findRight(rgbMat, copyMat, start);

		return start;
	}


	private void findRight(Mat rgbMat, Mat copyMat, Point start) {
		int x = (int) start.x;
		int y = (int) start.y;

		Scalar color2 = new Scalar(0,255,0);
		for (int i = x; i >= x - width/2; i--){
			for (int j = y; j >= 0; j--){
				double[] ds = copyMat.get(j, i);
				boolean isEdge = ds[0] != 0;

				if (isEdge){
					Core.circle(rgbMat, new Point(i, j), 1, color2, 1);
					break;
				}

			}
		}	
	}

	private void findTop(Mat rgbMat, Mat copyMat, Point start) {
		int x = (int) start.x;

		Scalar color2 = new Scalar(0,255,0);
		for (int j = 0; j < height; j++){
			for (int i = x; i >= x - width/2; i--){
				double[] ds = copyMat.get(j, i);
				boolean isEdge = ds[0] != 0;

				if (isEdge){
					Core.circle(rgbMat, new Point(i, j), 1, color2, 1);
					break;
				}

			}
		}	
	}


	private void findLeft(Mat rgbMat, Mat copyMat, Point start) {
		int x = (int) start.x;
		int y = (int) start.y;

		Scalar color2 = new Scalar(0,255,0);
		for (int i = x; i >= x - width/2; i--){
			for (int j = y; j < height; j++){
				double[] ds = copyMat.get(j, i);

				boolean isEdge = ds[0] != 0;

				if (isEdge){
					Core.circle(rgbMat, new Point(i, j), 1, color2, 1);
					break;
				}

			}
		}	
	}

	private Point findEdgeCollision(Mat copyMat, Point start) {
		double x = start.x;

		double[] ds = copyMat.get((int) start.y, (int) x);
		boolean isEdge = ds[0] != 0;
		System.out.println("ds: " + ds[0] + " llength: " + ds.length);
		while (!isEdge){
			if (Math.abs(x-start.x) <= width/2){
				break;
			}
			x--;
			System.out.println("xxxx:" + x + ", " + ds[0]);
			ds = copyMat.get((int) start.y, (int) x);
			isEdge = ds[0] != 0;
		}

		return new Point(x, start.y);
	}

	private void houghLines(Mat copyMat, Mat rgbMat){
		Mat lines = new Mat();
		//		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 100, 5, 30);
		Imgproc.HoughLinesP(copyMat, lines, 1, Math.PI/180, 50, 5, 30);

		System.out.println("Size: " + lines.cols());
		Scalar color = new Scalar(0,0,255);

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


	private void houghLines(Mat boundaries, Mat rgbMat, Point start){
		Mat lines = new Mat();

		Imgproc.HoughLinesP(boundaries, lines, 1, Math.PI/180, 50, 5, 30);

		int x = (int) start.x;
		int y = (int) start.y;
		
		System.out.println("Size: " + lines.cols());
		Scalar color = new Scalar(0,0,255);

		boolean crash = false;

		for (int j = x; j >= x - width/2; j--){
			for (int i = 0; i < lines.cols(); i++){			
				double[] points = lines.get(0, i); 
				double x1 = points[0];
				double x2 = points[2];

				if (j >= x1 && j <=x2){
					crash = true;
					break;
				}
			}
			if (!crash){
				Core.line(rgbMat, new Point(j, y), new Point(j, height - 1), color, 5);
				break;
			}
			crash = false;
		}
	}
}

