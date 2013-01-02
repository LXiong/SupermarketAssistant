package abz.chand.supermarketassistant.guide;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class ProcessFrame {
	
	private int width;
	private int height;
	private MatConverter matConverter;
	private CircleDetection circleDetection;
	private AngleCorrection angleCorrection;

	public ProcessFrame(int width, int height){
		this.width = width;
		this.height = height;	
		matConverter = new MatConverter(width, height);
		circleDetection = new CircleDetection();
		angleCorrection = new AngleCorrection();
	}

	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}

	public Mat getStickerData(byte[] data) {
		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgba(mat);
		List<Point> centerPoints = circleDetection.getCirclePoints(rgbMat);
		Mat hsvMat = matConverter.convertRgbToHsv(rgbMat);
		angleCorrection.adjustFrameAngle(centerPoints, hsvMat, rgbMat);
		drawCircleCenters(rgbMat, centerPoints); //Comment this later
		return rgbMat;	
	}

	private void drawCircleCenters(Mat mat, List<Point> centerPoints) {
		Scalar color = new Scalar(0,255, 0);
		for (Point cp : centerPoints){
			Core.circle(mat, cp, 1, color, 1);
		}
	}


}
