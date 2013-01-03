package abz.chand.supermarketassistant.guide;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class ProcessFrame {
	
	private static final int MAX_CONSISTENT_FRAMES = 4;
	
	private int width;
	private int height;
	private MatConverter matConverter;
	private CircleDetection circleDetection;
	private AngleCorrection angleCorrection;
	private String[] frameData;
	private int frameDataIndex;
	
	public ProcessFrame(int width, int height){
		this.width = width;
		this.height = height;	
		matConverter = new MatConverter(width, height);
		circleDetection = new CircleDetection();
		angleCorrection = new AngleCorrection();
		frameData = new String[MAX_CONSISTENT_FRAMES];
		init();
	}

	private void init() {
		frameDataIndex = 0;
		for (int i = 0; i < MAX_CONSISTENT_FRAMES; i++){
			frameData[i] = "";
		}
	}

	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}

	public Mat getStickerData(byte[] data) {
		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);
		List<Point> centerPoints = circleDetection.getCirclePoints(rgbMat);
		Mat hsvMat = matConverter.convertRgbToHsv(rgbMat);
		angleCorrection.adjustFrameAngle(centerPoints, hsvMat, rgbMat);	
		drawCircleCenters(rgbMat, centerPoints); //Comment this later
		return rgbMat;	
	}
	
	public void getStickerDataString(byte[] data) {
		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);
		List<Point> centerPoints = circleDetection.getCirclePoints(rgbMat);
		Mat hsvMat = matConverter.convertRgbToHsv(rgbMat);
		String fData = angleCorrection.adjustFrameAngle(centerPoints, hsvMat, rgbMat);
		frameData[frameDataIndex] = fData;
		if (checkIfFrameDataConsitent()){
			System.out.println("DATA: " + frameData[0]);	
		}
		frameDataIndex = (frameDataIndex + 1) % MAX_CONSISTENT_FRAMES;
	}

	private void drawCircleCenters(Mat mat, List<Point> centerPoints) {
		Scalar color = new Scalar(0,255, 0);
		for (Point cp : centerPoints){
			Core.circle(mat, cp, 1, color, 1);
		}
	}
	
	private boolean checkIfFrameDataConsitent(){
		String data = frameData[0]; 
		
		for (int i = 1; i < MAX_CONSISTENT_FRAMES; i++){
			if (!data.equals(frameData[i])){
				return false;
			}
		}
		
		return true;
	}

}
