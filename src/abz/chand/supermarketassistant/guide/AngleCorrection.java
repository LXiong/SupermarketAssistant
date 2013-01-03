package abz.chand.supermarketassistant.guide;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class AngleCorrection {

	private ColourRanges colourRanges;
	private IntersectionDetector intersectionDetector;
	private AngleCalculator angleCalculator;
	private ProcessFrameData processFrameData;

	public AngleCorrection() {
		colourRanges = new ColourRanges();
		intersectionDetector = new IntersectionDetector();
		angleCalculator = new AngleCalculator();
		processFrameData = new ProcessFrameData();
	}

	public String adjustFrameAngle(List<Point> centerPoints, Mat mat, Mat rgbMat){	
		Scalar color2 = new Scalar(0, 0, 255);
		for (Point centerPoint : centerPoints){
			if (isDetectedCircleCenterWhite(centerPoint, mat)){
				Point[] points = intersectionDetector.getBlackEdgePoints(centerPoint, mat);
				if (isPointBlack(points[0]) && isPointBlack(points[1])){
					int angle = angleCalculator.findAngle(points[0], points[1], centerPoint);
					for (int i = 0; i < points.length; i++){	
						Core.circle(rgbMat, points[i], 1, color2, 2); //Comment this later
					}
					Mat mapMat = Imgproc.getRotationMatrix2D(centerPoint, angle, 1);
					Imgproc.warpAffine(rgbMat, rgbMat, mapMat, new Size(), Imgproc.INTER_LINEAR);
					Imgproc.warpAffine(mat, mat, mapMat, new Size(), Imgproc.INTER_LINEAR);
					return processFrameData.getData(mat, centerPoint);	
				}
			}
		}
		return "Empty";
	}

	private boolean isDetectedCircleCenterWhite(Point point, Mat mat){
		double[] pixelData = mat.get((int) point.y, (int) point.x);
		return colourRanges.isWhite((int) pixelData[0], (int) pixelData[1], (int) pixelData[2]);
	}

	private boolean isPointBlack(Point point) {
		return point.x >= 0 && point.y >= 0;
	}
}
