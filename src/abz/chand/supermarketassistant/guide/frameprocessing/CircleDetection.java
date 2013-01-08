package abz.chand.supermarketassistant.guide.frameprocessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CircleDetection {
	
	private Mat circles; 
	private Mat copyMat; 
	private List<Point> centerPoints;
	
	
	public CircleDetection(){
		circles = new Mat();
		copyMat = new Mat();
		centerPoints = new ArrayList<Point>();
	}

	public List<Point> getCirclePoints(Mat mat){
		centerPoints.clear();
		
//		Imgproc.cvtColor(mat, copyMat, Imgproc.COLOR_YUV2GRAY_NV21);
//		Imgproc.cvtColor(mat, copyMat, Imgproc.COLOR_RGBA2GRAY);
//		Imgproc.cvtColor(mat, copyMat, Imgproc.COLOR_BGR2GRAY);
		Imgproc.cvtColor(mat, copyMat, Imgproc.COLOR_RGB2GRAY);
		
		
		Imgproc.GaussianBlur(copyMat, copyMat, new Size(9, 9), 0, 0);
		Imgproc.Canny(copyMat, copyMat, 10, 100);
		Imgproc.HoughCircles(copyMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 50, 100, 10, 10, 320);
	
//		Imgproc.HoughCircles(copyMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 10, 100, 10, 10, 1000);
		
		for (int i = 0; i < circles.cols(); i++){			
			double[] circleAttributes = circles.get(i,0);
			if (circleAttributes == null){
				continue;
			}
			centerPoints.add(new Point(circleAttributes[0], circleAttributes[1]));	
		}
		
		return centerPoints;
	}
	
}
